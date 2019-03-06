package io.github.splotycode.mosaik.webapi.response.content;

import io.github.splotycode.mosaik.webapi.response.HttpCashingConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

public interface ResponseContent {

    InputStream getInputStream() throws IOException;

    default String getContentType() throws IOException {
        return null;
    }

    default long lastModified() throws IOException {
        return -1;
    }

    default String eTag(HttpCashingConfiguration conf, Supplier<InputStream> stream) throws IOException {
        return conf.generateETag(stream.get());
    }

}
