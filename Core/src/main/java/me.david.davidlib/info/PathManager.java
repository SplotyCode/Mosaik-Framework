package me.david.davidlib.info;

import me.david.davidlib.link.LinkBase;
import me.david.davidlib.link.Links;

import java.io.File;

public interface PathManager {

    static PathManager getInstance() {
        return LinkBase.getInstance().getLink(Links.PATH_MANAGER);
    }

    File getMainDirectory();
    File getLogDirectory();

}
