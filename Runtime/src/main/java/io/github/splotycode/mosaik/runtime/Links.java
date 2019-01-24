package io.github.splotycode.mosaik.runtime;

import io.github.splotycode.mosaik.argparser.IArgParser;
import io.github.splotycode.mosaik.domparsing.annotation.FileSystemProvider;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingManager;
import io.github.splotycode.mosaik.runtime.application.IApplicationManager;
import io.github.splotycode.mosaik.runtime.pathmanager.PathManager;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.runtime.startup.IStartUpManager;
import io.github.splotycode.mosaik.runtime.startup.envirement.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.util.datafactory.DataKey;

public final class Links {

    public static final DataKey<IApplicationManager> APPLICATION_MANAGER = new DataKey<>("startup.applicationManager");
    public static final DataKey<BootContext> BOOT_DATA = new DataKey<>("startup.bootContext");
    public static final DataKey<StartUpEnvironmentChanger> STARTUP_ENVIRONMENT_CHANGER = new DataKey<>("startup.environment_changer");
    public static final DataKey<IStartUpManager> STARTUP_MANAGER = new DataKey<>("startup.manager");
    public static final DataKey<ParsingManager> PARSING_MANAGER = new DataKey<>("parsing.manager");
    public static final DataKey<FileSystemProvider> PARSING_FILEPROVIDER = new DataKey<>("parsing.file_system_provider");
    public static final DataKey<PathManager> PATH_MANAGER = new DataKey<>("path_manager");
    public static final DataKey<IArgParser> ARG_PARSER = new DataKey<>("argparser.argparser");

}
