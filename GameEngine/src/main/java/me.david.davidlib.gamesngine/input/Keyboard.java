package me.david.davidlib.gamesngine.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard extends GLFWKeyCallback {

    private static boolean[] keyStates = new boolean[1024];

    @Override
    public void invoke(long window, int key, int scanCode, int action, int mods) {
        keyStates[key] = action != GLFW_RELEASE;
    }

    public static boolean isKeyDown(int keycode) {
        return keyStates[keycode];
    }

}
