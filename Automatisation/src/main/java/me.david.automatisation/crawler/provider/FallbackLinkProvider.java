package me.david.automatisation.crawler.provider;

import me.david.automatisation.crawler.site.Site;

/**
 * Provides new Links
 */
public interface FallbackLinkProvider {

    /**
     * Returns the site that should be processed
     * @return the site
     */
    Site nextSite();

}
