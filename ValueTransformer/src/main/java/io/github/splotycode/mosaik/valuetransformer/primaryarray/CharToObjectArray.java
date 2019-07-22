package io.github.splotycode.mosaik.valuetransformer.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class CharToObjectArray extends ValueTransformer<char[], Character[]> {

    @Override
    public Character[] transform(char[] input, DataFactory info) throws Exception {
        return ArrayUtil.toObject(input);
    }
}
