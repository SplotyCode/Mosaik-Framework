package me.david.webapi.response.content;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.david.webapi.response.content.ResponseContent;

import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
@NoArgsConstructor
public class InputStreamContent implements ResponseContent {

    private InputStream inputStream;

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }
}
