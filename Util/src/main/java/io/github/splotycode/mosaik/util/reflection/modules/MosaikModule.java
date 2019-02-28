package io.github.splotycode.mosaik.util.reflection.modules;

public enum MosaikModule implements IModule {

    ANNOTATIONS("io.github.splotycode.mosaik.annotations.Disabled"),
    IUI("io.github.splotycode.mosaik.iui.ITaskBar"),
    UTIL("io.github.splotycode.mosaik.util.StringUtil", ANNOTATIONS),
    ANNOTATION_PROCESSING("io.github.splotycode.mosaik.annotationprocessors.SkipPathProcessor",
            ANNOTATIONS, UTIL),
    ARG_PARSER("io.github.splotycode.mosaik.argparser.IArgParser", UTIL),
    DOM_PARSING("io.github.splotycode.mosaik.domparsing.dom.Document", UTIL),
    RUNTIME("io.github.splotycode.mosaik.runtime.application.IApplication",
            UTIL, IUI, ARG_PARSER, DOM_PARSING),
    ARG_PARSER_IMPL("io.github.splotycode.mosaik.argparserimpl.ArgParser"),
    AUTOMATISATION("io.github.splotycode.mosaik.automatisation.generators.Generator",
            UTIL, RUNTIME),
    CONSOLE("io.github.splotycode.mosaik.console.ConsoleApplicationType", RUNTIME),
    DATABASE("io.github.splotycode.mosaik.database.connection.Connection", UTIL),
    VALUE_TRANSFORMER("io.github.splotycode.mosaik.valuetransformer.ValueTransformer", RUNTIME),
    DOM_PARSING_IMPL("io.github.splotycode.mosaik.domparsingimpl.ParsingManagerImpl",
            VALUE_TRANSFORMER, RUNTIME),
    GAME_ENGINE("io.github.splotycode.mosaik.gameengine.gameloop.GameLoop", UTIL),
    NETTY("io.github.splotycode.mosaik.netty.NetSession", UTIL),
    STARTUP("io.github.splotycode.mosaik.startup.Main",
            CONSOLE, RUNTIME, ANNOTATION_PROCESSING, ARG_PARSER, DOM_PARSING),
    WEB_API("io.github.splotycode.mosaik.webapi.WebApplicationType", RUNTIME, STARTUP, VALUE_TRANSFORMER),
    ALL(ANNOTATIONS, UTIL, ANNOTATION_PROCESSING, RUNTIME,
            ARG_PARSER, AUTOMATISATION, CONSOLE, DATABASE, DOM_PARSING,
            DOM_PARSING_IMPL, IUI, NETTY, STARTUP, WEB_API),
    SPIGOT_LIB("io.github.splotycode.mosaik.spigotlib.SpigotApplicationType", UTIL, RUNTIME);

    private IModule[] dependencies;

    private String loadChecker = null;

    MosaikModule(String loadChecker, IModule... dependencies) {
        this.dependencies = dependencies;
        this.loadChecker = loadChecker;
    }

    MosaikModule(IModule... dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public IModule[] getDependencies() {
        return dependencies;
    }

    @Override
    public String getDisplayName() {
        return name();
    }

    @Override
    public String[] loadChecker() {
        return new String[] {loadChecker};
    }


}
