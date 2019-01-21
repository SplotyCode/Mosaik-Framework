package io.github.splotycode.mosaik.runtime.pathmanager;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;

import java.io.File;

public interface PathManager {

    static PathManager getInstance() {
        return LinkBase.getInstance().getLink(Links.PATH_MANAGER);
    }

    File getMainDirectory();
    File getLogDirectory();

}
