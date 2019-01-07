package me.david.davidlib.crawler.condition;

import me.david.davidlib.crawler.site.Site;
import me.david.davidlib.util.condition.Condition;

public final class SiteConditions {

    Condition<Site> NEED_HTTPS = item -> item.getUrl().getProtocol().equalsIgnoreCase("https");

}
