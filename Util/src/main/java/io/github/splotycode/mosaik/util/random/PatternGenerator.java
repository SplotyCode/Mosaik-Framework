package io.github.splotycode.mosaik.util.random;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

/**
 * Tool to generate patterns
 * Pin format with default values: xxxx-xxxx-xxxx-xxxx-xxxx-xxxx
 */
@AllArgsConstructor
public class PatternGenerator {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    @Getter private static final PatternGenerator instance = new PatternGenerator();

    private int groups;
    private int digits;
    private Random random;

    public PatternGenerator() {
        this(6, 4);
    }

    public PatternGenerator(int groups, int digits) {
        this(groups, digits, new Random());
    }

    /**
     * Specifies the amount of groups
     */
    public PatternGenerator groups(int groups) {
        this.groups = groups;
        return this;
    }

    /**
     * Specifies the amount of digits in one group
     */
    public PatternGenerator digits(int digits) {
        this.digits = digits;
        return this;
    }

    /**
     * Builds the randomised pattern
     */
    public final String build() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.delete(0, stringBuilder.length());
        for (int i = 0; i < this.groups * this.digits; i++) {
            stringBuilder.append(CHARS.charAt(random.nextInt(CHARS.length())));
            if ((i + 1) % digits == 0 && (i != (groups * digits) - 1)) {
                stringBuilder.append("-");
            }
        }
        return stringBuilder.toString();
    }

}
