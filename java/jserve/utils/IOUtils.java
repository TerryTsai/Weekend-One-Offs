package jserve.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public enum IOUtils {;

    public static void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
            out.write(buffer, 0, read);
    }

    public static void write(String string, OutputStream out) throws IOException {
        out.write(string.getBytes());
    }
}
