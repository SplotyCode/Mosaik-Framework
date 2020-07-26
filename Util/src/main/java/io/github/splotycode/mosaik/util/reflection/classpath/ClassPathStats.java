package io.github.splotycode.mosaik.util.reflection.classpath;

import io.github.splotycode.mosaik.util.cache.LazyLoad;
import io.github.splotycode.mosaik.util.io.resource.ClassPathResource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassPathStats {
    @Getter private final ClassPath classPath;
    private LazyLoad<Long> totalCount = LazyLoad.fromResolver(cache -> computeTotalCount(false));
    private LazyLoad<Long> totalClassCount = LazyLoad.fromResolver(cache -> computeTotalCount(true));

    private long computeTotalCount(boolean onlyClasses) {
        if (onlyClasses) {
            return classPath.resources.values().stream().filter(ClassPathResource::isClass).count();
        }
        return classPath.resources.size();
    }

    void reset() {
        totalCount.clear();
        totalClassCount.clear();
    }
}
