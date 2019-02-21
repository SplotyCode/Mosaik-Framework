package io.github.splotycode.mosaik.webapi.handler.handlers;

import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.content.file.FileResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.ManipulateableContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@AllArgsConstructor
@Getter
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

    protected ManipulateableContent manipulate(ManipulateableContent content, String rawPath, String path) {
        return content;
    }

    @Override
    public boolean handle(Request request) throws HandleRequestException {
        String rawPath = request.getSimplifiedPath();
        String path = toVarPath(rawPath);

        ManipulateableContent content = new FileResponseContent(basePage);
        content.manipulate().variable(variable, path);
        content = manipulate(content, rawPath, path);

        request.getResponse().setContent(content);
        return false;
    }
}
