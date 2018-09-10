package me.david.automatisation.name;

import lombok.Getter;
import me.david.automatisation.AbstractFileGenerator;

public class FirstNameGenerator extends AbstractFileGenerator {

    @Getter private static FirstNameGenerator instance = new FirstNameGenerator();

    private FirstNameGenerator() {
        super("/firstnames.txt");
    }

}
