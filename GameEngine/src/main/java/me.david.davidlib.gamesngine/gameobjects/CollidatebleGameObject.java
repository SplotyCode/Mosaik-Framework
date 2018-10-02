package me.david.davidlib.gamesngine.gameobjects;

import me.david.davidlib.gamesngine.render.World;

import java.util.List;

public interface CollidatebleGameObject extends PositionableGameObject {

    List<BoundingArea> getBoundingAreas();
    BoundingArea getOverAllBoundingArea();

    List<CollidatebleGameObject> getCollidatingObjects(World world);


}
