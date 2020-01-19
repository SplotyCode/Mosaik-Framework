package io.github.splotycode.mosaik.util.info;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@ToString
public class JavaVersion implements Comparable<JavaVersion> {

    private final int feature, minor, update, build;

    private static JavaVersion current = null;

    public static JavaVersion current() {
        if (current == null) {
            current = parse(SystemInfo.JAVA_RUNTIME_VERSION);
        }
        return current;
    }

    public static JavaVersion parse(String version) {
        String str = version.trim();
        if (str.contains("Runtime Environment")) {
            int p = str.indexOf("(build ");
            if (p > 0) str = str.substring(p);
        }

        List<String> separators = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        int length = str.length(), p = 0;
        boolean number = false;
        while (p < length) {
            int start = p;
            while (p < length && Character.isDigit(str.charAt(p)) == number) p++;
            String part = str.substring(start, p);
            (number ? numbers : separators).add(part);
            number = !number;
        }

        if (!numbers.isEmpty() && !separators.isEmpty()) {
            int feature = Integer.parseInt(numbers.get(0)), minor = 0, update = 0, build = 0;
            p = 1;
            while (p < separators.size() && ".".equals(separators.get(p))) p++;
            if (p > 1 && numbers.size() > 2) {
                minor = Integer.parseInt(numbers.get(1));
                update = Integer.parseInt(numbers.get(2));
            }
            if (p < separators.size()) {
                String s = separators.get(p);
                if (s.startsWith("-")) {
                    if (p < numbers.size() && s.endsWith("+")) {
                        build = Integer.parseInt(numbers.get(p));
                    }
                    p++;
                }
                if (build == 0 && p < separators.size() && p < numbers.size() && "+".equals(separators.get(p))) {
                    build = Integer.parseInt(numbers.get(p));
                }
            }
            return new JavaVersion(feature, minor, update, build);
        }
        return null;
    }

    @Override
    public int compareTo(JavaVersion other) {
        int diff = feature - other.feature;
        if (diff != 0) return diff;
        diff = minor - other.minor;
        if (diff != 0) return diff;
        diff = update - other.update;
        if (diff != 0) return diff;
        diff = build - other.build;
        if (diff != 0) return diff;
        return 0;
    }


    public boolean isAtLeast(int feature) {
        return this.feature >= feature;
    }

}
