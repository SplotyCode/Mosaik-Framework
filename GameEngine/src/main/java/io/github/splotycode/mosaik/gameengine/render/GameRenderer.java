package io.github.splotycode.mosaik.gameengine.render;

import lombok.Getter;
import lombok.Setter;
import io.github.splotycode.mosaik.gameengine.gameloop.GameLoop;
import io.github.splotycode.mosaik.gameengine.window.Window;

public class GameRenderer implements Renderer {

    @Getter @Setter private Renderer renderer2d = () -> {};
    @Getter @Setter private Renderer renderer3d = () -> {};

    private GameLoop gameLoop;
    private Window window;

    public GameRenderer(GameLoop gameLoop, Window window) {
        this.gameLoop = gameLoop;
        this.window = window;
    }

    @Override
    public void render() {
        window.clear();
        renderer3d.render();
        renderer2d.render();
        window.update(gameLoop);
    }

}
