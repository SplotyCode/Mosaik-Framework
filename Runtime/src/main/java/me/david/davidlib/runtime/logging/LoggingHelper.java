package me.david.davidlib.runtime.logging;

import me.david.davidlib.runtime.application.Application;
import me.david.davidlib.runtime.application.ApplicationInfo;
import me.david.davidlib.util.StringUtil;
import me.david.davidlib.util.info.EnvironmentInformation;
import me.david.davidlib.util.info.SystemInfo;
import me.david.davidlib.util.logger.Logger;
import me.david.davidlib.util.reflection.ClassFinderHelper;

import java.util.Collections;

public final class LoggingHelper {

    private static String currentSection;

    private static Logger logger = Logger.getInstance(LoggingHelper.class);

    public static void startSection(String section) {
        currentSection = section;
        printSection("Starting " + section);
    }

    public static void endSection() {
        printSection("Finished " + currentSection);
    }

    private static void printSection(String section) {
        int length = (45 - section.length() - 2) / 2;
        String prefix = length <= 0 ? "" : StringUtil.join(Collections.nCopies(length, "-"), "");
        logger.info(prefix + "[" + section + "]" + prefix);
    }

    private static void printImportantSection(String name) {
        String prefix = StringUtil.join(Collections.nCopies(45, "-"), "");
        logger.info(prefix);
        printSection(name);
        logger.info(prefix);
    }

    public static void loggingStartUp() {
        printImportantSection("Api Start");
    }

    public static void registerShutdownLogging() {
        Application.getGlobalShutdownManager().addFirstShutdownTask(() -> printImportantSection("Api Shutdown Start"));
        Application.getGlobalShutdownManager().addShutdownTask(() -> printImportantSection("Api Shutdown Start"));
    }

    public static void printInfo() {
        logger.info(ApplicationInfo.getApplicationInfo());

        logger.info("Java (JDK): " + EnvironmentInformation.getJDKInfo());
        logger.info("Java (JRE): " + EnvironmentInformation.getJREInfo());
        logger.info("Jvm: " + EnvironmentInformation.getJVMInfo());
        logger.info("JVM-Args: " + EnvironmentInformation.getJVMArgs());
        logger.info("OS: " + SystemInfo.getOsNameVersionAndArch());
        logger.info("ClassPath: " + ClassFinderHelper.getUserClasses().size() + "/" + ClassFinderHelper.getTotalClassCount());
    }

}
