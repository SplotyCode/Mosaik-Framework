package io.github.splotycode.mosaik.automatisation.crawler.provider;

import io.github.splotycode.mosaik.automatisation.crawler.site.Site;

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
