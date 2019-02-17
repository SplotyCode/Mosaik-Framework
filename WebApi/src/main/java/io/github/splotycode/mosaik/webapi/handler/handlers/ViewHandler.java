package io.github.splotycode.mosaik.webapi.handler.handlers;

import io.github.splotycode.mosaik.annotations.Disabled;
import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.file.CachedFileResponseContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@Disabled
@AllArgsConstructor
@Getter
public class ViewHandler implements HttpHandler  {

    private File base;

    protected String toFileName(String path) {
        return path.toLowerCase() + ".html";
    }

    protected ResponseContent buildContent(File file, String path) {
        return new CachedFileResponseContent(file);
    }

    protected File findFile(String fileName) {
        return new File(base, fileName);
    }

    @Override
    public boolean valid(Request request) throws HandleRequestException {
        return !request.getSimplifiedPath().contains("/");
    }

    @Override
    public boolean handle(Request request) throws HandleRequestException {
        File file = findFile(toFileName(request.getSimplifiedPath()));
        request.getResponse().setContent(buildContent(file, request.getSimplifiedPath()));
        return false;
    }
}
