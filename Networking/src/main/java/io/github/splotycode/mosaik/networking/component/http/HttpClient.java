package io.github.splotycode.mosaik.networking.component.http;

import io.github.splotycode.mosaik.networking.component.tcp.TCPClient;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

import java.util.function.Supplier;

public class HttpClient<S extends HttpClient<S>> extends TCPClient<S> {

    public static HttpClient<? extends HttpClient> create() {
        return new HttpClient<>();
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
        if (uri == null) {
            throw new IllegalArgumentException("uri not defined");
        }
        if (method == null) {
            method = HttpMethod.GET;
        }
        if (version == null) {
            version = HttpVersion.HTTP_1_1;
        }
    }

    private String uri;
    private HttpVersion version;
    private HttpMethod method;
    private DefaultHttpHeaders headers = new DefaultHttpHeaders();

    private boolean compress;

    private Supplier<HttpClientCodec> codec;
    private Supplier<HttpContentDecompressor> decompressor;

    {
        onBound((component, future) -> {
            HttpRequest request = new DefaultFullHttpRequest(version, method, uri);
            request.headers().setAll(headers);
            future.channel().writeAndFlush(request);
        });
    }

    @Override
    protected void doHandlers(ChannelPipeline pipeline) {
        super.doHandlers(pipeline);
        pipeline.addLast("codec", codec == null ? new HttpClientCodec() : codec.get());
        if (compress) {
            pipeline.addLast("decompressor", decompressor == null ? new HttpContentDecompressor() : decompressor.get());
        }
    }

    public S compressor(Supplier<HttpContentDecompressor> decompressor) {
        this.decompressor = decompressor;
        return self();
    }

    public S codec(Supplier<HttpClientCodec> codec) {
        this.codec = codec;
        return self();
    }

    public S compress(boolean compress) {
        this.compress = compress;
        return self();
    }

    public S header(CharSequence name, String value) {
        headers.add(name, value);
        return self();
    }

    public S version(HttpVersion version) {
        this.version = version;
        return self();
    }

    public S method(HttpMethod method) {
        this.method = method;
        return self();
    }

    public S uri(String uri) {
        this.uri = uri;
        return self();
    }

    public S userAgend(String userAgent) {
        return header(HttpHeaderNames.USER_AGENT, userAgent);
    }

}
