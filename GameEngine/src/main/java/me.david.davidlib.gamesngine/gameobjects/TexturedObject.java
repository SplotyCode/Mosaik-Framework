package me.david.davidlib.gamesngine.gameobjects;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.gamesngine.model.Model;
import me.david.davidlib.math.Vector3D;

public abstract class TexturedObject implements CollidatebleGameObject {

    @Getter @Setter private int id;
    @Getter @Setter private Vector3D position = new Vector3D(0, 0, 0);

    private Model model;
    private Vector3D rotation;

    public TexturedObject(Vector3D position, Model model, Vector3D rotation) {
        this.position = position;
        this.model = model;
        this.rotation = rotation;
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }
}
