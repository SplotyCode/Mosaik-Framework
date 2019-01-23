package io.github.splotycode.mosaik.domparsing.annotation;

import io.github.splotycode.mosaik.util.io.FileUtil;

import java.io.File;

public class FileSystem<D extends DomResolver> {

    private File root;
    private Class<D> clazz;

    public FileSystem(File root, Class<D> clazz) {
        FileUtil.createDirectory(root);
        this.root = root;
        this.clazz = clazz;
    }

    public D
}
