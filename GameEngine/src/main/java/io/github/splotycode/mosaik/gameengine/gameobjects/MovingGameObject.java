package io.github.splotycode.mosaik.gameengine.gameobjects;

import io.github.splotycode.mosaik.util.math.Vector3D;

public interface MovingGameObject extends CollidatebleGameObject {

    Vector3D getMotion();
    void setMotion(Vector3D motion);

}
