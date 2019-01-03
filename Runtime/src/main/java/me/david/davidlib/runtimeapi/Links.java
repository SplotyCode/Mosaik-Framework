package me.david.davidlib.runtimeapi;

import me.david.davidlib.runtimeapi.application.IApplicationManager;
import me.david.davidlib.runtimeapi.argparser.IArgParser;
import me.david.davidlib.runtimeapi.parsing.ParsingManager;
import me.david.davidlib.runtimeapi.pathmanager.PathManager;
import me.david.davidlib.runtimeapi.startup.BootContext;
import me.david.davidlib.runtimeapi.startup.IStartUpManager;
import me.david.davidlib.runtimeapi.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.runtimeapi.transformer.ITransformerManager;
import me.david.davidlib.util.datafactory.DataKey;

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
