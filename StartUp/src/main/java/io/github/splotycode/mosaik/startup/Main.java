package io.github.splotycode.mosaik.startup;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.Runtime;
import io.github.splotycode.mosaik.runtime.logging.LoggingHelper;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.runtime.startup.StartUpConfiguration;
import io.github.splotycode.mosaik.runtime.startup.condition.ConditionSignal;
import io.github.splotycode.mosaik.runtime.startup.condition.StartupConditionRegistry;
import io.github.splotycode.mosaik.startup.manager.StartUpManager;
import io.github.splotycode.mosaik.startup.runtime.MosaikRuntime;
import io.github.splotycode.mosaik.util.init.AlreadyInitailizedException;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.logger.LoggerFactory;

import java.time.Instant;

public class Main {
    private volatile static boolean initialised;

    public static void main() throws Exception {
        mainImpl(new StartUpConfiguration());
    }

    public static void mainIfNotInitialised() throws Exception {
        if (!initialised)
            mainImpl(new StartUpConfiguration());
    }

    public static void mainIfNotInitialised(String[] args) throws Exception {
        if (!initialised)
            mainImpl(new StartUpConfiguration().withArgs(args).withClassLoader(BootContext.createProvider(args)));
    }

    public static void mainIfNotInitialised(StartUpConfiguration configuration) throws Exception {
        if (!initialised)
            mainImpl(configuration);
    }

    private static Logger logger;

    public static void main(String[] args) throws Exception {
        mainImpl(new StartUpConfiguration().withArgs(args).withClassLoader(BootContext.createProvider(args)));
    }

    public static void main(StartUpConfiguration configuration) throws Exception {
        mainImpl(configuration);
    }

    private static synchronized void checkInitialised() {
        if (initialised) throw new AlreadyInitailizedException("Main.main() already called");
        initialised = true;
    }

    private static void mainImpl(StartUpConfiguration configuration) throws Exception {
        Instant start = Instant.now();
        checkInitialised();

        configuration.finish();
        configuration.getBootLoggerFactory().ifPresent(Logger::setFactory);

        BootContext bootCtx = configuration.getBootContext(start);
        LinkBase.getInstance().registerLink(Links.BOOT_DATA, bootCtx);

        StartupConditionRegistry conditionRegistry = StartupConditionRegistry.fromConfiguration(configuration);
        conditionRegistry.testSignal(ConditionSignal.PRE_INIT);

        LinkBase.getInstance().loadLinkBaseFile(configuration.getLinkBasePath());

        setUpLogging(configuration.getRuntimeLoggerFactory());
        LoggingHelper.loggingStartUp();

        conditionRegistry.testSignal(ConditionSignal.POST_INIT);

        logger.info("");

        MosaikRuntime.create(bootCtx, configuration);
        new Main();
    }

    private Main() {
        MosaikRuntime runtime = (MosaikRuntime) Runtime.getRuntime();
        StartUpManager startUpManager = runtime.getStartUpManager();
        runtime.prepareLinkage();

        /* Register StartUp Environment Changer */
        startUpManager.prepairEnvironment();

        /* Fully load global ClassPath */
        runtime.loadClassPath();

        /* Running Startup Tasks*/
        LoggingHelper.doSection("StartUp Tasks", startUpManager::runStartupTasks);

        LoggingHelper.doSection("Environment Information", LoggingHelper::printInfo);

        /* Starting Applications */
        LoggingHelper.doSection("Applications", () -> runtime.getApplicationManager().start());

        startUpManager.finished();
    }

    private static void setUpLogging(Class<? extends LoggerFactory> factory) {
        Logger.setFactory(factory);
        logger = Logger.getInstance(Main.class);

        LoggingHelper.registerShutdownLogging();
    }
}
