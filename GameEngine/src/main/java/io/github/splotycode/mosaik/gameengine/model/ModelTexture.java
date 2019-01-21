package io.github.splotycode.mosaik.gameengine.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
public class ModelTexture {

    private int textureID;

    @Setter private float shineDamper = 1;
    @Setter private float reflectivity = 0;

    public ModelTexture(int textureID) {
        this.textureID = textureID;
    }

}
