package jserve.processors;

import jserve.model.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class LoggingProcessor implements Processor {

    @Override
    public void process(Request request, InputStream in, OutputStream out) throws IOException {
        System.out.println(String.format(
                Locale.ENGLISH,
                "%s %s %s [Message Size: %d]",
                request.getMethod(),
                request.getUri(),
                request.getVersion(),
                request.getHeader("content-length", 0)
        ));
    }

}
