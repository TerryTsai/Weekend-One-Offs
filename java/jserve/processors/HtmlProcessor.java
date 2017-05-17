package jserve.processors;

import jserve.model.Request;
import jserve.model.Response;
import jserve.utils.IOUtils;

import java.io.*;

public class HtmlProcessor implements Processor {

    private final File path;

    public HtmlProcessor(File path) {
        this.path = path;
    }

    @Override
    public void process(Request request, InputStream in, OutputStream out) throws IOException {
        File file = new File(path, request.getUri());

        Response response = new Response();
        response.setVersion("HTTP/1.1");
        response.setStatus(200);
        response.setReason("OK");
        response.setHeader("content-type", "text/html");
        response.setHeader("content-length", String.valueOf(file.length()));
        response.write(out);

        FileInputStream fis = new FileInputStream(file);
        IOUtils.write(fis, out);
    }

}
