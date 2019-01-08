package me.david.davidlib.spigotlib.paths;

import me.david.davidlib.runtime.application.ApplicationInfo;
import me.david.davidlib.runtime.pathmanager.PathManager;

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
