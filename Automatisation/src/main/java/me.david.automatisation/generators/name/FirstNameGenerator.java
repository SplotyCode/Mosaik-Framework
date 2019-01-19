package me.david.automatisation.generators.name;

import lombok.Getter;
import me.david.automatisation.generators.AbstractFileGenerator;

/**
 * Used for generating First names
 */
public class FirstNameGenerator extends AbstractFileGenerator {

    @Getter private static FirstNameGenerator instance = new FirstNameGenerator();

    private FirstNameGenerator() {
        super("firstnames.txt");
    }

    public FirstNameGenerator(boolean shouldCache) {
        super("firstnames.txt", shouldCache);
    }

    /**
     * Gets an Random First Name
     * @return a first name
     */
    @Override
    public String getRandom() {
        return super.getRandom();
    }

}
