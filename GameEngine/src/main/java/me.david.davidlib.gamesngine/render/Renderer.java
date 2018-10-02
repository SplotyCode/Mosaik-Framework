package me.david.davidlib.gamesngine.render;

/**
 * Represents an Object that can be renderer
 * Avery GameObject uses Renderer Class
 * @see me.david.davidlib.gamesngine.gameobjects.GameObject
 * You can use it in an GameLoop
 * @see me.david.davidlib.gamesngine.gameloop.TickFrameLoop
 */
public interface Renderer {

    void render();

}
