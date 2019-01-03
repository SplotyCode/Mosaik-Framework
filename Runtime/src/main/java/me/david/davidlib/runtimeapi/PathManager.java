package me.david.davidlib.runtimeapi;

import java.io.File;

public interface PathManager {

    static PathManager getInstance() {
        return LinkBase.getInstance().getLink(Links.PATH_MANAGER);
    }

    File getMainDirectory();
    File getLogDirectory();

}
