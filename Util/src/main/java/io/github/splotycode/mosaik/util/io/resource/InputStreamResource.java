package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import io.github.splotycode.mosaik.util.io.copier.CopySources;
import io.github.splotycode.mosaik.util.io.resource.copier.ReadableCopySourceBasedResource;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.function.Supplier;

public abstract class InputStreamResource extends ReadableCopySourceBasedResource<InputStream, Reader> {
    public static InputStreamResource wrap(BufferProvider bufferProvider, AvailableTrust trustAvailableEstimation,
                                           SizeComputationComplexity sizeComplexity, boolean avoidBuffering,
                                           Supplier<InputStream> inputStream) {
        return new InputStreamResource(bufferProvider, trustAvailableEstimation) {
            @Override
            public InputStream openStream() throws IOException {
                return inputStream.get();
            }

            @Override
            public SizeComputationComplexity byteSizeComplexity() throws IOException {
                return sizeComplexity;
            }

            @Override
            public boolean avoidBufferedStream() {
                return avoidBuffering;
            }
        };
    }

    public static InputStreamResource wrap(BufferProvider bufferProvider, AvailableConfig availableConfig,
                                           boolean avoidBuffering, Supplier<InputStream> inputStream) {
        return wrap(bufferProvider, availableConfig.trust, availableConfig.complexity,
                avoidBuffering, inputStream);
    }

    public static InputStreamResource wrap(BufferProvider bufferProvider, Supplier<InputStream> inputStream) {
        return wrap(bufferProvider, AVAILABLE_RETRIEVABLE, false, inputStream);
    }

    public static InputStreamResource wrap(Supplier<InputStream> inputStream) {
        return wrap(ReadableCopySourceBasedResource.DEFAULT_BUFFER_PROVIDER, inputStream);
    }

    public static final AvailableConfig AVAILABLE_UNSUPPORTED = new AvailableConfig(AvailableTrust.NEVER_TRUST, SizeComputationComplexity.COUNTABLE);
    public static final AvailableConfig AVAILABLE_RETRIEVABLE = new AvailableConfig(AvailableTrust.UNSURE, SizeComputationComplexity.RETRIEVABLE);
    public static final AvailableConfig AVAILABLE_COUNTABLE = new AvailableConfig(AvailableTrust.UNSURE, SizeComputationComplexity.COUNTABLE);

    enum AvailableTrust {
        TRUST,
        UNSURE,
        NEVER_TRUST
    }

    @AllArgsConstructor
    static class AvailableConfig {
        AvailableTrust trust;
        SizeComputationComplexity complexity;
    }

    private AvailableTrust trustAvailableEstimation;

    public InputStreamResource(BufferProvider bufferProvider, AvailableTrust trustAvailableEstimation) {
        super(bufferProvider, CopySources.READER, CopySources.STREAM);
        this.trustAvailableEstimation = trustAvailableEstimation;
    }

    @Override
    public InputStream openByteSource() throws IOException {
        return openStream();
    }

    @Override
    public Reader openCharSource() throws IOException {
        return openReader();
    }

    @Override
    public Reader openCharSource(Charset charset) throws IOException {
        return openReader(charset);
    }

    @Override
    public long byteSize() throws IOException {
        try (InputStream is = openPreferredBufferedStream()) {
            if (trustAvailableEstimation != AvailableTrust.NEVER_TRUST) {
                int available = is.available();
                if (trustAvailableEstimation == AvailableTrust.TRUST ||
                        available != 0) {
                    return available;
                }
            }
            return super.byteSize();
        }
    }
}
