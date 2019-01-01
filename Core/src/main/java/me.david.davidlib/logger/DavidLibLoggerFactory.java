package me.david.davidlib.logger;

import me.david.davidlib.utils.init.InitialisedOnce;
import me.david.davidlib.utils.io.Charsets;
import me.david.davidlib.utils.io.IOUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.IOException;
import java.nio.charset.Charset;

public class DavidLibLoggerFactory extends InitialisedOnce implements LoggerFactory {
    @Override
    public Logger getLoggerInstance(String name) {
        initalizeIfNotAlready();
        return new DavidLibLogger(org.apache.log4j.Logger.getLogger(name));
    }

    @Override
    protected void init() {
        System.setProperty("log4j.defaultInitOverride", "true");
        try {
            String config = IOUtil.resourceToText("/log-config.xml");
            config = config.replaceAll("_\\$log\\$_", "log/");
            new DOMConfigurator().doConfigure(IOUtil.toInputStream(config, Charsets.UTF8), LogManager.getLoggerRepository());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
