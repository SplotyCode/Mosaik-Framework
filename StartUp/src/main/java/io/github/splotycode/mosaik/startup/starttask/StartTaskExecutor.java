package io.github.splotycode.mosaik.startup.starttask;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import io.github.splotycode.mosaik.annotations.AnnotationHelper;
import io.github.splotycode.mosaik.runtime.startup.StartupTask;
import io.github.splotycode.mosaik.runtime.startup.environment.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.startup.exception.FrameworkStartException;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.io.IOUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.ClassCollector;
import io.github.splotycode.mosaik.util.reflection.ClassFinderHelper;
import io.github.splotycode.mosaik.util.reflection.ClassPath;
import lombok.Getter;

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

    public void collectSkippedPaths(ClassLoader classLoader) {
        int[] files = {0};
        new ClassPath(classLoader).resources(resource -> {
            if (resource.getPath().contains("disabled_paths")) {
                String text = IOUtil.resourceToText(resource.getPath());
                for (String skippedPath : text.split("\n")) {
                    ClassFinderHelper.registerSkippedPath(skippedPath);
                }
                files[0]++;
            }
        });
        if (ClassFinderHelper.getSkippedPaths().isEmpty()) {
            logger.info("Could not found any disabled paths");
        } else {
            logger.info("Found " + ClassFinderHelper.getSkippedPaths().size() + " disabled paths from " + files[0] + " disabled_paths files");
        }
    }

}
