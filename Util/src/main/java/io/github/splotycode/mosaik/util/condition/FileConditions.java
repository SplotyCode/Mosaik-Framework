package io.github.splotycode.mosaik.util.condition;

import io.github.splotycode.mosaik.util.io.FileUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileConditions {

    public Predicate<File> ONLY_DIRECTORYS = File::isDirectory;
    public Predicate<File> ONLY_EXECUTABLE = FileUtil::canExecute;

}
