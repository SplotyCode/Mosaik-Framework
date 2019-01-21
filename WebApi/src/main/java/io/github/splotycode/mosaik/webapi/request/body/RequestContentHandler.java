package io.github.splotycode.mosaik.webapi.request.body;

import io.github.splotycode.mosaik.webapi.request.Request;

public interface RequestContentHandler {

    boolean valid(Request request);

    RequestContent create(Request request);

}
