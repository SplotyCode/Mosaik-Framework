package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.i18n.I18N;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.nio.charset.Charset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonData {

    public static final DataKey<Class> RESULT = new DataKey<>("result");
    public static final DataKey<Charset> CHARSET = new DataKey<>("charset");
    public static final DataKey<File> BASE_PATH = new DataKey<>("base_path");

    public static final DataKey<I18N> I18N = new DataKey<>("translation.i18n");

    public static final DataKey<String> TRANSLATION_PREFIX = new DataKey<>("translation.prefix");

}
