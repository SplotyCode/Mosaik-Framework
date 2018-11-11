package de.splotycode.davidlib.startup;

import de.splotycode.davidlib.startup.exception.FrameworkStartException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.david.davidlib.startup.StartupTask;
import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.utils.ClassFinderHelper;
import me.david.davidlib.utils.ReflectionUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartTaskExecutor {

    @Getter private static StartTaskExecutor instance = new StartTaskExecutor();

    public void runAll(StartUpEnvironmentChanger environmentChanger) {
        for (Class<?> clazz : ClassFinderHelper.getUserClasses()) {
            if (ReflectionUtil.validClass(clazz, StartupTask.class, true, true)) {
                try {
                    StartupTask task = (StartupTask) clazz.newInstance();
                    try {
                        task.execute(environmentChanger);
                    } catch (Exception ex) {
                        throw new FrameworkStartException("Exception in StartupTask", ex);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new FrameworkStartException("Could not create task instance", e);
                } catch (ClassCastException e) {
                    throw new FrameworkStartException("Could not cast to StartupTask", e);
                }
            }
        }
    }

}
