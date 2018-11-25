package me.david.webapi.request;

public interface RequestContentHandler {

    boolean valid(Request request);

    RequestContent create(Request request);

}
