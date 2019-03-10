package io.github.splotycode.mosaik.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtil {

    public static String toDisplayName(Enum enumm) {
        return StringUtil.camelCase(enumm.name().replace('_', '-'), "-");
    }

}
