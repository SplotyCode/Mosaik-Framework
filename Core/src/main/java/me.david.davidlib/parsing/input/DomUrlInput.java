package me.david.davidlib.parsing.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.david.davidlib.parsing.DomSourceType;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@AllArgsConstructor
@Getter
public class DomUrlInput implements DomInput {

    protected URL url;

    @Override
    public byte[] getBytes() {
        try {
            return IOUtils.toByteArray(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getString() {
        try {
            return IOUtils.toString(url, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InputStream getStream() {
        try (InputStream inputStream = url.openConnection().getInputStream()) {
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DomSourceType getType() {
        return DomSourceType.URL;
    }
}
