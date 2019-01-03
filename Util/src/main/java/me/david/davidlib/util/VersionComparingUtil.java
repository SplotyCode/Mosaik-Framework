package me.david.davidlib.util;

public final class VersionComparingUtil {

    public static int compare(String v1, String v2) {
        String[] part1 = v1.split("[._\\-]");
        String[] part2 = v2.split("[._\\-]");

        int idx = 0;
        for (; idx < part1.length && idx < part2.length; idx++) {
            String p1 = part1[idx];
            String p2 = part2[idx];

            int cmp;
            if (p1.matches("\\d+") && p2.matches("\\d+")) {
                cmp = new Integer(p1).compareTo(new Integer(p2));
            }
            else {
                cmp = part1[idx].compareTo(part2[idx]);
            }
            if (cmp != 0) return cmp;
        }

        if (part1.length != part2.length) {
            boolean left = part1.length > idx;
            String[] parts = left ? part1 : part2;

            for (; idx < parts.length; idx++) {
                String p = parts[idx];
                int cmp;
                if (p.matches("\\d+")) {
                    cmp = new Integer(p).compareTo(0);
                }
                else {
                    cmp = 1;
                }
                if (cmp != 0) return left ? cmp : -cmp;
            }
        }
        return 0;
    }

}
