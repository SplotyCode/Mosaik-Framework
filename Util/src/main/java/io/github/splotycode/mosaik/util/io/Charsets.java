package io.github.splotycode.mosaik.util.io;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.Charset;

/**
 * @deprecated use {@link java.nio.charset.StandardCharsets}
 */
@Deprecated
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Charsets {

    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final Charset UTF16 = Charset.forName("UTF-16");

    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

}
