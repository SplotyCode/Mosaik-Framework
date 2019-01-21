package io.github.splotycode.mosaik.gameengine.gameobjects;

import io.github.splotycode.mosaik.util.math.Vector3D;

public interface PositionableGameObject extends GameObject {

    Vector3D getPosition();
    void setPosition(Vector3D position);

}
