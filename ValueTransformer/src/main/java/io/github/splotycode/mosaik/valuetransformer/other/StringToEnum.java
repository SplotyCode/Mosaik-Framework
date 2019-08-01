package io.github.splotycode.mosaik.valuetransformer.other;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.CommonData;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

public class StringToEnum extends ValueTransformer<String, Enum> {

    @Override
    @SuppressWarnings("unchecked")
    public Enum transform(String input, DataFactory info) throws Exception {
        Class clazz = info.getData(CommonData.RESULT);
        Object[] constants = clazz.getEnumConstants();

        try {
            int ordinal = Integer.parseInt(input);
            return (Enum) constants[ordinal];
        } catch (NumberFormatException ignored) {}
        try {
            return Enum.valueOf(clazz, input.toUpperCase());
        } catch (IllegalArgumentException ex) {
            String constantString = StringUtil.join(constants, obj -> {
                Enum enumm = (Enum) obj;
                return enumm.name() + " (" + enumm.ordinal() + ")";
            });
            throw TransformException.createTranslated(info, "string_to_enum", "{0} is not a valid enum constant! Valid constants: {1}", ex, input, input, constantString);
        }
    }

}
