package me.david.davidlib.runtime.pathmanager;

import de.splotycode.davidlib.argparser.Parameter;
import me.david.davidlib.runtime.LinkBase;
import me.david.davidlib.runtime.application.ApplicationInfo;
import me.david.davidlib.util.info.SystemInfo;
import me.david.davidlib.util.info.SystemProperties;

import java.io.File;

public class DefaultPathManager implements PathManager {

    @Parameter(name = "maindir")
    private File mainDirectory = null;

    private File logDirectory;

    public DefaultPathManager() {
        LinkBase.getBootContext().applyArgs("paths", this);
    }

    @Override
    public File getMainDirectory() {
        if (mainDirectory == null) {
            mainDirectory = new File(SystemProperties.getUserHome(), "." + ApplicationInfo.getImplementingName());
        }
        return mainDirectory;
    }

    @Override
    public File getLogDirectory() {
        if (logDirectory == null) {
            if (SystemInfo.isMac) {
                logDirectory = new File(SystemProperties.getUserHome(), "Library/Logs/" + ApplicationInfo.getImplementingName());
            } else {
                logDirectory = new File(getMainDirectory(), "Logs/");
            }
        }
        return logDirectory;
    }
}
