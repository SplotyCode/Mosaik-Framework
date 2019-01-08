package me.david.automatisation.crawler.provider;

import me.david.automatisation.crawler.site.Site;

public interface LinkRepository extends FallbackLinkProvider {

    void storeLink(Site site);



}
