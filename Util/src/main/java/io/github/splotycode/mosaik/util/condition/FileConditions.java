package io.github.splotycode.mosaik.util.condition;

import io.github.splotycode.mosaik.util.io.FileUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.function.Predicate;

/**
 * Useful file Conditions
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileConditions {

    /**
     * A Predicate that returns true if the specific file is a directory
     */
    public Predicate<File> ONLY_DIRECTORYS = File::isDirectory;

    /**
     * A Predicate that returns true if the specific file is executable
     */
    public Predicate<File> ONLY_EXECUTABLE = FileUtil::canExecute;

}
