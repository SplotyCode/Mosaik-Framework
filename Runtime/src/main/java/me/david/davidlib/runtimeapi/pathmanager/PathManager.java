package me.david.davidlib.runtimeapi.pathmanager;

import me.david.davidlib.runtimeapi.LinkBase;
import me.david.davidlib.runtimeapi.Links;

import java.io.File;

public interface PathManager {

    static PathManager getInstance() {
        return LinkBase.getInstance().getLink(Links.PATH_MANAGER);
    }

    File getMainDirectory();
    File getLogDirectory();

}
