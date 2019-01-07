package me.david.davidlib.crawler.site;

import java.net.URL;

public interface Site {

    URL getUrl();

    default String getRawUrl() {
        return getUrl().toExternalForm();
    }

}
