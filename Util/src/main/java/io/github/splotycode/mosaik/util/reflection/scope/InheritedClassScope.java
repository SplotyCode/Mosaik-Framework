package io.github.splotycode.mosaik.util.reflection.scope;

public class InheritedClassScope extends DefaultClassScope {

    protected ClassScope inherited;

    public InheritedClassScope(Type type, ClassScope inherited) {
        super(type);
        this.inherited = inherited;
    }

    @Override
    public boolean test(String path) {
        boolean result = super.test(path);
        if (result) return true;
        return inherited.test(path);
    }
}
