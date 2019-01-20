package de.splotycode.davidlib.argparserimpl;

import de.splotycode.davidlib.argparser.Parameter;
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