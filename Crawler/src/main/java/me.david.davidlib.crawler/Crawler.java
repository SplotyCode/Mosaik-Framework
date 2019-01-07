package me.david.davidlib.crawler;

import me.david.davidlib.crawler.provider.FallbackLinkProvider;
import me.david.davidlib.crawler.site.DefaultSite;
import me.david.davidlib.crawler.site.IndexingSite;
import me.david.davidlib.crawler.provider.LinkRepository;
import me.david.davidlib.crawler.site.Site;
import me.david.davidlib.util.collection.PushingStackQueue;
import me.david.davidlib.util.condition.Condition;
import me.david.davidlib.util.condition.Processor;
import me.david.davidlib.util.datafactory.DataFactoryComponent;
import me.david.davidlib.util.datafactory.DataKey;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public interface Crawler extends DataFactoryComponent {

    DataKey<Condition<Site>> SITE_CONDITION = new DataKey<>("crawler.site_condition");
    DataKey<ExecutorService> EXECUTOR_SERVICE = new DataKey<>("crawler.executor_service");
    DataKey<LinkRepository> LINK_REPOSITORY = new DataKey<>("crawler.link_repository");
    DataKey<FallbackLinkProvider> FALLBACK_LINK_PROVIDER = new DataKey<>("crawler.fallback_provider");
    DataKey<Processor<IndexingSite>> SITE_EXECUTOR = new DataKey<>("crawler.site_executor");

    default void addUrl(URL url) {
        getLinkRepository().storeLink(new DefaultSite(url));
    }

    default Condition<Site> getSiteCondition() {
        return getDataFactory().getData(SITE_CONDITION);
    }

    default ExecutorService getExecutorService() {
        return getDataFactory().getData(EXECUTOR_SERVICE);
    }

    default LinkRepository getLinkRepository() {
        return getDataFactory().getData(LINK_REPOSITORY);
    }

    default Processor<IndexingSite> getSiteExecutor() {
        return getDataFactory().getData(SITE_EXECUTOR);
    }

    default FallbackLinkProvider getFallbackProvider() {
        return getDataFactory().getData(FALLBACK_LINK_PROVIDER);
    }

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
