package me.david.automatisation.crawler;

import me.david.automatisation.crawler.provider.FallbackLinkProvider;
import me.david.automatisation.crawler.provider.LinkRepository;
import me.david.automatisation.crawler.site.DefaultSite;
import me.david.automatisation.crawler.site.IndexingSite;
import me.david.automatisation.crawler.site.Site;
import me.david.davidlib.util.collection.PushingStackQueue;
import me.david.davidlib.util.condition.Condition;
import me.david.davidlib.util.condition.Processor;
import me.david.davidlib.util.datafactory.DataFactoryComponent;
import me.david.davidlib.util.datafactory.DataKey;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Main Interface for crawler
 * Lets you create costom crawlers
 */
public interface Crawler extends DataFactoryComponent {

    DataKey<Condition<Site>> SITE_CONDITION = new DataKey<>("crawler.site_condition");
    DataKey<ExecutorService> EXECUTOR_SERVICE = new DataKey<>("crawler.executor_service");
    DataKey<LinkRepository> LINK_REPOSITORY = new DataKey<>("crawler.link_repository");
    DataKey<FallbackLinkProvider> FALLBACK_LINK_PROVIDER = new DataKey<>("crawler.fallback_provider");
    DataKey<Processor<IndexingSite>> SITE_EXECUTOR = new DataKey<>("crawler.site_executor");

    /**
     * Adds an url to the link repository
     * @param url the url to add
     */
    default void addUrl(URL url) {
        getLinkRepository().storeLink(new DefaultSite(url));
    }

    /**
     * Gets the condition for sites
     * @return the condition
     * @see me.david.automatisation.crawler.condition.SiteConditions
     */
    default Condition<Site> getSiteCondition() {
        return getDataFactory().getData(SITE_CONDITION);
    }

    /**
     * ExecutorService to process sites
     * @return the used ExecutorService
     */
    default ExecutorService getExecutorService() {
        return getDataFactory().getData(EXECUTOR_SERVICE);
    }

    /**
     * Returns LinkRepository
     * @see LinkRepository
     * @return the LinkRepository
     */
    default LinkRepository getLinkRepository() {
        return getDataFactory().getData(LINK_REPOSITORY);
    }

    /**
     * Called on site processing
     * @return the site processor
     */
    default Processor<IndexingSite> getSiteExecutor() {
        return getDataFactory().getData(SITE_EXECUTOR);
    }

    /**
     * Used when there is no link in the LinkRepository
     * @return the used FallbackLinkProvider
     */
    default FallbackLinkProvider getFallbackProvider() {
        return getDataFactory().getData(FALLBACK_LINK_PROVIDER);
    }

    /**
     * Starts the crawler
     * @param minThreads minimum amount of threads
     * @param maxThreads maximum amount of threads
     * @param linkBuffer number of links that will be preprocessed from the LinkRepository
     */
    default void start(int minThreads, int maxThreads, int linkBuffer) {
        ThreadPoolExecutor service = new ThreadPoolExecutor(minThreads, maxThreads, 0, TimeUnit.MILLISECONDS, new PushingStackQueue<>(linkBuffer, () -> {
            Site site = getLinkRepository().nextSite();
            if (site == null) site = getFallbackProvider().nextSite();
            if (site == null) return null;
            if (getSiteCondition().check(site)) {
                final Site finalSite = site;
                return () -> getSiteExecutor().process(new IndexingSite(finalSite.getUrl(), this));
            }
            return null;
        }));
        getDataFactory().putData(EXECUTOR_SERVICE, service);
    }

}
