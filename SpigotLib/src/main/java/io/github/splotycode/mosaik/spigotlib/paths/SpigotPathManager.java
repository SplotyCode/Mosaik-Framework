package io.github.splotycode.mosaik.spigotlib.paths;

import io.github.splotycode.mosaik.runtime.application.ApplicationInfo;
import io.github.splotycode.mosaik.runtime.pathmanager.PathManager;

import java.io.File;

public class SpigotPathManager implements PathManager {

    private File mainDir, logDir;


    @Override
    public File getMainDirectory() {
        if (mainDir == null) {
            mainDir = new File("plugins/" + ApplicationInfo.getImplementingName() + "/");
        }
        return mainDir;
    }

    @Override
    public File getLogDirectory() {
        if (logDir == null) {
            logDir = new File(getMainDirectory(), "Logs/");
        }
        return logDir;
    }

}
