package jserve.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class Response extends Message {

    private Integer status;
    private String reason;

    public Response() {
    }

    public Response(Integer status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public Response(String version, Integer status, String reason, Map<String, String> headers) {
        super(version, headers);
        this.status = status;
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void write(OutputStream out) throws IOException {
        out.write(version.getBytes());
        out.write(' ');
        out.write(status.toString().getBytes());
        out.write(' ');
        out.write(reason.getBytes());
        out.write('\r');
        out.write('\n');

        for (Map.Entry<String, String> header : headers.entrySet()) {
            out.write(header.getKey().getBytes());
            out.write(':');
            out.write(' ');
            out.write(header.getValue().getBytes());
            out.write('\r');
            out.write('\n');
        }

        out.write('\r');
        out.write('\n');
    }

}
