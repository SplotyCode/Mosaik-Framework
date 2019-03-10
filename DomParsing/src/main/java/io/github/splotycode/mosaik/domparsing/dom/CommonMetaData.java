package io.github.splotycode.mosaik.domparsing.dom;

import io.github.splotycode.mosaik.util.datafactory.DataKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonMetaData {

    public static final DataKey<String> VERSION = new DataKey<>("general.version");

}
