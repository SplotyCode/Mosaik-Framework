package me.david.davidlib.runtimeapi;

import me.david.davidlib.util.datafactory.DataKey;
import me.david.davidlib.util.info.PathManager;
import me.david.davidlib.util.core.link.argparser.IArgParser;
import me.david.davidlib.util.core.link.transformer.ITransformerManager;
import me.david.davidlib.util.core.startup.BootContext;
import me.david.davidlib.util.core.startup.envirement.StartUpEnvironmentChanger;

public final class Links {

    public static final DataKey<IApplicationManager> APPLICATION_MANAGER = new DataKey<>("startup.applicationManager");
    public static final DataKey<BootContext> BOOT_DATA = new DataKey<>("startup.bootContext");
    public static final DataKey<ITransformerManager> TRANSFORMER_MANAGER = new DataKey<>("transformer.manager");
    public static final DataKey<StartUpEnvironmentChanger> STARTUP_ENVIRONMENT_CHANGER = new DataKey<>("startup.environment_changer");
    public static final DataKey<IStartUpManager> STARTUP_MANAGER = new DataKey<>("startup.manager");
    public static final DataKey<IArgParser> ARG_PARSER = new DataKey<>("argparser.argparser");
    public static final DataKey<ParsingManager> PARSING_MANAGER = new DataKey<>("parsing.manager");
    public static final DataKey<PathManager> PATH_MANAGER = new DataKey<>("path_manager");

}
