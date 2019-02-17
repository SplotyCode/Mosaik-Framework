package io.github.splotycode.mosaik.webapi.handler.handlers;

import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.content.file.FileResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.ManipulateableContent;

import java.io.File;

public class SinglePageHandler implements HttpHandler {

    private File basePage;
    private String variable;

    @Override
    public boolean valid(Request request) throws HandleRequestException {
        return !request.getSimplifiedPath().contains("/");
    }

    protected String toVarPath(String path) {
        return "/views/" + path + ".html";
    }

    @Override
    public boolean handle(Request request) throws HandleRequestException {
        String path = request.getSimplifiedPath();

        ManipulateableContent content = new FileResponseContent(basePage);
        content.manipulate().variable(variable, toVarPath(path));
        request.getResponse().setContent(content);
        return false;
    }
}
