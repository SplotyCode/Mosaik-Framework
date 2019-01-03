package de.splotycode.davidlib.startup;

import de.splotycode.davidlib.startup.application.ApplicationManager;
import de.splotycode.davidlib.startup.envirementchanger.StartUpInvirementChangerImpl;
import de.splotycode.davidlib.startup.manager.StartUpManager;
import de.splotycode.davidlib.startup.processbar.StartUpProcessHandler;
import de.splotycode.davidlib.startup.starttask.StartTaskExecutor;
import lombok.Getter;
import me.david.davidlib.util.core.application.Application;
import me.david.davidlib.util.core.application.IApplication;
import me.david.davidlib.util.info.ApplicationInfo;
import me.david.davidlib.util.info.EnvironmentInformation;
import me.david.davidlib.util.info.SystemInfo;
import me.david.davidlib.util.core.link.LinkBase;
import me.david.davidlib.util.core.link.Links;
import me.david.davidlib.util.logger.DavidLibLoggerFactory;
import me.david.davidlib.util.logger.Logger;
import me.david.davidlib.util.core.startup.BootContext;
import me.david.davidlib.util.core.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.util.array.ArrayUtil;
import me.david.davidlib.util.init.AlreadyInitailizedException;
import me.david.davidlib.util.reflection.ReflectionUtil;
import me.david.davidlib.utils.StringUtil;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

@Getter
public class Main {

    @Getter private static Main instance;

    private static BootContext bootData;

    @Getter private static boolean initialised = false;

    public static void main() {
        main(ArrayUtil.EMPTY_STRING_ARRAY);
    }

    public static void mainIfNotInitialised() {
        if (!initialised)
            main();
    }

    public static void mainIfNotInitialised(String[] args) {
        if (!initialised)
            main(args);
    }

    private static Logger logger;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        if (initialised) throw new AlreadyInitailizedException("Main.main() already called");
        initialised = true;

        setUpLogging();
        logger.info("Starting FrameWork!");

        if (ReflectionUtil.getCallerClasses().length >= 4) {
            logger.warn("Framework was not invoked by JVM! It was invoked by: " + ReflectionUtil.getCallerClass().getName());
        }
        logger.info("");

        bootData = new BootContext(args, start);
        instance = new Main();
    }

    private Main() {
        ApplicationManager applicationManager = new ApplicationManager();

        /* Register Links */
        LinkBase.getInstance().registerLink(Links.BOOT_DATA, bootData);
        LinkBase.getInstance().registerLink(Links.APPLICATION_MANAGER, new ApplicationManager());
        LinkBase.getInstance().registerLink(Links.STARTUP_MANAGER, new StartUpManager());

        /* Register StartUp Environment Changer */
        StartUpEnvironmentChanger environmentChanger = new StartUpInvirementChangerImpl();
        LinkBase.getInstance().registerLink(Links.STARTUP_ENVIRONMENT_CHANGER, environmentChanger);

        /* Running Startup Tasks*/
        StartTaskExecutor.getInstance().collectSkippedPaths();
        StartTaskExecutor.getInstance().findAll(false);
        StartTaskExecutor.getInstance().runAll(environmentChanger);

        printInfo();

        /* Starting Applications */
        applicationManager.startUp();

        StartUpProcessHandler.getInstance().end();

        logger.info("Started " + applicationManager.getLoadedApplicationsCount() + " Applications: " + StringUtil.join(applicationManager.getLoadedApplications(), IApplication::getName, ", "));
    }

    private static void setUpLogging() {
        Logger.setFactory(DavidLibLoggerFactory.class);
        System.setProperty("log4j.defaultInitOverride", "true");
        try {
            org.apache.log4j.Logger root = org.apache.log4j.Logger.getRootLogger();
            if (!root.getAllAppenders().hasMoreElements()) {
                root.setLevel(Level.WARN);
                root.addAppender(new ConsoleAppender(new PatternLayout(PatternLayout.DEFAULT_CONVERSION_PATTERN)));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        logger = Logger.getInstance(Main.class);
    }

    private static void printInfo() {
        Application.getGlobalShutdownManager().addShutdownTask(() -> {
            logger.info("-----[Api Shutdown]-----");
        });
        logger.info("-----[Api Start]-----");
        logger.info(ApplicationInfo.getApplicationInfo());

        logger.info("Java (JDK): " + EnvironmentInformation.getJDKInfo());
        logger.info("Java (JRE): " + EnvironmentInformation.getJREInfo());
        logger.info("Jvm: " + EnvironmentInformation.getJVMInfo());
        logger.info("JVM-Args: " + EnvironmentInformation.getJVMArgs());
        logger.info("OS: " + SystemInfo.getOsNameVersionAndArch());
    }

}
