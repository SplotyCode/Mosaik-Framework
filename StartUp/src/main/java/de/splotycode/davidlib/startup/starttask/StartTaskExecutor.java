package de.splotycode.davidlib.startup.starttask;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import de.splotycode.davidlib.startup.exception.FrameworkStartException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.david.davidlib.annotations.AnnotationHelper;
import me.david.davidlib.runtimeapi.startup.StartupTask;
import me.david.davidlib.runtimeapi.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.util.logger.Logger;
import me.david.davidlib.util.io.IOUtil;
import me.david.davidlib.util.reflection.ClassFinderHelper;
import me.david.davidlib.util.reflection.ReflectionUtil;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartTaskExecutor {

    private static Logger logger = Logger.getInstance(StartTaskExecutor.class);

    @Getter private static StartTaskExecutor instance = new StartTaskExecutor();

    private TreeMultimap<Integer, StartupTask> tasks = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());

    public void findAll(boolean externalCall) {
        if (externalCall) tasks.clear();
        for (Class<?> clazz : ClassFinderHelper.getUserClasses()) {
            if (ReflectionUtil.validClass(clazz, StartupTask.class, true, true)) {
                try {
                    int priority = AnnotationHelper.getPriority(clazz.getAnnotations());
                    logger.info("Found StartUp Task: " + clazz.getSimpleName() + " with priority " + priority);
                    StartupTask task = (StartupTask) clazz.newInstance();
                    tasks.put(priority, task);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new FrameworkStartException("Could not create task instance (" + clazz.getSimpleName() + ")", e);
                } catch (ClassCastException e) {
                    throw new FrameworkStartException("Could not cast to StartupTask (" + clazz.getSimpleName() + ")", e);
                }
            }
        }
    }

    public void runAll(StartUpEnvironmentChanger environmentChanger) {
        for (StartupTask task : tasks.values()) {
            logger.info("Executing: " + task.getClass().getSimpleName());
            try {
                task.execute(environmentChanger);
            } catch (Exception ex) {
                throw new FrameworkStartException("Exception in StartupTask", ex);
            }
        }
    }

    public void collectSkippedPaths() {
        Reflections reflections = new Reflections(".*", new ResourcesScanner());
        for (String file : reflections.getResources(x -> x != null && x.startsWith("disabled_paths"))) {
            try (InputStream is = StartTaskExecutor.class.getResourceAsStream("/" + file)) {
                List<String> lines = IOUtil.loadLines(is);
                logger.info("Registered " + lines.size() + " disabled paths from '" + file + "'.txt");
                for (String skippedPath : lines) {
                    ClassFinderHelper.registerSkippedPath(skippedPath);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (ClassFinderHelper.getSkippedPaths().isEmpty()) {
            logger.info("Could not found any disabled paths");
        } else {
            logger.info("Found " + ClassFinderHelper.getSkippedPaths().size() + " disabled paths");
        }
    }

}
