package io.github.splotycode.mosaik.automatisation.crawler.condition;

import io.github.splotycode.mosaik.automatisation.crawler.site.Site;
import io.github.splotycode.mosaik.util.condition.FileConditions;

import java.util.function.Predicate;

/**
 * General Conditions for sites
 * @see FileConditions
 */
public final class SiteConditions {

    /**
     * Checks if an sits is using https
     */
    Predicate<Site> NEED_HTTPS = item -> item.getUrl().getProtocol().equalsIgnoreCase("https");

}
