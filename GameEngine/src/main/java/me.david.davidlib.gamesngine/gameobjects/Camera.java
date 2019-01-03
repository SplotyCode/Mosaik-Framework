package me.david.davidlib.gamesngine.gameobjects;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.gamesngine.input.Keyboard;
import me.david.davidlib.util.math.Vector2D;
import me.david.davidlib.util.math.Vector3D;

import static org.lwjgl.glfw.GLFW.*;

public class Camera implements PositionableGameObject {

    @Getter @Setter private Vector3D position = new Vector3D(0, 0, 0);
    @Getter @Setter private Vector2D rotation = new Vector2D(10, 0);
    @Getter @Setter private int id;

    @Override
    public void update() {
        if (Keyboard.isKeyDown(GLFW_KEY_W)) {
            position.setZ(position.getZ() - 0.2);
        }
        if (Keyboard.isKeyDown(GLFW_KEY_S)) {
            position.setZ(position.getZ() + 0.2);
        }
        if (Keyboard.isKeyDown(GLFW_KEY_D)) {
            position.setX(position.getX() - 0.2);
        }
        if (Keyboard.isKeyDown(GLFW_KEY_A)) {
            position.setX(position.getX() + 0.2);
        }
        if (Keyboard.isKeyDown(GLFW_KEY_W)) {
            position.setZ(position.getZ() - 0.2);
        }
        if (Keyboard.isKeyDown(GLFW_KEY_W)) {
            position.setZ(position.getZ() - 0.2);
        }
        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
            position.setY(position.getY() + 0.2);
        }
        if(Keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT)){
            position.setY(position.getY() - 0.2);
        }
    }

    @Override
    public void render() {

    }

}
