package me.david.davidlib.gamesngine.gameobjects;

import me.david.davidlib.math.Vector3D;

public interface MovingGameObject extends CollidatebleGameObject {

    Vector3D getMotion();
    void setMotion(Vector3D motion);

}
