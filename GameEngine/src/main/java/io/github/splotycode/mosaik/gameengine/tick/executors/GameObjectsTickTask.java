package io.github.splotycode.mosaik.gameengine.tick.executors;

import io.github.splotycode.mosaik.gameengine.render.World;
import io.github.splotycode.mosaik.gameengine.tick.ExecutorTask;
import lombok.AllArgsConstructor;
import lombok.Getter;
import io.github.splotycode.mosaik.gameengine.gameobjects.GameObject;

@AllArgsConstructor
public class GameObjectsTickTask implements ExecutorTask {

    @Getter
    private final World world;

    @Override
    public void exec() {
        world.getGameObjects().forEach(GameObject::update);
    }

}
