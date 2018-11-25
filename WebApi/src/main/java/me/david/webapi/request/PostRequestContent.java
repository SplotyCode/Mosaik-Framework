package me.david.webapi.request;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.QueryStringDecoder;

public class PostRequestContent implements RequestContent {

    private PostRequestContent(Request request) {
        QueryStringDecoder uri = new QueryStringDecoder("?" + new String(request.getBody()));
        request.setPost(uri.parameters());
    }

    public static class Handler implements RequestContentHandler {

        @Override
        public boolean valid(Request request) {
            String contentType = request.getHeader(RequestHeaders.CONTENT_TYPE);
            return contentType != null && HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.contentEqualsIgnoreCase(contentType);
        }

        @Override
        public RequestContent create(Request request) {
            return new PostRequestContent(request);
        }
    }

}
