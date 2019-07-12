package io.github.splotycode.mosaik.startup;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.application.IApplication;
import io.github.splotycode.mosaik.runtime.logging.LoggingHelper;
import io.github.splotycode.mosaik.runtime.logging.MosaikLoggerFactory;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.runtime.startup.StartUpConfiguration;
import io.github.splotycode.mosaik.runtime.startup.environment.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.startup.application.ApplicationManager;
import io.github.splotycode.mosaik.startup.envirementchanger.StartUpInvirementChangerImpl;
import io.github.splotycode.mosaik.startup.exception.FrameworkStartException;
import io.github.splotycode.mosaik.startup.manager.StartUpManager;
import io.github.splotycode.mosaik.startup.processbar.StartUpProcessHandler;
import io.github.splotycode.mosaik.startup.starttask.StartTaskExecutor;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.init.AlreadyInitailizedException;
import io.github.splotycode.mosaik.util.io.IOUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.logger.LoggerFactory;
import io.github.splotycode.mosaik.util.reflection.ClassFinderHelper;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import io.github.splotycode.mosaik.util.reflection.modules.MosaikModule;
import lombok.Getter;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

import java.io.IOException;

public class Main {

    @Getter private static Main instance;

    @Getter private static BootContext bootData;

    @Getter private static boolean initialised = false;

    public static void main() throws Exception {
        mainImpl(new StartUpConfiguration());
    }

    public static void mainIfNotInitialised() throws Exception {
        if (!initialised)
            mainImpl(new StartUpConfiguration());
    }

    public static void mainIfNotInitialised(String[] args) throws Exception {
        if (!initialised)
            mainImpl(new StartUpConfiguration().withArgs(args));
    }

    public static void mainIfNotInitialised(StartUpConfiguration configuration) throws Exception {
        if (!initialised)
            mainImpl(configuration);
    }

    private static Logger logger;

    public static void main(String[] args) throws Exception {
        mainImpl(new StartUpConfiguration().withArgs(args));
    }

    public static void main(StartUpConfiguration configuration) throws Exception {
        mainImpl(configuration);
    }

    private static void mainImpl(StartUpConfiguration configuration) throws Exception {
        long start = System.currentTimeMillis();
        if (initialised) throw new AlreadyInitailizedException("Main.main() already called");
        initialised = true;

        configuration.finish();
        if (configuration.hasBootLoggerFactory()) {
            Logger.setFactory(configuration.getBootLoggerFactory());
        }

        bootData = configuration.getBootContext(start);
        LinkBase.getInstance().registerLink(Links.BOOT_DATA, bootData);

        MosaikModule.STARTUP.checkLoaded();
        MosaikModule.DOM_PARSING_IMPL.checkLoaded();
        MosaikModule.ARG_PARSER_IMPL.checkLoaded();

        loadLinkBase();
        setUpLogging(configuration.getBootLoggerFactory());
        LoggingHelper.loggingStartUp();

        checkClassLoader();
        if (ReflectionUtil.getCallerClasses().length >= 4 + 1) {
            logger.warn("Framework was not invoked by JVM! It was invoked by: " + ReflectionUtil.getCallerClass(1).getName());
        }

        logger.info("");

        instance = new Main();
    }

    private static void loadLinkBase() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String[] lines = IOUtil.resourceToText("/linkbase.txt").split("\n");
        for (String line : lines) {
            String[] lineSplit = line.split(": ");
            LinkBase.getInstance().getLinkFactory().putData(lineSplit[0], null, Class.forName(lineSplit[1]).newInstance());
        }
    }

    private Main() {
        ApplicationManager applicationManager = (ApplicationManager) LinkBase.getInstance().getLink(Links.APPLICATION_MANAGER);
        StartUpManager startUpManager = (StartUpManager) LinkBase.getInstance().getLink(Links.STARTUP_MANAGER);

        /* Register StartUp Environment Changer */
        StartUpEnvironmentChanger environmentChanger = new StartUpInvirementChangerImpl();
        LinkBase.getInstance().registerLink(Links.STARTUP_ENVIRONMENT_CHANGER, environmentChanger);

        /* Running Startup Tasks*/
        LoggingHelper.startSection("StartUp Tasks");
        ClassLoader classLoader = bootData.getClassLoaderProvider().getClassLoader();
        ClassFinderHelper.setClassLoader(classLoader);
        StartTaskExecutor.getInstance().collectSkippedPaths(classLoader);
        StartTaskExecutor.getInstance().findAll(false);
        StartTaskExecutor.getInstance().runAll(environmentChanger);
        LoggingHelper.endSection();

        LoggingHelper.startSection("Environment Information");
        LoggingHelper.printInfo();
        LoggingHelper.endSection();

        /* Starting Applications */
        LoggingHelper.startSection("Applications");
        applicationManager.startUp();
        StartUpProcessHandler.getInstance().end();
        logger.info("Started " + applicationManager.getLoadedApplicationsCount() + " Applications: " + StringUtil.join(applicationManager.getLoadedApplications(), IApplication::getName, ", "));
        LoggingHelper.endSection();
        startUpManager.finished();
    }

    private static void setUpLogging(Class<? extends LoggerFactory> factory) {
        Logger.setFactory(factory);
        logger = Logger.getInstance(Main.class);

        LoggingHelper.registerShutdownLogging();
    }

    private static void checkClassLoader() {
        ClassLoader systemLoader = ClassLoader.getSystemClassLoader();
        ClassLoader threadLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader thisLoader = Main.class.getClassLoader();

        if (thisLoader.getClass() != threadLoader.getClass() || thisLoader.getClass() != systemLoader.getClass()) {
            logger.warn(StringUtil.format("Invalid ClassLoader! ThisLoader: '{1}', SystemLoader: '{2}', ThisLoader: '{3}'",
                    className(thisLoader),
                    className(threadLoader),
                    className(systemLoader)));
        }
    }

    private static String className(Object obj) {
        return obj == null ? "Null" : obj.getClass().getName();
    }

}
