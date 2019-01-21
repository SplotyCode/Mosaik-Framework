package io.github.splotycode.mosaik.automatisation.crawler.provider;

import io.github.splotycode.mosaik.automatisation.crawler.site.Site;

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
