package io.github.splotycode.mosaik.webapi.handler.handlers;

import io.github.splotycode.mosaik.runtime.pathmanager.PathManager;
import io.github.splotycode.mosaik.util.io.FileUtil;

import java.io.File;

public class UnpackingStaticFileSystemHandler extends StaticFileSystemHandler {

    public UnpackingStaticFileSystemHandler(String path, String prefix, boolean recursive, boolean allowUpwardTraveling, boolean replaceFileVars, boolean noError) {
        super(new File(PathManager.getInstance().getMainDirectory(), "web/" + path), prefix, recursive, allowUpwardTraveling, noError);

        FileUtil.delete(root);
        FileUtil.copyResources(path, root, recursive);

        if (replaceFileVars) {
            HandlerTools.replaceFileVariables(root, recursive);
        }
    }

    public UnpackingStaticFileSystemHandler(String path, String prefix) {
        this(path, prefix, true);
    }

    public UnpackingStaticFileSystemHandler(String path, String prefix, boolean recursive) {
        this(path, prefix, recursive, false, false, false);
    }

    public UnpackingStaticFileSystemHandler(String path, String prefix, boolean recursive, boolean replaceFileVars) {
        this(path, prefix, recursive, false, replaceFileVars, false);
    }
}
