package me.david.davidlib.gamesngine.tick.executors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.david.davidlib.gamesngine.gameobjects.GameObject;
import me.david.davidlib.gamesngine.render.World;
import me.david.davidlib.gamesngine.tick.ExecutorTask;

@AllArgsConstructor
public class GameObjectsTickTask implements ExecutorTask {

    @Getter
    private final World world;

    @Override
    public void exec() {
        world.getGameObjects().forEach(GameObject::update);
    }

}
