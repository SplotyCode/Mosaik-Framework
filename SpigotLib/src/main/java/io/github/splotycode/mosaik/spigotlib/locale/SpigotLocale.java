package io.github.splotycode.mosaik.spigotlib.locale;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.i18n.ILocale;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@AllArgsConstructor
public class SpigotLocale implements ILocale {

    private File file;

    @Override
    public String displayName() {
        return "Spigot-Main";
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            ExceptionUtil.throwRuntime(e);
        }
        return null;
    }
}
