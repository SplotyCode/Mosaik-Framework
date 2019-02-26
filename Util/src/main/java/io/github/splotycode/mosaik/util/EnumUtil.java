package io.github.splotycode.mosaik.util;

public final class EnumUtil {

    public static String toDisplayName(Enum enumm) {
        return StringUtil.camelCase(enumm.name().replace('_', '-'));
    }

}
