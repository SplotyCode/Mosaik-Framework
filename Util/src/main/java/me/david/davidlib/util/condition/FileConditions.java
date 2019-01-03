package me.david.davidlib.util.condition;

import me.david.davidlib.util.io.FileUtil;

import java.io.File;

public final class FileConditions {

    public Condition<File> ONLY_DIRECTORYS = File::isDirectory;
    public Condition<File> ONLY_EXECUTABLE = FileUtil::canExecute;

}
