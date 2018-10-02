package me.david.davidlib.gamesngine.render;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.gamesngine.gameloop.GameLoop;
import me.david.davidlib.gamesngine.window.Window;

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
        renderer2d.render();
        renderer3d.render();
        window.update(gameLoop);
    }

}
