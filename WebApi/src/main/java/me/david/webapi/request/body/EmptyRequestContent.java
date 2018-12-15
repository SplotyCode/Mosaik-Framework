package me.david.webapi.request.body;

import me.david.webapi.request.Request;

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
