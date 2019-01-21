package io.github.splotycode.mosaik.argparserimpl;

import io.github.splotycode.mosaik.argparser.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
@AllArgsConstructor
public class Argument {

    private Field field;
    private Parameter parameter;

    public String getName() {
        return parameter.name();
    }

}