package me.david.davidlib.gamesngine.gameobjects;

import me.david.davidlib.gamesngine.render.Renderer;

public interface GameObject extends Renderer {

    int getId();
    void setId(int id);

    void update();

}
