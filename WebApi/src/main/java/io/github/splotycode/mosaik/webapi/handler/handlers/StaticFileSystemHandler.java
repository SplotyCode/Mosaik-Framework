package io.github.splotycode.mosaik.webapi.handler.handlers;

import io.github.splotycode.mosaik.annotations.Disabled;
import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.HttpCashingConfiguration;
import io.github.splotycode.mosaik.webapi.response.content.file.StaticFileContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@AllArgsConstructor
@Getter
@Disabled
public class StaticFileSystemHandler implements HttpHandler {

    private File root;
    private String prefix;
    private boolean recursive, allowUpwardTraveling, noError;

    public StaticFileSystemHandler(File root, String prefix) {
        this(root, prefix, true);
    }

    public StaticFileSystemHandler(File root, String prefix, boolean recursive) {
        this(root, prefix, recursive, false, false);
    }

    @Override
    public boolean valid(Request request) throws HandleRequestException {
        return request.getSimplifiedPath().startsWith(prefix) && (allowUpwardTraveling || !request.getSimplifiedPath().contains(".."));
    }

    @Override
    public boolean handle(Request request) throws HandleRequestException {
        String path = request.getSimplifiedPath();
        if (!recursive && (path.contains("/") || path.contains("\\"))) return false;

        File file = new File(root, path.substring(prefix.length()));
        if (!noError || file.exists()) {
            request.getResponse().applyCashingConfiguration(HttpCashingConfiguration.ASSET_CASHING);
            request.getResponse().setContent(new StaticFileContent(file));
        }
        return false;
    }
}
