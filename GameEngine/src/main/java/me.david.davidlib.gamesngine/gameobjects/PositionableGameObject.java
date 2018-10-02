package me.david.davidlib.gamesngine.gameobjects;

import me.david.davidlib.math.Vector3D;

public interface PositionableGameObject extends GameObject {

    Vector3D getPosition();
    void setPosition(Vector3D position);

}
