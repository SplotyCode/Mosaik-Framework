package me.david.automatisation.generators;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Path path = null;
        try {
            path = Paths.get(getClass().getClassLoader().getResource(file).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.path = path;
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
                try (Stream<String> lines = Files.lines(path)) {
                    if (count == -1) {
                        count = lines.count();
                    }
                    return lines.skip(ThreadLocalRandom.current().nextLong(0, count)).findFirst().orElse(null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
