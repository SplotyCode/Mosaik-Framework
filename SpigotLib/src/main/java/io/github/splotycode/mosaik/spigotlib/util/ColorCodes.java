package io.github.splotycode.mosaik.spigotlib.util;

public enum  ColorCodes {

    WHITE(0, "white"),
    ORANGE(1, "orange"),
    MAGENTA(2, "magenta"),
    LIGHT_BLUE(3, "light_blue"),
    YELLOW(4, "yellow"),
    LIME(5, "lime"),
    PINK(6, "pink"),
    GRAY(7, "gray"),
    LIGHT_GRAY(8, "light_gray"),
    CYAN(9, "cyan"),
    PURPLE(10, "purple"),
    BLUE(11, "light_blue"),
    BROWN(12, "brown"),
    GREEN(13, "green"),
    RED(14, "red"),
    BLACK(15, "black");

    private final int id;
    private final String color;

    ColorCodes(int id, String color) {
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }
}
