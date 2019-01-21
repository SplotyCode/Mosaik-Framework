package io.github.splotycode.mosaik.webapi.handler;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import io.github.splotycode.mosaik.util.cache.MaxSizeHashMap;
import io.github.splotycode.mosaik.webapi.handler.anotation.IllegalHandlerException;

import java.util.*;

@EqualsAndHashCode
public class UrlPattern {

    private MaxSizeHashMap<String, MatchResult> cachedResults = new MaxSizeHashMap<>(25);

    private static String simplify(String url) {
        if (url.length() > 1) {
            while (url.startsWith("/")) {
                url = url.substring(1);
            }
            while (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
        }
        return url;
    }

    String[] split;
    private boolean base = false, all = false;
    private boolean ignoreBegin = false, ignoreEnd = false;
    private Map<Integer, String> variables = new HashMap<>();

    public UrlPattern(String pattern) {
        pattern = simplify(pattern);
        split = pattern.split("/");
        base = pattern.equals("/");
        all = pattern.equals("*");
        boolean first = true;
        int i = 0;
        for (String path : split) {
            if (path.startsWith("?") && path.endsWith("?")) {
                variables.put(i, path.substring(1).substring(0, path.length() - 2));
            }
            if (first) {
                ignoreBegin = path.equals("**");
                first = false;
            } else if (i == split.length - 1) {
                ignoreEnd = path.equals("**");
            } else if (path.equals("**")) {
                throw new IllegalHandlerException("** is only aloud as first or last path");
            }
            i++;
        }
        if (ignoreBegin) {
            split = Arrays.copyOfRange(split, 1, split.length);
        }
        if (ignoreEnd) {
            split = Arrays.copyOf(split, split.length-1);
        }
    }

    public MatchResult match(String url) {
        /* Base(/) and All(*) Operator */
        if (url.equals("/")) return base ? EMPTY_TRUE_RESULT : EMPTY_FALSE_RESULT;
        if (all) return EMPTY_TRUE_RESULT;

        /* Simplify Url */
        url = simplify(url);

        /* Use Cache if present */
        MatchResult cached = cachedResults.get(url);
        if (cached != null) {
            return cached;
        }

        String[] pathSplit = url.split("/");
        int skip = 0;
        if (ignoreBegin) {
            while (skip < pathSplit.length - 1 && !split[0].equals(pathSplit[skip])) {
                skip++;
            }
        }

        Map<String, String> currentVariables = new HashMap<>();

        int i = 0;
        boolean badEnding = false;
        for (String path : pathSplit) {
            if (i < skip) {
                i++;
                continue;
            }
            if (i - skip >= split.length) {
                badEnding = true;
                break;
            }
            if (variables.containsKey(i - skip)) {
                currentVariables.put(variables.get(i - skip), path);
            } else {
                String pattern = split[i - skip];
                if (!path.equals(pattern) && !pattern.equals("*")) {
                    cachedResults.put(url, EMPTY_FALSE_RESULT);
                    return EMPTY_FALSE_RESULT;
                }
            }
            i++;
        }
        if (badEnding && !ignoreEnd) {
            cachedResults.put(url, EMPTY_FALSE_RESULT);
            return EMPTY_FALSE_RESULT;
        }
        MatchResult result = new MatchResult(true);
        result.variables = currentVariables;
        cachedResults.put(url, result);
        return result;
    }

    private static final MatchResult EMPTY_TRUE_RESULT = new MatchResult(true);
    private static final MatchResult EMPTY_FALSE_RESULT = new MatchResult(false);

    @Getter
    @EqualsAndHashCode
    public static class MatchResult {

        private final boolean match;
        private Map<String, String> variables;

        public MatchResult(boolean match) {
            this.match = match;
        }
    }

}
