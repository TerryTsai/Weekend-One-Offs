package kinect;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.openkinect.freenect.DepthHandler;
import org.openkinect.freenect.FrameMode;
import org.openkinect.freenect.VideoHandler;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class DualRenderer {

    /**
     * Each channel has 3 working images. The WRITE image
     * is written to by the VideoHandler, DepthHandler,
     * and renderImage() method. The WRITE image is never
     * read from. The READ image is used to generate the
     * final composite image, and it is only ever read
     * from. The SWAP image is how information gets
     * passed from writing to reading. This ensures that
     * we are never reading or writing to the same image.
     * This also prevents either operation from blocking
     * the other when it is ready to flip images. The swap
     * operation is synchronized to prevent race conditions
     * arising from either reader or writer flips.
     *
     * Kinect <-> WRITE <-> SWAP <-> READ <-> JavaFX
     *
     */
    private static final int WRITE = 0, SWAP = 1, READ = 2;

    private final int width, height;
    private final AtomicBoolean videoWriteLock, depthWriteLock, resultWriteLock;
    private final WritableImage[] videos, depths, result;
    private final VideoHandler videoHandler;
    private final DepthHandler depthHandler;

    public DualRenderer(FrameMode videoMode, FrameMode depthMode) {
        if (videoMode.getWidth() != depthMode.getWidth())
            throw new RuntimeException("Video and Depth modes must have the same resolution.");
        if (videoMode.getHeight() != depthMode.getHeight())
            throw new RuntimeException("Video and Depth modes must have the same resolution.");

        this.width = videoMode.getWidth();
        this.height = videoMode.getHeight();

        this.videoWriteLock = new AtomicBoolean(false);
        this.depthWriteLock = new AtomicBoolean(false);
        this.resultWriteLock = new AtomicBoolean(false);

        this.videos = new WritableImage[]{
                new WritableImage(width, height),
                new WritableImage(width, height),
                new WritableImage(width, height)
        };

        this.depths = new WritableImage[]{
                new WritableImage(width, height),
                new WritableImage(width, height),
                new WritableImage(width, height)
        };

        this.result = new WritableImage[]{
                new WritableImage(width, height),
                new WritableImage(width, height),
                new WritableImage(width, height)
        };

        this.videoHandler = (frameMode, in, index) -> {
            if (videoWriteLock.compareAndSet(false, true)) {
                readVideo(in);
                swapItems(videos, WRITE, SWAP);
                videoWriteLock.set(false);
            }
        };

        this.depthHandler = (frameMode, in, index) -> {
            if (depthWriteLock.compareAndSet(false, true)) {
                readDepth(in);
                swapItems(depths, WRITE, SWAP);
                depthWriteLock.set(false);
            }
        };
    }

    public VideoHandler getVideoHandler() {
        return videoHandler;
    }

    public DepthHandler getDepthHandler() {
        return depthHandler;
    }

    public Image renderImage() {
        if (resultWriteLock.compareAndSet(false, true)) {
            swapItems(videos, SWAP, READ);
            swapItems(depths, SWAP, READ);
            PixelReader vr = videos[READ].getPixelReader();
            PixelReader dr = depths[READ].getPixelReader();
            PixelWriter pw = result[WRITE].getPixelWriter();
            for (int i = 0; i < width; i++) {
                for (int j = 20; j < height; j++) {
                    int v = vr.getArgb(i, j);
                    int d = dr.getArgb(i, j - 20);
                    int c = (d > 0xff000000) ? v : 0;
                    pw.setArgb(i, j, c);
                }
            }
            swapItems(result, READ, WRITE);
            resultWriteLock.set(false);
        }
        return result[READ];
    }

    private void readVideo(ByteBuffer in) {
        PixelWriter pw = videos[WRITE].getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int k = (i * 3) + (j * width * 3);
                int c = 0xf0000000 | in.get(k) << 16 | in.get(k + 1) << 8 | in.get(k + 2);
                pw.setArgb(i, j, c);
            }
        }
    }

    private void readDepth(ByteBuffer in) {
        PixelWriter pw = depths[WRITE].getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int k = (i * 2) + (j * width * 2);
                int lo = in.get(k) & 0xff;
                int hi = in.get(k + 1) & 0xff;
                int o = (hi > 0b00001111) ? 0 : (hi << 4 | lo >> 4) & 0xff;
                int c = o | 0xff000000;
                pw.setArgb(i, j, c);
            }
        }
    }

    private <T> void swapItems(T[] array, int i, int j) {
        synchronized (array) {
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

}
