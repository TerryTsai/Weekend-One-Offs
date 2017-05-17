package jserve.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Message {

    protected String version;
    protected Map<String, String> headers = new HashMap<>();

    public Message() {}

    public Message(String version, Map<String, String> headers) {
        this.version = version;
        this.headers = headers;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public int getHeader(String header, int otherwise) {
        try {
            String result = headers.get(header);
            return (result != null && !result.isEmpty())
                    ? Integer.parseInt(result)
                    : otherwise;
        } catch (NumberFormatException e) {
            return otherwise;
        }
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setHeader(String header, String value) {
        headers.put(header, value);
    }

}
