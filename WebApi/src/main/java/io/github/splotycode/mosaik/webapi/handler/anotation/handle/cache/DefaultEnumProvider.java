package io.github.splotycode.mosaik.webapi.handler.anotation.handle.cache;

import io.github.splotycode.mosaik.webapi.response.HttpCashingConfiguration;

import java.util.function.Supplier;

public enum DefaultEnumProvider implements Supplier<HttpCashingConfiguration> {

    ASSETS() {
        @Override
        public HttpCashingConfiguration get() {
            return HttpCashingConfiguration.ASSET_CASHING;
        }
    },
    NO_CASHING() {
        @Override
        public HttpCashingConfiguration get() {
            return HttpCashingConfiguration.NO_CACHE;
        }
    };

    @Override
    public abstract HttpCashingConfiguration get();

}
