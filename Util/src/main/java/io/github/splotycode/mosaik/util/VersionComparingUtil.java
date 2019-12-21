package io.github.splotycode.mosaik.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.OptionalInt;

/**
 * Util to compare version numbers
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VersionComparingUtil {

    /**
     * Compares two version numbers.
     * @return the value {@code 0} if the versions are equal.
     *      A value less than {@code 0} if v2 is newer then v1 and a
     *      value greater than {@code 0} if v1 greater than v2.
     */
    public static int compare(String v1, String v2) {
        String[] part1 = v1.split("[._\\-]");
        String[] part2 = v2.split("[._\\-]");

        int idx = 0;
        for (; idx < part1.length && idx < part2.length; idx++) {
            String p1 = part1[idx];
            String p2 = part2[idx];

            int result;
            OptionalInt int1 = StringUtil.parseInteger(p1);
            OptionalInt int2 = StringUtil.parseInteger(p2);

            if (int1.isPresent() && int2.isPresent()) {
                result = Integer.compare(int1.getAsInt(), int2.getAsInt());
            } else {
                result = p1.compareTo(p2);
            }

            if (result != 0) return result;
        }

        if (part1.length != part2.length) {
            boolean left = part1.length > idx;
            String[] parts = left ? part1 : part2;

            for (String part : parts) {
                int cmp = 1;
                OptionalInt parsed = StringUtil.parseInteger(part);
                if (parsed.isPresent()) {
                    cmp = Integer.compare(parsed.getAsInt(), 0);
                }
                if (cmp != 0) return left ? cmp : -cmp;
            }
        }
        return 0;
    }

}
