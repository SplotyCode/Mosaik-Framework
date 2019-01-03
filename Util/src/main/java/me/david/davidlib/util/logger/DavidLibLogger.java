package me.david.davidlib.util.logger;

import me.david.davidlib.util.StringUtil;
import me.david.davidlib.util.info.ApplicationInfo;
import me.david.davidlib.util.info.EnvironmentInformation;
import me.david.davidlib.util.info.SystemInfo;
import org.apache.log4j.Logger;

public class DavidLibLogger extends Log4JLogger {

    private static Exception firstException = null;

    public DavidLibLogger(Logger logger4J) {
        super(logger4J);
    }

    @Override
    public void error(String message, Throwable t, String... details) {
        String detailString = StringUtil.join(details, "\n");

        if (!detailString.isEmpty()) {
            detailString = "\nDetails: " + detailString;
        }

        if (firstException == null) {
            String mess = "Logger errors occurred. See IDEA logs for details. " +
                    (StringUtil.isEmpty(message) ? "" : "Error message is '" + message + "'");
            firstException = new Exception(mess + detailString, t);
        }
        logger4J.error(message + detailString, t);
        logErrorHeader();
    }

    private void logErrorHeader() {
        logger4J.error(ApplicationInfo.getApplicationInfo());

        logger4J.error("Java (JDK): " + EnvironmentInformation.getJDKInfo());
        logger4J.error("Java (JRE): " + EnvironmentInformation.getJREInfo());
        logger4J.error("Jvm: " + EnvironmentInformation.getJVMInfo());
        logger4J.error("OS: " + SystemInfo.getOsNameVersionAndArch());
    }

}
