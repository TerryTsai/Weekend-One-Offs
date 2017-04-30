package kinect;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.openkinect.freenect.*;

import java.io.IOException;

public class Kinect extends Application {

    private Context context;
    private Device device;
    private ImageView iv;
    private DualRenderer renderer;
    private Thread updater;
    private boolean quit;

    @Override
    public void start(Stage stage) throws Exception {
        quit = false;

        context = Freenect.createContext();

        device = context.openDevice(0);
        device.setVideoFormat(VideoFormat.RGB, Resolution.MEDIUM);
        device.setDepthFormat(DepthFormat.MM, Resolution.MEDIUM);

        renderer = new DualRenderer(device.getVideoMode(), device.getDepthMode());
        device.startVideo(renderer.getVideoHandler());
        device.startDepth(renderer.getDepthHandler());

        iv = new ImageView(renderer.renderImage());

        Button btn = new Button("Quit");
        btn.setOnMouseClicked(mouseEvent -> shutdown());

        Label lbl = new Label("Just an experiment with the Kinect 1.");

        Scene scene = new Scene(new VBox(btn, lbl, iv));
        stage.setScene(scene);
        stage.show();

        updater = new Thread(() -> {
            while (!quit) {
                try {
                    iv.setImage(renderer.renderImage());
                    Thread.sleep(50);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        updater.start();
    }

    private void shutdown() {
        try {
            quit = true;
            updater.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            device.stopDepth();
            device.stopVideo();
            device.close();
            context.shutdown();
        }

        Platform.exit();
        System.exit(1);
    }

    public static void main(String[] args) throws IOException {
        System.load(args[0]);
        Application.launch(Kinect.class);
    }
}
