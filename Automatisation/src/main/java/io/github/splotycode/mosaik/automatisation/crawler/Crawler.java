package io.github.splotycode.mosaik.automatisation.crawler;

import io.github.splotycode.mosaik.automatisation.crawler.condition.SiteConditions;
import io.github.splotycode.mosaik.automatisation.crawler.provider.FallbackLinkProvider;
import io.github.splotycode.mosaik.automatisation.crawler.site.DefaultSite;
import io.github.splotycode.mosaik.automatisation.crawler.provider.LinkRepository;
import io.github.splotycode.mosaik.automatisation.crawler.site.IndexingSite;
import io.github.splotycode.mosaik.automatisation.crawler.site.Site;
import io.github.splotycode.mosaik.util.collection.PushingStackQueue;
import io.github.splotycode.mosaik.util.datafactory.DataFactoryComponent;
import io.github.splotycode.mosaik.util.datafactory.DataKey;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Main Interface for crawler
 * Lets you create costom crawlers
 */
public interface Crawler extends DataFactoryComponent {

    DataKey<Predicate<Site>> SITE_CONDITION = new DataKey<>("crawler.site_condition");
    DataKey<ExecutorService> EXECUTOR_SERVICE = new DataKey<>("crawler.executor_service");
    DataKey<LinkRepository> LINK_REPOSITORY = new DataKey<>("crawler.link_repository");
    DataKey<FallbackLinkProvider> FALLBACK_LINK_PROVIDER = new DataKey<>("crawler.fallback_provider");
    DataKey<Predicate<IndexingSite>> SITE_EXECUTOR = new DataKey<>("crawler.site_executor");

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
     * @see SiteConditions
     */
    default Predicate<Site> getSiteCondition() {
        return getDataFactory().getDataDefault(SITE_CONDITION);
    }

    /**
     * Sets the condition for sites
     * @param site the condition
     * @see SiteConditions
     */
    default void setSiteCondition(Predicate<Site> site) {
        getDataFactory().putData(SITE_CONDITION, site);
    }

    /**
     * ExecutorService to process sites
     * @return the used ExecutorService
     */
    default ExecutorService getExecutorService() {
        return getDataFactory().getDataDefault(EXECUTOR_SERVICE);
    }

    /**
     * Returns LinkRepository
     * @see LinkRepository
     * @return the LinkRepository
     */
    default LinkRepository getLinkRepository() {
        return getDataFactory().getDataDefault(LINK_REPOSITORY);
    }

    /**
     * Sets the  LinkRepository
     * @see LinkRepository
     * @param repository  the LinkRepository
     */
    default void setinkRepository(LinkRepository repository) {
        getDataFactory().putData(LINK_REPOSITORY, repository);
    }

    /**
     * Called on site processing
     * @return the site processor
     */
    default Predicate<IndexingSite> getSiteExecutor() {
        return getDataFactory().getDataDefault(SITE_EXECUTOR);
    }

    /**
     * Sets the processor Called on site processing
     * @param processor the site processor
     */
    default void getSiteExecutor(Predicate<IndexingSite> processor) {
        getDataFactory().putData(SITE_EXECUTOR, processor);
    }

    /**
     * Used when there is no link in the LinkRepository
     * @return the used FallbackLinkProvider
     */
    default FallbackLinkProvider getFallbackProvider() {
        return getDataFactory().getDataDefault(FALLBACK_LINK_PROVIDER);
    }

    /**
     * Sets the FallbackLinkProvider Used when there is no link in the LinkRepository
     * @param fallbackProvider the used FallbackLinkProvider
     */
    default void getFallbackProvider(FallbackLinkProvider fallbackProvider) {
        getDataFactory().putData(FALLBACK_LINK_PROVIDER, fallbackProvider);
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
            if (getSiteCondition().test(site)) {
                final Site finalSite = site;
                return () -> getSiteExecutor().test(new IndexingSite(finalSite.getUrl(), this));
            }
            return null;
        }));
        getDataFactory().putData(EXECUTOR_SERVICE, service);
    }

}
