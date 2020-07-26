package io.github.splotycode.mosaik.startup.manager;

import io.github.splotycode.mosaik.iui.INamedTaskBar;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.startup.IStartUpManager;
import io.github.splotycode.mosaik.runtime.startup.environment.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.startup.envirementchanger.StartUpInvirementChangerImpl;
import io.github.splotycode.mosaik.startup.processbar.StartUpProcessHandler;
import io.github.splotycode.mosaik.startup.starttask.StartTaskExecutor;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.classpath.ClassPath;
import io.github.splotycode.mosaik.util.reflection.scope.ClassScope;
import io.github.splotycode.mosaik.util.reflection.scope.DefaultClassScope;

import java.io.IOException;

public class StartUpManager implements IStartUpManager {
    private final Logger logger = Logger.getInstance(StartUpManager.class);
    private StartTaskExecutor startTaskExecutor = new StartTaskExecutor();
    private StartUpEnvironmentChanger environmentChanger;
    private boolean running = false;
    private int skippedPathFiles;

    public void prepairEnvironment() {
        environmentChanger = new StartUpInvirementChangerImpl();
        LinkBase.getInstance().registerLink(Links.STARTUP_ENVIRONMENT_CHANGER, environmentChanger);
    }

    public void runStartupTasks() {
        startTaskExecutor.findAll(false);
        startTaskExecutor.runAll(environmentChanger);
    }

    public ClassScope collectSkippedPaths(ClassPath classPath) {
        DefaultClassScope classScope = DefaultClassScope.createBlacklist();
        classPath.loadAllResources();
        classPath.resources().forEach(resource -> {
            if (resource.getPath().contains("disabled_paths")) {
                try {
                    resource.readLines().forEach(line -> {
                        if (!line.startsWith("#")) {
                            classScope.ignorePath(line);
                        }
                    });
                } catch (IOException e) {
                    ExceptionUtil.throwRuntime(e);
                }
                skippedPathFiles++;
            }
        });
        if (classScope.getIgnored().isEmpty()) {
            logger.info("Could not found any disabled paths");
        } else {
            logger.info("Found " + classScope.getIgnored().size() + " disabled paths from " + skippedPathFiles + " disabled_paths files");
        }
        return classScope;
    }

    @Override
    public void researchTasks() {
        startTaskExecutor.findAll(true);
    }

    @Override
    public void executeTasks() {
        startTaskExecutor.runAll(LinkBase.getInstance().getLink(Links.STARTUP_ENVIRONMENT_CHANGER));
    }

    @Override
    public boolean running() {
        return running;
    }

    public void finished() {
        running = true;
    }

    @Override
    public INamedTaskBar currentProcess() {
        return StartUpProcessHandler.getInstance().current();
    }
}
