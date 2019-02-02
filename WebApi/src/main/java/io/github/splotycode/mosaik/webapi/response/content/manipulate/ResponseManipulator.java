package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.util.Pair;

public interface ResponseManipulator {

    ResponseManipulator variable(String str, Object obj);
    ResponseManipulator object(Object object);

    ResponseManipulator pattern(String name, Object object);
    ResponseManipulator pattern(Object object);

    ResponseManipulator patternList(String name, Iterable<?> objects);
    ResponseManipulator patternList(Iterable<?> objects);

    ResponseManipulator patternArray(String name, String... objects);
    ResponseManipulator patternArray(Object... objects);

    ResponseManipulator patternCostom(String name, Pair<String, Object>... values);
    ResponseManipulator patternCostom(String name, Object main, Pair<String, Object>... values);
    ResponseManipulator patternCostom(Object main, Pair<String, Object>... values);

}
