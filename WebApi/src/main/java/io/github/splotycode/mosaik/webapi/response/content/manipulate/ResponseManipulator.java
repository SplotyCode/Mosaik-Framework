package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern.PatternCommand;

public interface ResponseManipulator {

    ResponseManipulator variable(String str, Object obj);
    ResponseManipulator object(Object object);

    ResponseManipulator pattern(String name, Object object);
    ResponseManipulator pattern(Object object);
    ResponseManipulator pattern(PatternCommand command);

    ResponseManipulator patternListName(String name, Iterable<?> objects);
    ResponseManipulator patternList(Iterable<?> objects);

    ResponseManipulator patternArrayName(String name, Object... objects);
    ResponseManipulator patternArray(Object... objects);

    ResponseManipulator patternCostomName(String name, Pair<String, Object>... values);
    ResponseManipulator patternCostomWithObj(String name, Object main, Pair<String, Object>... values);
    ResponseManipulator patternCostomWithObj(Object main, Pair<String, Object>... values);

}
