package me.david.automatisation.crawler.condition;

import me.david.automatisation.crawler.site.Site;

import java.util.function.Predicate;

/**
 * General Conditions for sites
 * @see me.david.davidlib.util.condition.FileConditions
 */
public final class SiteConditions {

    /**
     * Checks if an sits is using https
     */
    Predicate<Site> NEED_HTTPS = item -> item.getUrl().getProtocol().equalsIgnoreCase("https");

}
