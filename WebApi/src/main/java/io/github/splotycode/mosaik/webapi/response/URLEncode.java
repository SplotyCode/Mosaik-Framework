package io.github.splotycode.mosaik.webapi.response;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class URLEncode {

    private static final String UTF_8 = "UTF-8";

    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, UTF_8);
        } catch (UnsupportedEncodingException ex) {
            ExceptionUtil.throwRuntime(ex);
            return null;
        }
    }

    public static String decode(String value) {
        try {
            return URLDecoder.decode(value, UTF_8);
        } catch (UnsupportedEncodingException ex) {
            ExceptionUtil.throwRuntime(ex);
            return null;
        }
    }

}
