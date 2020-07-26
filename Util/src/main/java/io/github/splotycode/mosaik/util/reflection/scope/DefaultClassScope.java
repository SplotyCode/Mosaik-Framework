package io.github.splotycode.mosaik.util.reflection.scope;

import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.util.reflection.classpath.VisitorAction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.TreeSet;

@Getter
@RequiredArgsConstructor
public class DefaultClassScope implements ClassScope {
    public static DefaultClassScope createWhitelist() {
        return new DefaultClassScope(Type.WHITELIST);
    }

    public static DefaultClassScope createBlacklist() {
        return new DefaultClassScope(Type.BLACKLIST);
    }

    public enum Type {
        WHITELIST,
        BLACKLIST
    }

    private final Type type;
    private final TreeSet<String> ignored = new TreeSet<>();
    private final TreeSet<String> paths = new TreeSet<>();

    @Override
    public VisitorAction visitPackage(String packagePath) {
        return test(packagePath) ? VisitorAction.CONTINUE : VisitorAction.SKIP;
    }

    @Override
    public VisitorAction visitResource(String fullName) {
        return test(fullName) ? VisitorAction.CONTINUE : VisitorAction.SKIP;
    }

    @Override
    public boolean test(Class<?> clazz) {
        return test(clazz.getName());
    }

    @Override
    public boolean test(String path) {
        if (type == Type.WHITELIST) {
            if (CollectionUtil.startsWith(ignored, path)) {
                return false;
            }
            return CollectionUtil.startsWith(paths, path);
        }
        /* BLACKLIST */
        if (CollectionUtil.startsWith(paths, path)) {
            return true;
        }
        return !CollectionUtil.startsWith(ignored, path);
    }

    @Override
    public void ignorePath(String path) {
        ignored.add(path);
    }

    @Override
    public void addPath(String path) {
        paths.add(path);
    }
}
