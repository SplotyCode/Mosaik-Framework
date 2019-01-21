package io.github.splotycode.mosaik.gameengine.render;

import io.github.splotycode.mosaik.gameengine.gameloop.TickFrameLoop;
import io.github.splotycode.mosaik.gameengine.gameobjects.GameObject;

/**
 * Represents an Object that can be renderer
 * Avery GameObject uses Renderer Class
 * @see GameObject
 * You can use it in an GameLoop
 * @see TickFrameLoop
 */
public interface Renderer {

    void render();

}
