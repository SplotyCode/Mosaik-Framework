package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern.PatternCommand;

public interface ResponseManipulator<R extends ResponseManipulator> {

    R setCashing(boolean cashing);
    boolean isCashing();

    R variable(String str, Object obj);
    R object(Object object);

    R pattern(String name, Object object);
    R pattern(Object object);
    R pattern(PatternCommand command);

    R patternListName(String name, Iterable<?> objects);
    R patternList(Iterable<?> objects);

    R patternArrayName(String name, Object... objects);
    R patternArray(Object... objects);

    R patternCostomName(String name, Pair<String, Object>... values);
    R patternCostomWithObj(String name, Object main, Pair<String, Object>... values);
    R patternCostomWithObj(Object main, Pair<String, Object>... values);

}
