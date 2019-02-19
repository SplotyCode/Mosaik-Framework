package io.github.splotycode.mosaik.runtime.logging;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.debug.DebugMode;
import io.github.splotycode.mosaik.runtime.debug.DebugProvider;
import io.github.splotycode.mosaik.runtime.startup.tasks.PreLinkBaseStartUp;
import io.github.splotycode.mosaik.util.init.InitialisedOnce;
import io.github.splotycode.mosaik.util.io.IOUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.logger.LoggerFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.IOError;
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
        if (!LinkBase.getInstance().getLinkFactory().containsData(Links.PARSING_MANAGER)) {
            new PreLinkBaseStartUp().execute(null);
        }
        try {
            String config = IOUtil.resourceToText("/log-config.xml");
            config = config.replaceAll("_\\$log\\$_", LinkBase.getInstance().getLink(Links.PATH_MANAGER).getLogDirectory().getAbsolutePath());
            config = config.replaceAll("_\\$file_mode\\$_", DebugProvider.getInstance().hasDebug(DebugMode.LOG_FILE) ? "DEBUG" : "NORMAL");
            config = config.replaceAll("_\\$log_mode\\$_", DebugProvider.getInstance().hasDebug(DebugMode.LOG) ? "DEBUG" : "INFO");
            new DOMConfigurator().doConfigure(IOUtil.toInputStream(config, StandardCharsets.UTF_8), LogManager.getLoggerRepository());
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

}
