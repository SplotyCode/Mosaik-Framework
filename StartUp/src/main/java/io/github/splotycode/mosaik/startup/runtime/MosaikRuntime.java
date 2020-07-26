package io.github.splotycode.mosaik.startup.runtime;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.Runtime;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.runtime.startup.StartUpConfiguration;
import io.github.splotycode.mosaik.startup.application.ApplicationManager;
import io.github.splotycode.mosaik.startup.manager.StartUpManager;
import io.github.splotycode.mosaik.util.reflection.ClassFinderHelper;
import io.github.splotycode.mosaik.util.reflection.classpath.ClassPath;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MosaikRuntime extends Runtime {
    public static void create(BootContext bootContext, StartUpConfiguration startUpConfiguration) {
        runtime = new MosaikRuntime(bootContext, startUpConfiguration);
    }

    private StartUpManager startUpManager = new StartUpManager();
    @Setter private ApplicationManager applicationManager;

    private MosaikRuntime(BootContext bootContext, StartUpConfiguration startUpConfiguration) {
        super(bootContext, startUpConfiguration);
    }

    public void prepareLinkage() {
        applicationManager = new ApplicationManager();
        LinkBase.getInstance().registerLink(Links.APPLICATION_MANAGER, applicationManager);
        LinkBase.getInstance().registerLink(Links.STARTUP_MANAGER, startUpManager);
    }

    public void loadClassPath() {
        ClassLoader[] classLoaders = bootContext.getClassLoaderProvider().getClassLoaders();
        globalClassPath = ClassPath.fromClassLoaders(classLoaders);
        ClassFinderHelper.setClassLoader(classLoaders[0]);
        globalClassPath.load(startUpManager.collectSkippedPaths(globalClassPath));
    }
}
