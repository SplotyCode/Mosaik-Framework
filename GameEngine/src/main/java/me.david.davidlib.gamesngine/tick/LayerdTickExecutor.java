package me.david.davidlib.gamesngine.tick;

import me.david.davidlib.objects.Pair;

import java.util.Map;
import java.util.TreeMap;

public class LayerdTickExecutor implements TickExecutor {

    private Map<Integer, Pair<String, ExecutorTask>> map = new TreeMap<>();

    @Override
    public void runTick() {
        preTick();
        for (Pair<String, ExecutorTask> task : map.values()) {
            task.getTwo().exec();
        }
        postTick();
    }

    public void installTask(int priority, String name, ExecutorTask task) {
        map.put(priority, new Pair<>(name, task));
    }

    public void uninstallTask(String name) {
        for (Map.Entry<Integer, Pair<String, ExecutorTask>> task : map.entrySet()) {
            if (task.getValue().getOne().equals(name)) {
                map.remove(task.getKey());
                /* Do not break - we want to install all tasks with this name is exists */
            }
        }
    }

    @Override public void preTick() {}
    @Override public void postTick() {}

}
