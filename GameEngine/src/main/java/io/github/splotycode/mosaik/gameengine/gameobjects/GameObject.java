package io.github.splotycode.mosaik.gameengine.gameobjects;

import io.github.splotycode.mosaik.gameengine.render.Renderer;

public interface GameObject extends Renderer {

    int getId();
    void setId(int id);

    void update();

}
