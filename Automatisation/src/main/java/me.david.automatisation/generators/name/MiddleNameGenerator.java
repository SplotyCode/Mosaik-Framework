package me.david.automatisation.generators.name;

import me.david.automatisation.generators.AbstractFileGenerator;

/**
 * Used for generating Middle names
 */
public class MiddleNameGenerator extends AbstractFileGenerator {

    public MiddleNameGenerator() {
        super("/middlenames.txt");
    }

    /**
     * Gets an Random Middle Name
     * @return a middle name
     */
    @Override
    public String getRandom() {
        return super.getRandom();
    }

}
