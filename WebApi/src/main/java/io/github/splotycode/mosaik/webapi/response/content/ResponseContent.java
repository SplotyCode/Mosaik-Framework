package io.github.splotycode.mosaik.webapi.response.content;

import java.io.IOException;
import java.io.InputStream;

public interface ResponseContent {

    InputStream getInputStream() throws IOException;

    default String getContentType() throws IOException {
        return null;
    }

}
