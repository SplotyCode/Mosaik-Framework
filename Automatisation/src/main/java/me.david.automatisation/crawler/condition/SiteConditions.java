package me.david.automatisation.crawler.condition;

import me.david.automatisation.crawler.site.Site;
import me.david.davidlib.util.condition.Condition;

/**
 * General Conditions for sites
 * @see me.david.davidlib.util.condition.FileConditions
 */
public final class SiteConditions {

    /**
     * Checks if an sits is using https
     */
    Condition<Site> NEED_HTTPS = item -> item.getUrl().getProtocol().equalsIgnoreCase("https");

}
