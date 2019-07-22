package io.github.splotycode.mosaik.networking.component.http;

import io.github.splotycode.mosaik.networking.component.SSLMode;
import io.github.splotycode.mosaik.networking.component.tcp.TCPServer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HttpServer<S extends HttpServer<S>> extends TCPServer<S> {

    public static HttpServer<? extends HttpServer> create() {
        return new HttpServer<>();
    }

    @Override
    protected void doHandlers(ChannelPipeline pipeline) {
        super.doHandlers(pipeline);
        pipeline.addLast("decoder", decoder == null ? new HttpRequestDecoder() : decoder.get());
        if (aggreagte) {
            pipeline.addLast("aggregator", aggregator == null ? new HttpObjectAggregator(512 * 1024) : aggregator.get());
        }
        pipeline.addLast("encoder", encoder == null ? new HttpResponseEncoder() : encoder.get());
        if (compress) {
            pipeline.addLast("compressor", compressor == null ? new HttpContentCompressor() : compressor.get());
        }
        if (sslMode == SSLMode.FORCE) {
            pipeline.addLast("ssl-redirect", new HttpRedirectHandler());
        }
    }

    private boolean compress;
    private boolean aggreagte;
    private Supplier<HttpRequestDecoder> decoder;
    private Supplier<HttpObjectAggregator> aggregator;
    private Supplier<HttpResponseEncoder> encoder;
    private Supplier<HttpContentCompressor> compressor;

    public S contructDecoder(Supplier<HttpRequestDecoder> decoder) {
        this.decoder = decoder;
        return self();
    }

    public S contructAggregator(Supplier<HttpObjectAggregator> aggregator) {
        this.aggregator = aggregator;
        return self();
    }

    public S contructEncoder(Supplier<HttpResponseEncoder> encoder) {
        this.encoder = encoder;
        return self();
    }

    public S contructCompressor(Supplier<HttpContentCompressor> compressor) {
        this.compressor = compressor;
        return self();
    }

    public S compress(boolean compress) {
        this.compress = compress;
        return self();
    }

    public S aggreagte(boolean aggreagte) {
        this.aggreagte = aggreagte;
        return self();
    }

}
