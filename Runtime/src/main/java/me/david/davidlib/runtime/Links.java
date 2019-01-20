package me.david.davidlib.runtime;

import de.splotycode.davidlib.argparser.IArgParser;
import me.david.davidlib.runtime.application.IApplicationManager;
import me.david.davidlib.runtime.parsing.ParsingManager;
import me.david.davidlib.runtime.pathmanager.PathManager;
import me.david.davidlib.runtime.startup.BootContext;
import me.david.davidlib.runtime.startup.IStartUpManager;
import me.david.davidlib.runtime.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.util.datafactory.DataKey;

public final class Links {

    public static final DataKey<IApplicationManager> APPLICATION_MANAGER = new DataKey<>("startup.applicationManager");
    public static final DataKey<BootContext> BOOT_DATA = new DataKey<>("startup.bootContext");
    public static final DataKey<StartUpEnvironmentChanger> STARTUP_ENVIRONMENT_CHANGER = new DataKey<>("startup.environment_changer");
    public static final DataKey<IStartUpManager> STARTUP_MANAGER = new DataKey<>("startup.manager");
    public static final DataKey<ParsingManager> PARSING_MANAGER = new DataKey<>("parsing.manager");
    public static final DataKey<PathManager> PATH_MANAGER = new DataKey<>("path_manager");
    public static final DataKey<IArgParser> ARG_PARSER = new DataKey<>("argparser.argparser");

}
