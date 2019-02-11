package io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern;

import io.github.splotycode.mosaik.util.Pair;
import lombok.Getter;

import java.util.*;

@Getter
public class PatternAction {

    private PatternCommand command;

    List<Object> objects = new ArrayList<>();
    private Map<String, Object> costom = new HashMap<>();

    List<PatternCommand> childs = new ArrayList<>();

    public PatternAction(PatternCommand command) {
        this.command = command;
    }

    public PatternAction addObject(Object object) {
        objects.add(object);
        return this;
    }

    public PatternAction addObjects(Object... objects) {
        this.objects.addAll(Arrays.asList(objects));
        return this;
    }

    public PatternAction addObjects(Iterable<Object> objects) {
        for (Object object : objects) {
            this.objects.add(object);
        }
        return this;
    }

    public PatternAction addCostom(String key, Object value) {
        costom.put(key, value);
        return this;
    }

    public PatternAction addCostom(Pair<String, Object>... values) {
        for (Pair<String, Object> value : values) {
            costom.put(value.getOne(), value.getTwo());
        }
        return this;
    }

    public PatternCommand createChild() {
        PatternCommand child = PatternCommand.create();
        child.setParent(command);
        childs.add(child);
        return child;
    }

    public PatternCommand createChild(String name) {
        PatternCommand child = PatternCommand.create(name);
        child.setParent(command);
        childs.add(child);
        return child;
    }

    public PatternCommand createChild(Object object) {
        PatternCommand child = PatternCommand.create(object);
        child.setParent(command);
        childs.add(child);
        return child;
    }

}
