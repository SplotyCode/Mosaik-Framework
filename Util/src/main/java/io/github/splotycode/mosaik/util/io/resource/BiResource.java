package io.github.splotycode.mosaik.util.io.resource;

public interface BiResource<D_B, D_C, S_B, S_C> extends WritableResource<D_B, D_C>, ReadableResource<S_B, S_C> {
    @Override
    default boolean writable() {
        return true;
    }

    @Override
    default boolean readable() {
        return true;
    }
}
