package io.github.splotycode.mosaik.automatisation.generators;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.io.IOUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is an generator that gives out random lines from a text file
 */
public abstract class AbstractFileGenerator implements Generator<String> {

    private final Path path;
    private long count = -1;
    private List<String> cache;
    private boolean shouldCache;

    public AbstractFileGenerator(String file) {
        this(file, false);
    }

    public AbstractFileGenerator(String file, boolean shouldCache) {
        this.shouldCache = shouldCache;
        path = IOUtil.getResourcePath("/res/automatisation/" + file);
    }

    /**
     * Gets an random line
     * @return the random line
     */
    @Override
    public String getRandom() {
        try {
            if (shouldCache) {
                if (cache == null) {
                    cache = Files.lines(path).collect(Collectors.toList());
                }
                return cache.get(ThreadLocalRandom.current().nextInt(0, cache.size()));
            } else {
                if (count == -1) count = Files.lines(path).count();
                try (Stream<String> lines = Files.lines(path)) {
                    return lines.skip(ThreadLocalRandom.current().nextLong(0, count)).findFirst().orElse(null);
                }
            }
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
        }
        return null;
    }

}
