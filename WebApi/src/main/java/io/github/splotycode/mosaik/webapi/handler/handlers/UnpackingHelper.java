package io.github.splotycode.mosaik.webapi.handler.handlers;

import io.github.splotycode.mosaik.runtime.pathmanager.PathManager;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.file.CachedFileResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.file.CachedStaticFileContent;
import io.github.splotycode.mosaik.webapi.response.content.file.FileResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.file.StaticFileContent;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.ManipulateableContent;

import java.io.File;

public class UnpackingHelper {

    public static UnpackingHelper getMosaikUnpacker(File destination) {
        return new UnpackingHelper(destination, "js", false, false);
    }

    public static UnpackingHelper getMosaikUnpacker(String destination) {
        return new UnpackingHelper(destination, "js", false, false);
    }

    private File root;
    private boolean recursive;

    public UnpackingHelper(File destination, String path, boolean recursive, boolean replaceFileVars) {
        this.recursive = recursive;
        root = destination;

        FileUtil.delete(root);
        FileUtil.copyResources(path, root, recursive);

        if (replaceFileVars) {
            HandlerTools.replaceFileVariables(root, recursive);
        }
    }

    public UnpackingHelper(String destination, String path, boolean recursive, boolean replaceFileVars) {
        this(new File(PathManager.getInstance().getMainDirectory(), "web/" + destination), path, recursive, replaceFileVars);
    }

    public UnpackingHelper(String path, boolean recursive, boolean replaceFileVars) {
        this(path, path, recursive, replaceFileVars);
    }

    public File getFile(String name) {
        File file = new File(root, name);
        if (!recursive && !file.getParentFile().equals(root)) {
            throw new IllegalStateException("Can not get " + name + " because recursion is off");
        }
        return new File(root, name);
    }

    public StaticFileSystemHandler staticHandler(String name, boolean recursive) {
        return new StaticFileSystemHandler(getFile(name), name, recursive);
    }

    public StaticFileSystemHandler staticHandler(String name, boolean recursive, boolean allowUpwardTraveling, boolean noError) {
        return new StaticFileSystemHandler(getFile(name), name, recursive, allowUpwardTraveling, noError);
    }

    public ManipulateableContent response(String name) {
        return response(name, true);
    }

    public ManipulateableContent response(String name, boolean localCache) {
        File file = getFile(name);
        return localCache ? new CachedFileResponseContent(file) : new FileResponseContent(file);
    }

    public ResponseContent staticResponse(String name) {
        return staticResponse(name, true);
    }

    public ResponseContent staticResponse(String name, boolean localCache) {
        File file = getFile(name);
        return localCache ? new CachedStaticFileContent(file) : new StaticFileContent(file);
    }

}
