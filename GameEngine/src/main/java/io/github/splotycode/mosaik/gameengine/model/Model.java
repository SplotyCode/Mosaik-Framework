package io.github.splotycode.mosaik.gameengine.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Model {

    private RawModel rawModel;
    private ModelTexture modelTexture;

}
