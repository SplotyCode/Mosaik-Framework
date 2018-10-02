package me.david.davidlib.gamesngine.render;

import lombok.Getter;
import me.david.davidlib.gamesngine.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This Represents a World
 * It Managed the GameObject and Terrain on the World
 */
public class World implements Renderer {

    @Getter private List<GameObject> gameObjects = new ArrayList<>();

    private AtomicInteger currentId = new AtomicInteger(0);

    public void addGameObject(GameObject gameObject) {
        gameObject.setId(currentId.getAndIncrement());
        gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    @Override
    public void render() {
        gameObjects.forEach(GameObject::render);
    }

}
