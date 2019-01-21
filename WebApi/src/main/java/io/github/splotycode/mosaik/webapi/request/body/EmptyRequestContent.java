package io.github.splotycode.mosaik.webapi.request.body;

import io.github.splotycode.mosaik.webapi.request.Request;

public class EmptyRequestContent implements RequestContentHandler, RequestContent {

    @Override
    public boolean valid(Request request) {
        return true;
    }

    @Override
    public RequestContent create(Request request) {
        return new EmptyRequestContent();
    }

}
