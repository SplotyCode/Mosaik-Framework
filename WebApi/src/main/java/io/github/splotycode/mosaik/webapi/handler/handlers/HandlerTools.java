package io.github.splotycode.mosaik.webapi.handler.handlers;

import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.StringManipulator;

import java.io.File;

public final class HandlerTools {

    private static final String FILE_PREFIX = "file:";

    public static void replaceFileVariables(File file, boolean recursive) {
        FileUtil.listFiles(file, recursive, child -> {
            StringManipulator manipulator = new StringManipulator(FileUtil.loadFile(child));
            boolean changed = false;
            for (String var : manipulator.getManipulateData().getVariableMap().keySet()) {
                if (var.startsWith(FILE_PREFIX)) {
                    changed = true;
                    String path = var.substring(FILE_PREFIX.length());
                    manipulator.variable(var, FileUtil.loadFile(new File(file, path)));
                }
            }
            if (changed) {
                FileUtil.writeToFile(child, manipulator.getResult());
            }
        });
    }

}
