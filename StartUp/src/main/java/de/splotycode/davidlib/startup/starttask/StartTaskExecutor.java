package de.splotycode.davidlib.startup.starttask;

import de.splotycode.davidlib.startup.exception.FrameworkStartException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.david.davidlib.annotation.AnnotationHelper;
import me.david.davidlib.startup.StartupTask;
import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.utils.ClassFinderHelper;
import me.david.davidlib.utils.ReflectionUtil;

import java.util.TreeMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartTaskExecutor {

    @Getter private static StartTaskExecutor instance = new StartTaskExecutor();

    private TreeMap<Integer, StartupTask> tasks = new TreeMap<>();

    public void findAll() {
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

}
