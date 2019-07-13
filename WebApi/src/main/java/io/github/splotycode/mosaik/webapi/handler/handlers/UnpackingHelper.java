package io.github.splotycode.mosaik.webapi.handler.handlers;

import io.github.splotycode.mosaik.runtime.pathmanager.PathManager;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.webapi.response.content.file.FileResponseContent;

import java.io.File;

public class UnpackingHelper {

    private File root;

    public UnpackingHelper(String path, boolean recursive, boolean replaceFileVars) {
        root = new File(PathManager.getInstance().getMainDirectory(), "web/" + path);

        FileUtil.delete(root);
        FileUtil.copyResources(path, root, recursive);

        if (replaceFileVars) {
            HandlerTools.replaceFileVariables(root, recursive);
        }
    }

    public FileResponseContent response(String file) {
        return new FileResponseContent(root, file);
    }

}
