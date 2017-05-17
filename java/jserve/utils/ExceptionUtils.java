package jserve.utils;

import java.io.Closeable;
import java.io.IOException;

public enum ExceptionUtils {

    ;

    public interface IORunnable {
        void run() throws IOException;
    }

    public static void run(IORunnable ioRunnable) {
        try {
            ioRunnable.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void join(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
