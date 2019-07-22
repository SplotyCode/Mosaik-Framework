package io.github.splotycode.mosaik.util.i18n;

import java.io.InputStream;

public interface ILocale {

    String displayName();

    InputStream getInputStream();

}
