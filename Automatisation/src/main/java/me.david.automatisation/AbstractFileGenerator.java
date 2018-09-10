package me.david.automatisation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public abstract class AbstractFileGenerator implements Generator<String> {

    public AbstractFileGenerator(String file) {
        Path path = null;
        try {
            path = Paths.get(this.getClass().getClassLoader().getResource(file).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.path = path;
    }

    private final Path path;
    private long count = -1;

    @Override
    public String getRandom() {
        try {
            try (Stream<String> lines = Files.lines(path)) {
                if (count == -1) {
                    count = lines.count();
                }
                return lines.skip(ThreadLocalRandom.current().nextLong(0, count)).findFirst().get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
