package io.github.splotycode.mosaik.startup.starttask;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import com.google.common.reflect.ClassPath;
import io.github.splotycode.mosaik.annotations.AnnotationHelper;
import io.github.splotycode.mosaik.runtime.startup.StartupTask;
import io.github.splotycode.mosaik.runtime.startup.environment.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.startup.exception.FrameworkStartException;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.io.IOUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.ClassCollector;
import io.github.splotycode.mosaik.util.reflection.ClassFinderHelper;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StartTaskExecutor {

    private StartTaskExecutor() {}

    private static Logger logger = Logger.getInstance(StartTaskExecutor.class);

    @Getter private static StartTaskExecutor instance = new StartTaskExecutor();

    private static ClassCollector classCollector = ClassCollector.newInstance()
                                                    .setOnlyClasses(true)
                                                    .setNoDisableds(true)
                                                    .setNeedAssignable(StartupTask.class);

    private TreeMultimap<Integer, StartupTask> tasks = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());

    public void findAll(boolean externalCall) {
        if (externalCall) tasks.clear();
        int run = 0;
        for (Class<?> clazz : classCollector.collectAll()) {
            try {
                int priority = AnnotationHelper.getPriority(clazz.getAnnotations());
                //logger.info("Found StartUp Task: " + clazz.getSimpleName() + " with priority " + priority);
                StartupTask task = (StartupTask) clazz.newInstance();
                tasks.put(priority, task);
                run++;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new FrameworkStartException("Could not create task instance (" + clazz.getSimpleName() + ")", e);
            } catch (ClassCastException e) {
                throw new FrameworkStartException("Could not cast to StartupTask (" + clazz.getSimpleName() + ")", e);
            }
        }
        logger.info("Found StartUp Tasks " + run + "/" + classCollector.totalResults() + ": " + StringUtil.join(tasks.values(), obj -> obj.getClass().getSimpleName(), ", "));
    }

    public void runAll(StartUpEnvironmentChanger environmentChanger) {
        int run = 0;
        for (StartupTask task : tasks.values()) {
            //logger.info("Executing: " + task.getClass().getSimpleName());
            try {
                task.execute(environmentChanger);
                run++;
            } catch (Throwable ex) {
                logger.info("Failed to Execute StartupTask", new FrameworkStartException("Exception in StartupTask", ex));
            }
        }
        logger.info("Executing StartUp Tasks (" + run + "/" + tasks.values().size() + "): " + StringUtil.join(tasks.values(), obj -> obj.getClass().getSimpleName(), ", "));
    }

    private List<ClassPath.ResourceInfo> getSippedFiles(ClassLoader classLoader) {
        try {
            List<ClassPath.ResourceInfo> files = new ArrayList<>();
            for (ClassPath.ResourceInfo resource : ClassPath.from(classLoader).getResources()) {
                if (resource.getResourceName().contains("disabled_paths")) {
                    files.add(resource);
                }
            }
            return files;
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }
        return Collections.emptyList();
    }

    public void collectSkippedPaths(ClassLoader classLoader) {
        List<ClassPath.ResourceInfo> files = getSippedFiles(classLoader);
        for (ClassPath.ResourceInfo file : files) {
            try (InputStream is = file.asByteSource().openStream()) {
                List<String> lines = IOUtil.loadLines(is);
                for (String skippedPath : lines) {
                    ClassFinderHelper.registerSkippedPath(skippedPath);
                }
            } catch (IOException ex) {
                ExceptionUtil.throwRuntime(ex);
            }
        }
        if (ClassFinderHelper.getSkippedPaths().isEmpty()) {
            logger.info("Could not found any disabled paths");
        } else {
            logger.info("Found " + ClassFinderHelper.getSkippedPaths().size() + " disabled paths from " + files.size() + " disabled_paths files");
        }
    }

}
