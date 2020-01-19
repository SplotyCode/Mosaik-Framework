package io.github.splotycode.mosaik.webapi.handler;

import io.github.splotycode.mosaik.util.collection.MaxSizeHashMap;
import io.github.splotycode.mosaik.webapi.handler.anotation.IllegalHandlerException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
public class UrlPattern {

    private MaxSizeHashMap<String, MatchResult> cachedResults = new MaxSizeHashMap<>(25);

    public static String simplify(String url) {
        if (url.length() > 1) {
            while (url.startsWith("/") && url.length() > 1) {
                url = url.substring(1);
            }
            while (url.endsWith("/") && url.length() > 1) {
                url = url.substring(0, url.length() - 1);
            }
        }
        return url;
    }

    String[] patternSplit;
    private boolean base, all;
    private boolean ignoreBegin, ignoreEnd;
    private boolean hasVariables;
    private String[] variables;

    public UrlPattern(String pattern) throws IllegalHandlerException {
        pattern = simplify(pattern);
        patternSplit = pattern.split("/", -1);
        base = pattern.equals("/");
        all = pattern.equals("*");

        int length = patternSplit.length;
        variables = new String[length];

        for (int i = 0; i < length; i++) {
            String path = patternSplit[i];
            if (path.startsWith("?") && path.endsWith("?")) {
                variables[i] = path.substring(1).substring(0, path.length() - 2);
                hasVariables = true;
            } else if (path.equals("**")) {
                if (i == 0) {
                    ignoreBegin = true;
                } else if (i == length - 1) {
                    ignoreEnd = true;
                } else {
                    throw new IllegalHandlerException("** is only allowed as first or last element");
                }
            }
        }

        if (ignoreBegin) {
            patternSplit = Arrays.copyOfRange(patternSplit, 1, patternSplit.length);
        }
        if (ignoreEnd) {
            patternSplit = Arrays.copyOf(patternSplit, patternSplit.length - 1);
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

        MatchResult result = match0(url);
        cachedResults.put(url, result);
        return result;
    }

    protected MatchResult match0(String url) {
        String[] currentSplit = url.split("/");
        int currentLength = currentSplit.length;

        int skip = 0;
        if (ignoreBegin) {
            String start = patternSplit[0];
            while (skip < currentLength - 1 && !start.equals(currentSplit[skip])) {
                skip++;
            }
        }

        Map<String, String> currentVariables = hasVariables ? new HashMap<>() : Collections.emptyMap();

        int length = patternSplit.length + skip;

        /* Check for too many elements  */
        if (!ignoreEnd && length < currentLength) {
            return EMPTY_FALSE_RESULT;
        }

        /* Check for too few elements */
        if (length > currentLength) {
            return EMPTY_FALSE_RESULT;
        }

        for (int i = skip; i < length; i++) {
            String path = currentSplit[i];
            String variable = variables[i - skip];
            if (variable != null) {
                currentVariables.put(variable, path);
            } else {
                String pattern = patternSplit[i - skip];
                if (!path.equals(pattern) && !pattern.equals("*")) {
                    return EMPTY_FALSE_RESULT;
                }
            }
        }

        return new MatchResult(true, currentVariables);
    }

    private static final MatchResult EMPTY_TRUE_RESULT = new MatchResult(true, Collections.emptyMap());
    private static final MatchResult EMPTY_FALSE_RESULT = new MatchResult(false, Collections.emptyMap());

    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class MatchResult {

        private final boolean match;
        private Map<String, String> variables;

    }

}
