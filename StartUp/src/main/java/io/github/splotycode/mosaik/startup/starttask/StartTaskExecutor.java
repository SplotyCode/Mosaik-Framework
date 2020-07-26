package io.github.splotycode.mosaik.startup.starttask;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import io.github.splotycode.mosaik.annotations.AnnotationHelper;
import io.github.splotycode.mosaik.annotations.visibility.VisibilityLevel;
import io.github.splotycode.mosaik.runtime.Runtime;
import io.github.splotycode.mosaik.runtime.startup.StartupTask;
import io.github.splotycode.mosaik.runtime.startup.environment.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.startup.exception.FrameworkStartException;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.collector.ClassCollector;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class StartTaskExecutor {
    private static Logger logger = Logger.getInstance(StartTaskExecutor.class);

    private static ClassCollector classCollector = ClassCollector.newInstance()
                                                    .setOnlyClasses(true)
                                                    .setNoDisable(true)
                                                    .setVisibility(VisibilityLevel.NOT_INVISIBLE)
                                                    .setNeedAssignable(StartupTask.class);

    private final io.github.splotycode.mosaik.util.reflection.classpath.ClassPath classPath;
    private TreeMultimap<Integer, StartupTask> tasks = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());

    public StartTaskExecutor() {
        this(Runtime.getRuntime().getGlobalClassPath());
    }

    public void findAll(boolean externalCall) {
        if (externalCall) tasks.clear();
        int run = 0;
        Collection<Class> classes = classCollector.collectAll(classPath);
        for (Class<?> clazz : classes) {
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
        logger.info("Found StartUp Tasks " + run + "/" + classes.size() + ": " + StringUtil.join(tasks.values(), obj -> obj.getClass().getSimpleName(), ", "));
    }

    public void runAll(StartUpEnvironmentChanger environmentChanger) {
        int run = 0;
        for (StartupTask task : tasks.values()) {
            try {
                task.execute(environmentChanger);
                run++;
            } catch (Throwable ex) {
                logger.warn("Failed to Execute StartupTask", new FrameworkStartException("Exception in StartupTask", ex));
            }
        }
        logger.info("Executing StartUp Tasks (" + run + "/" + tasks.values().size() + "): " + StringUtil.join(tasks.values(), obj -> obj.getClass().getSimpleName(), ", "));
    }
}
