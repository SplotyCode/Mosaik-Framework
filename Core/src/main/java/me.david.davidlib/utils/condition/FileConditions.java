package me.david.davidlib.utils.condition;

import me.david.davidlib.utils.io.FileUtil;

import java.io.File;

public final class FileConditions {

    public Condition<File> ONLY_DIRECTORYS = File::isDirectory;
    public Condition<File> ONLY_EXECUTABLE = FileUtil::canExecute;

}
