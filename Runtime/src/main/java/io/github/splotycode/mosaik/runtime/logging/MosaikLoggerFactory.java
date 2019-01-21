package io.github.splotycode.mosaik.runtime.logging;

import io.github.splotycode.mosaik.util.init.InitialisedOnce;
import io.github.splotycode.mosaik.util.io.IOUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.logger.LoggerFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MosaikLoggerFactory extends InitialisedOnce implements LoggerFactory {
    @Override
    public Logger getLoggerInstance(String name) {
        initalizeIfNotAlready();
        return new MosaikLogger(org.apache.log4j.Logger.getLogger(name));
    }

    @Override
    protected void init() {
        System.setProperty("log4j.defaultInitOverride", "true");
        try {
            String config = IOUtil.resourceToText("/log-config.xml");
            config = config.replaceAll("_\\$log\\$_", "log/");
            new DOMConfigurator().doConfigure(IOUtil.toInputStream(config, StandardCharsets.UTF_8), LogManager.getLoggerRepository());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
