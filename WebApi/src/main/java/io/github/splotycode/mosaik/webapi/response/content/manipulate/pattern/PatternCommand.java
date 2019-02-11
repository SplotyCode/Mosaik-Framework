package io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern;

import io.github.splotycode.mosaik.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class PatternCommand {

    public static PatternCommand create() {
        return new PatternCommand();
    }

    public static PatternCommand create(String name) {
        PatternCommand command = new PatternCommand();
        command.name = name;
        return command;
    }

    public static PatternCommand create(Object object) {
        PatternCommand command = new PatternCommand();
        command.name = object.getClass().getSimpleName();
        command.addObject(object);
        return command;
    }

    private PatternCommand() {}

    @Setter private PatternCommand parent = null;
    private String name;
    PatternAction primary = null;
    private List<PatternAction> secondaries = new ArrayList<>();

    public PatternAction getPrimary(boolean force) {
        if (primary == null && force) {
            primary = new PatternAction(this);
        }
        return primary;
    }

    public PatternAction createSecondary() {
        PatternAction secondary = new PatternAction(this);
        secondaries.add(secondary);
        return secondary;
    }

    public <T> PatternCommand createSecondaries(Consumer<Pair<T, PatternAction>> func, T... objects) {
        for (T object : objects) {
            PatternAction action = createSecondary();
            action.addObject(object);
            func.accept(new Pair<>(object, action));
        }
        return this;
    }

    public <T> PatternCommand createSecondaries(Consumer<Pair<T, PatternAction>> func, Iterable<T> objects) {
        for (T object : objects) {
            PatternAction action = createSecondary();
            action.addObject(object);
            func.accept(new Pair<>(object, action));
        }
        return this;
    }

    public PatternCommand setName(String name) {
        this.name = name;
        return this;
    }

    public PatternCommand addObject(Object object) {
        getPrimary(true).addObject(object);
        return this;
    }

    public PatternCommand addObjects(Object... objects) {
        getPrimary(true).addObjects(objects);
        return this;
    }

    public PatternCommand addObjects(Iterable<Object> objects) {
        getPrimary(true).addObjects(objects);
        return this;
    }

    public PatternCommand addCostom(String key, Object value) {
        getPrimary(true).addCostom(key, value);
        return this;
    }

    public PatternCommand addCostom(Pair<String, Object>... values) {
        getPrimary(true).addCostom(values);
        return this;
    }

    public PatternCommand createChild() {
        return getPrimary(true).createChild();
    }


    public PatternCommand createChild(String name) {
        return getPrimary(true).createChild();
    }

    public PatternCommand createChild(Object object) {
        return getPrimary(true).createChild(object);
    }

}
