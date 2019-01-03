package de.splotycode.davidlib.argparser.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.runtimeapi.argparser.Parameter;

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