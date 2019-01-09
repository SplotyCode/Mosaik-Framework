package me.david.automatisation.crawler.provider;

import me.david.automatisation.crawler.site.Site;

/**
 * Returns and stores sites that stills need to be processed
 */
public interface LinkRepository extends FallbackLinkProvider {

    /**
     * Stores a new site
     * @param site the site you want to store
     */
    void storeLink(Site site);



}
