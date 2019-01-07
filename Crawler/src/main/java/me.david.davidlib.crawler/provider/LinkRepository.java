package me.david.davidlib.crawler.provider;

import me.david.davidlib.crawler.site.Site;

public interface LinkRepository extends FallbackLinkProvider {

    void storeLink(Site site);



}
