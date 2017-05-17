package jserve.processors;

import jserve.model.Request;
import jserve.model.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface Processor {

    void process(Request request, InputStream in, OutputStream out) throws IOException;

}
