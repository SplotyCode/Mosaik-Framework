package de.splotycode.davidlib.argparserimpl;

import de.splotycode.davidlib.argparser.Parameter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.david.davidlib.util.reflection.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParsedObject {

    private Set<Argument> parameter = new HashSet<>();

    public Collection<Argument> getAll() {
        return parameter;
    }

    public static ParsedObject parse(Object object) {
        ParsedObject parsed = new ParsedObject();

        for (Field field : ReflectionUtil.getAllFields(object.getClass())) {
            if (field.isAnnotationPresent(Parameter.class)) {
                field.setAccessible(true);
                parsed.parameter.add(new Argument(field, field.getAnnotation(Parameter.class)));
            }
        }
        return parsed;
    }

}
