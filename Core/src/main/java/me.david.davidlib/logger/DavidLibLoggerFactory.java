package me.david.davidlib.logger;

import me.david.davidlib.utils.init.InitialisedOnce;
import org.apache.commons.io.IOUtils;
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
            String config = IOUtils.resourceToString("/log-config.xml", Charset.forName("UTF-8"));
            config = config.replaceAll("_\\$log\\$_", "log/");
            new DOMConfigurator().doConfigure(IOUtils.toInputStream(config, Charset.forName("UTF-8")), LogManager.getLoggerRepository());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
