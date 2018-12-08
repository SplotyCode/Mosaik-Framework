package de.splotycode.davidlib.startup.starttask;

import de.splotycode.davidlib.startup.exception.FrameworkStartException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.david.davidlib.annotation.AnnotationHelper;
import me.david.davidlib.startup.StartupTask;
import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.utils.reflection.ClassFinderHelper;
import me.david.davidlib.utils.reflection.ReflectionUtil;
import org.apache.commons.io.IOUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartTaskExecutor {

    @Getter private static StartTaskExecutor instance = new StartTaskExecutor();

    private TreeMap<Integer, StartupTask> tasks = new TreeMap<>();

    public void findAll(boolean externalCall) {
        if (externalCall) tasks.clear();
        for (Class<?> clazz : ClassFinderHelper.getUserClasses()) {
            if (ReflectionUtil.validClass(clazz, StartupTask.class, true, true)) {
                try {
                    StartupTask task = (StartupTask) clazz.newInstance();
                    tasks.put(AnnotationHelper.getPriority(clazz.getAnnotations()), task);
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
            try {
                task.execute(environmentChanger);
            } catch (Exception ex) {
                throw new FrameworkStartException("Exception in StartupTask", ex);
            }
        }
    }

    public void collectSkippedPaths() {
        System.out.println("Collecting Skipped Paths");
        Reflections reflections = new Reflections(null, new ResourcesScanner());
        for (String file : reflections.getResources(x -> x != null && x.startsWith("disabled_paths"))) {
            try (InputStream is = StartTaskExecutor.class.getResourceAsStream("/" + file)) {
                String[] array = IOUtils.toString(is, "UTF-8").split("\n");
                System.out.println("Registered " + array.length + " disabled paths from '" + file + "'.txt");
                for (String skippedPath : array) {
                    ClassFinderHelper.registerSkippedPath(skippedPath);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
