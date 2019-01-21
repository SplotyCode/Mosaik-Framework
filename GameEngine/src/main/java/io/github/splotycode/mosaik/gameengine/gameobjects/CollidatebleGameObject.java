package io.github.splotycode.mosaik.gameengine.gameobjects;

import io.github.splotycode.mosaik.gameengine.render.World;

import java.util.List;

public interface CollidatebleGameObject extends PositionableGameObject {

    List<BoundingArea> getBoundingAreas();
    BoundingArea getOverAllBoundingArea();

    List<CollidatebleGameObject> getCollidatingObjects(World world);


}
