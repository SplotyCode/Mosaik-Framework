package io.github.splotycode.mosaik.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * General Utils for Object's
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectUtil {

    /**
     * Checks if two Objects have the same.
     * This works for Arrays, Collection and CharSequences were equals() often don't works
     */
    public static <T> boolean areSame(T o1, T o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return Arrays.deepEquals((Object[]) o1, (Object[]) o2);
        }
        if (o1 instanceof CharSequence && o2 instanceof CharSequence) {
            CharSequence s1 = (CharSequence) o1, s2 = (CharSequence) o2;
            if (s1.length() != s2.length()) return false;

            for (int i = 0; i < s1.length(); i++) {
                char c1 = s1.charAt(i);
                char c2 = s2.charAt(i);
                if (c1 != c2) {
                    return false;
                }
            }
            return true;
        }
        if (o1 instanceof Collection && o2 instanceof Collection) {
            Collection c1 = (Collection) o1, c2 = (Collection) o2;
            if (c1.size() != c2.size()) {
                return false;
            }

            Set c1Set = new HashSet<Object>(c1);
            for (Object t : c2) {
                if (!c1Set.contains(t)) {
                    return false;
                }
            }
            return true;
        }
        return o1.equals(o2);
    }

}
