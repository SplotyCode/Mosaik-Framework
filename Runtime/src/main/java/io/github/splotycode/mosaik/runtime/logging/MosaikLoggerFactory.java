package io.github.splotycode.mosaik.runtime.logging;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.debug.DebugMode;
import io.github.splotycode.mosaik.runtime.debug.DebugProvider;
import io.github.splotycode.mosaik.runtime.startup.BootException;
import io.github.splotycode.mosaik.util.init.InitialisedOnce;
import io.github.splotycode.mosaik.util.io.IOUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.logger.LoggerFactory;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.DOMConfigurator;

import java.nio.charset.StandardCharsets;

public class MosaikLoggerFactory extends InitialisedOnce implements LoggerFactory {

    {
        System.setProperty("log4j.defaultInitOverride", "true");
        try {
            org.apache.log4j.Logger root = org.apache.log4j.Logger.getRootLogger();
            if (!root.getAllAppenders().hasMoreElements()) {
                root.setLevel(Level.WARN);
                root.addAppender(new ConsoleAppender(new PatternLayout(PatternLayout.DEFAULT_CONVERSION_PATTERN)));
            }
        } catch (Throwable e) {
            throw new BootException("Could not initialize log4j logging", e);
        }
    }

    @Override
    public Logger getLoggerInstance(String name) {
        initalizeIfNotAlready();
        return new MosaikLogger(org.apache.log4j.Logger.getLogger(name));
    }

    @Override
    protected void init() {
        System.setProperty("log4j.defaultInitOverride", "true");
        String config = IOUtil.resourceToText("/log-config.xml");
        config = config.replaceAll("_\\$log\\$_", LinkBase.getInstance().getLink(Links.PATH_MANAGER).getLogDirectory().getAbsolutePath());
        config = config.replaceAll("_\\$file_mode\\$_", DebugProvider.getInstance().hasDebug(DebugMode.LOG_FILE) ? "TRACE" : "INFO");
        config = config.replaceAll("_\\$log_mode\\$_", DebugProvider.getInstance().hasDebug(DebugMode.LOG) ? "TRACE" : "INFO");
        new DOMConfigurator().doConfigure(IOUtil.toInputStream(config, StandardCharsets.UTF_8), LogManager.getLoggerRepository());
}

}
