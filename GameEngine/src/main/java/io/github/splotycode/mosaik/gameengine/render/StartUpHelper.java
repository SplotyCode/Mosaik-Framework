package io.github.splotycode.mosaik.gameengine.render;

import lombok.Getter;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class StartUpHelper {

    @Getter private static StartUpHelper instance = new StartUpHelper();

    private boolean glfwInitialised = false;

    public void initGLFW() {
        if (glfwInitialised) throw new IllegalStateException("GLFW is already initialised");
        glfwInitialised = true;
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    public void destructGLFW() {
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

}
