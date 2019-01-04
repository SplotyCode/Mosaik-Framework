package me.david.davidlib.runtime.pathmanager;

import me.david.davidlib.runtime.LinkBase;
import me.david.davidlib.runtime.Links;

import java.io.File;

public interface PathManager {

    static PathManager getInstance() {
        return LinkBase.getInstance().getLink(Links.PATH_MANAGER);
    }

    File getMainDirectory();
    File getLogDirectory();

}
