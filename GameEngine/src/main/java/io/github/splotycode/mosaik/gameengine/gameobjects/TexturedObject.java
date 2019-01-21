package io.github.splotycode.mosaik.gameengine.gameobjects;

import lombok.Getter;
import lombok.Setter;
import io.github.splotycode.mosaik.gameengine.model.Model;
import io.github.splotycode.mosaik.util.math.Vector3D;

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
