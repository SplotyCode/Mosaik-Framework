package io.github.splotycode.mosaik.webapi.response.content;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
