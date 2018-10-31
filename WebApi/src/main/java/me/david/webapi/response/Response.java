package me.david.webapi.response;

import me.david.webapi.response.content.ResponseContent;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private HttpVersion httpVersion;
    private Map<String, String> headers = new HashMap<>();
    private ResponseContent content;

    public Response(ResponseContent content) {
        this.content = content;
    }

    public void setHeader(HttpHeaders httpHeader, String value) {
        headers.put(httpHeader.name().toLowerCase().replace('_', '-'), value);
    }

    public void setHeader(String httpHeader, String value) {
        headers.put(httpHeader, value);
    }

}
