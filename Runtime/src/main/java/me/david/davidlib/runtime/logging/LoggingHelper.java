package me.david.davidlib.runtime.logging;

import me.david.davidlib.runtime.application.Application;
import me.david.davidlib.runtime.application.ApplicationInfo;
import me.david.davidlib.util.info.EnvironmentInformation;
import me.david.davidlib.util.info.SystemInfo;
import me.david.davidlib.util.logger.Logger;

public final class LoggingHelper {

    private static Logger logger = Logger.getInstance(LoggingHelper.class);

    public static void loggingStartUp() {
        logger.info("------------------------------");
        logger.info("-----[Api Start]--------------");
        logger.info("------------------------------");
    }

    public static void registerShutdownLogging() {
        Application.getGlobalShutdownManager().addFirstShutdownTask(() -> {
            logger.info("------------------------------");
            logger.info("-----[Api Shutdown Start]-----");
            logger.info("------------------------------");
        });
        Application.getGlobalShutdownManager().addShutdownTask(() -> {
            logger.info("---------------------------------");
            logger.info("-----[Api Shutdown Finished]-----");
            logger.info("---------------------------------");
        });
    }

    public static void printInfo() {
        logger.info(ApplicationInfo.getApplicationInfo());

        logger.info("Java (JDK): " + EnvironmentInformation.getJDKInfo());
        logger.info("Java (JRE): " + EnvironmentInformation.getJREInfo());
        logger.info("Jvm: " + EnvironmentInformation.getJVMInfo());
        logger.info("JVM-Args: " + EnvironmentInformation.getJVMArgs());
        logger.info("OS: " + SystemInfo.getOsNameVersionAndArch());
    }

}
