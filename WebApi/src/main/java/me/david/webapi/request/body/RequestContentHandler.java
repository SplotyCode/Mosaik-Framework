package me.david.webapi.request.body;

import me.david.webapi.request.Request;

public interface RequestContentHandler {

    boolean valid(Request request);

    RequestContent create(Request request);

}
