package io.github.splotycode.mosaik.valuetransformer.other;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.CommonData;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

import java.lang.reflect.Method;
import java.util.Objects;

public class StringToEnum extends ValueTransformer<String, Enum> {

    @Override
    @SuppressWarnings("unchecked")
    public Enum transform(String input, DataFactory info) throws Exception {
        Class clazz = info.getData(CommonData.RESULT);
        try {
            return Enum.valueOf(clazz, input.toUpperCase());
        } catch (IllegalArgumentException ex) {
            Method values = clazz.getMethod("values");
            values.setAccessible(true);
            Object[] constants = (Object[]) values.invoke(null);
            String constantString = StringUtil.join(constants, Objects::toString);
            throw TransformException.createTranslated(info, "string_to_enum", "{0} is not a valid enum constant! Valid constants: {1}", ex, input, input, constantString);
        }
    }

}
