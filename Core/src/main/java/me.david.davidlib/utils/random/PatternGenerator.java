package me.david.davidlib.utils.random;

import lombok.Getter;

import java.util.Random;

public class PatternGenerator {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    @Getter private static final PatternGenerator instance = new PatternGenerator();

    private int groups;
    private int digits;
    private String pin;
    private Random random = new Random();
    private StringBuilder stringBuilder = new StringBuilder();

    public PatternGenerator() {
        this.groups = 6;
        this.digits = 4;
        this.pin = generate();
    }

    public PatternGenerator groups(int groups) {
        this.groups = groups;
        return this;
    }

    public PatternGenerator digits(int digits) {
        this.digits = digits;
        return this;
    }

    public final String build() {
        this.pin = generate();
        return this.pin;
    }

    private String generate() {
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
