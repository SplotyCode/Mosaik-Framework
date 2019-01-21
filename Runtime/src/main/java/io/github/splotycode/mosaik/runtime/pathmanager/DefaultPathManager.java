package io.github.splotycode.mosaik.runtime.pathmanager;

import io.github.splotycode.mosaik.argparser.Parameter;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.application.ApplicationInfo;
import io.github.splotycode.mosaik.util.info.SystemInfo;
import io.github.splotycode.mosaik.util.info.SystemProperties;

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
