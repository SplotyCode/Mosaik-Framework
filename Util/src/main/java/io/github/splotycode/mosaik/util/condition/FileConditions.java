package io.github.splotycode.mosaik.util.condition;

import io.github.splotycode.mosaik.util.io.FileUtil;

import java.io.File;
import java.util.function.Predicate;

public final class FileConditions {

    public Predicate<File> ONLY_DIRECTORYS = File::isDirectory;
    public Predicate<File> ONLY_EXECUTABLE = FileUtil::canExecute;

}
