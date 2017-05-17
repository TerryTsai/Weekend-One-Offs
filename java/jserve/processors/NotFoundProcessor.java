package jserve.processors;

import jserve.model.Request;
import jserve.model.Response;
import jserve.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NotFoundProcessor implements Processor {

    @Override
    public void process(Request request, InputStream in, OutputStream out) throws IOException {

        Response response = new Response();
        response.setVersion("HTTP/1.1");
        response.setStatus(404);
        response.setReason("Not Found");
        response.setHeader("content-type", "text/plain");
        response.write(out);

        IOUtils.write("Could not find " + request.getUri(), out);

    }

}
