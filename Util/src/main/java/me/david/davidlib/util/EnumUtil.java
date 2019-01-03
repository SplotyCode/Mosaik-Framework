package me.david.davidlib.util;

public final class EnumUtil {

    public static String toDisplayName(Enum enumm) {
        return enumm.name().toLowerCase().replace('_', '-');
    }

}
