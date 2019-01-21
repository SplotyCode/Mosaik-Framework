package io.github.splotycode.mosaik.gameengine.window;

import io.github.splotycode.mosaik.gameengine.input.Keyboard;
import lombok.Getter;
import io.github.splotycode.mosaik.gameengine.gameloop.GameLoop;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    @Getter private long id;
    @Getter private boolean closed = false;

    public Window(int width, int height) {
        createWindow(width, height);
    }

    public Window(int width, int height, String name) {
        this(width, height);
        setName(name);
    }

    public void setName(String name) {
        glfwSetWindowTitle(id, name);
    }

    private void createWindow(int width, int height) {
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        id = glfwCreateWindow(width, height, "Unamed Window", NULL, NULL);

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(id, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    id,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }
        glfwSetKeyCallback(id, new Keyboard());
        glfwMakeContextCurrent(id);
        // Enable v-sync
        glfwSwapInterval(1);
        glfwShowWindow(id);

        GL.createCapabilities();
        glClearColor(0, 0, 0, 1);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
    }

    public void update(GameLoop loop) {
        glfwSwapBuffers(id);
        glfwPollEvents();
        if (glfwWindowShouldClose(id)) {
            loop.end();
            closeWindow();
        }
    }

    public void closeWindow() {
        closed = true;
        glfwFreeCallbacks(id);
        glfwDestroyWindow(id);
    }

}
