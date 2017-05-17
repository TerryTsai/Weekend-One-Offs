package jserve.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

public class Request extends Message {

    private String method;
    private String uri;

    public Request() {
    }

    public Request(String method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    public Request(String version, Map<String, String> headers, String method, String uri) {
        super(version, headers);
        this.method = method;
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void read(InputStream in) throws IOException {
        PushbackInputStream pbInput = new PushbackInputStream(in, 3);
        setMethod(consumeUntil(pbInput, ' '));
        setUri(consumeUntil(pbInput, ' '));
        setVersion(consumeUntil(pbInput, '\r'));
        consumeUntil(pbInput, '\n');

        Map<String, String> headers = new HashMap<>();
        setHeaders(headers);
        while (!checkCRLF(pbInput)) {
            String key = consumeUntil(pbInput, ':').toLowerCase();
            consumeUntil(pbInput, ' ');
            String val = consumeUntil(pbInput, '\r');
            consumeUntil(pbInput, '\n');
            headers.put(key, val);
        }
    }

    private static String consumeUntil(PushbackInputStream pbInput, char c) throws IOException {
        StringBuilder sb = new StringBuilder();
        int read = pbInput.read();
        while (read != -1 && read != c) {
            sb.append((char) read);
            read = pbInput.read();
        }
        return sb.toString();
    }

    private static boolean checkCRLF(PushbackInputStream pbInput) throws IOException {
        int cr = pbInput.read();
        if ((char) cr != '\r') {
            pbInput.unread(cr);
            return false;
        }

        int lf = pbInput.read();
        if ((char) lf != '\n') {
            pbInput.unread(cr);
            pbInput.unread(lf);
            return false;
        }

        return true;
    }
}
