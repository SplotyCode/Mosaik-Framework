package io.github.splotycode.mosaik.valuetransformer.exception;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.i18n.I18N;
import io.github.splotycode.mosaik.valuetransformer.CommonData;

import java.text.MessageFormat;

public class TransformException extends RuntimeException {

    public static TransformException createTranslated(DataFactory data, String key, String def, Object... objects) {
        return new TransformException(getMessage(data, key, def, objects));
    }

    public static TransformException createTranslated(DataFactory data, String key, String def, Throwable cause, Object... objects) {
        return new TransformException(getMessage(data, key, def, objects), cause);
    }

    private static String getMessage(DataFactory data, String key, String def, Object... objects) {
        I18N translation = data.getDataDefault(CommonData.I18N);
        key = data.getDataDefault(CommonData.TRANSLATION_PREFIX, "") + key;
        return translation == null ? new MessageFormat(def).format(objects) : translation.getOrDefault(key, def, objects);
    }

    public TransformException() {
    }

    public TransformException(String s) {
        super(s);
    }

    public TransformException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TransformException(Throwable throwable) {
        super(throwable);
    }

    public TransformException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
