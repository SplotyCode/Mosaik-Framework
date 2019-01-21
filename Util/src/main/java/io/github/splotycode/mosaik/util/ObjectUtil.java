package io.github.splotycode.mosaik.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class ObjectUtil {

    public static <T> boolean areSame(T o1, T o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[])o1,  (Object[])o2);
        }
        if (o1 instanceof CharSequence && o2 instanceof CharSequence) {
            CharSequence s1 = (CharSequence) o1, s2 = (CharSequence) o2;
            if (s1.length() != s2.length()) return false;
            int to = 0;
            int po = 0;
            int len = s1.length();

            while (len-- > 0) {
                char c1 = s1.charAt(to++);
                char c2 = s2.charAt(po++);
                if (c1 == c2) {
                    continue;
                }
                return false;
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
