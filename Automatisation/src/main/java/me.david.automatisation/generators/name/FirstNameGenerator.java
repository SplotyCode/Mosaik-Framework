package me.david.automatisation.generators.name;

import lombok.Getter;
import me.david.automatisation.generators.AbstractFileGenerator;

public class FirstNameGenerator extends AbstractFileGenerator {

    @Getter private static FirstNameGenerator instance = new FirstNameGenerator();

    private FirstNameGenerator() {
        super("/firstnames.txt");
    }

}
