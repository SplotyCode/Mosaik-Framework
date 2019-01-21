package me.david.webapi.request.body;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.david.webapi.request.Request;
import me.david.webapi.request.RequestHeaders;

/* ClassRegister needs a No Args Constructor */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PostRequestContent implements RequestContent, RequestContentHandler {

    private PostRequestContent(Request request) {
        QueryStringDecoder uri = new QueryStringDecoder("?" + new String(request.getBody()));
        request.setPost(uri.parameters());
    }

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
