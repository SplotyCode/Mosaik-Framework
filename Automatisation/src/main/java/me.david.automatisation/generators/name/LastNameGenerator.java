package me.david.automatisation.generators.name;

import me.david.automatisation.generators.AbstractFileGenerator;

/**
 * Used for generating Last names
 */
public class LastNameGenerator extends AbstractFileGenerator {

    public LastNameGenerator() {
        super("/lastnames.txt");
    }

    public LastNameGenerator(boolean shouldCache) {
        super("/lastnames.txt", shouldCache);
    }

    /**
     * Gets an Random Last Name
     * @return a last name
     */
    @Override
    public String getRandom() {
        return super.getRandom();
    }


}
