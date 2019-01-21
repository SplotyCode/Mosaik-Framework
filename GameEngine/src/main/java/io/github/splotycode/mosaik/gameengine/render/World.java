package io.github.splotycode.mosaik.gameengine.render;

import lombok.Getter;
import io.github.splotycode.mosaik.gameengine.gameobjects.Camera;
import io.github.splotycode.mosaik.gameengine.gameobjects.GameObject;
import io.github.splotycode.mosaik.gameengine.tick.ExecutorTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.opengl.GL11.*;

/**
 * This Represents a World
 * It Managed the GameObject and Terrain on the World
 */
public class World implements Renderer, ExecutorTask {

    @Getter private List<GameObject> gameObjects = new ArrayList<>();

    private Camera camera;

    private AtomicInteger currentId = new AtomicInteger(0);

    public void addGameObject(GameObject gameObject) {
        gameObject.setId(currentId.getAndIncrement());
        gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public void init() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    @Override
    public void render() {
        gameObjects.forEach(GameObject::render);
        camera.render();
    }

    @Override
    public void exec() {
        gameObjects.forEach(GameObject::update);
        camera.update();
    }
}
