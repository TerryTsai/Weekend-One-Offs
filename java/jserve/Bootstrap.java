package jserve;

import jserve.core.JServe;
import jserve.processors.HtmlProcessor;
import jserve.processors.LoggingProcessor;
import jserve.processors.NotFoundProcessor;

import java.io.File;
import java.io.IOException;

public class Bootstrap {

    public static void main(String[] args) throws IOException {
        JServe jServe = new JServe();

        // Add Processors For All Requests
        jServe.addPreprocessor(new LoggingProcessor());

        // Add Processors Per Regex Route
        jServe.addEndpoint(".*\\.html", new HtmlProcessor(new File("./www/html")));
        jServe.addEndpoint(".*", new NotFoundProcessor());

        jServe.run();
    }

}
