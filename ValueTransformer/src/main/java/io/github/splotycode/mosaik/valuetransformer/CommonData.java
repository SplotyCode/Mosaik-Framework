package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.util.datafactory.DataKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.nio.charset.Charset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonData {

    public static final DataKey<Class> RESULT = new DataKey<>("result");
    public static final DataKey<Charset> CHARSET = new DataKey<>("charset");
    public static final DataKey<File> BASE_PATH = new DataKey<>("base_path");

}
