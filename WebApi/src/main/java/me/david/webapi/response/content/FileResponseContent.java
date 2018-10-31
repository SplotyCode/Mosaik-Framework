package me.david.webapi.response.content;

import lombok.AllArgsConstructor;
import me.david.webapi.response.content.manipulate.ManipulateableContent;
import me.david.webapi.response.content.manipulate.ResponseManipulator;

import java.io.*;

@AllArgsConstructor
public class FileResponseContent implements ManipulateableContent {

    private File file;

    public FileResponseContent(String file) {
        this.file = new File(file);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public ResponseManipulator manipulate() {
        return null;
    }
}
