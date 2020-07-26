package io.github.splotycode.mosaik.util.io.buffer;

public interface BufferType<T> {
    BufferType<byte[]> BYTE = new BufferType<byte[]>() {
        @Override
        public void update(BufferHolder holder, byte[] buffer) {
            holder.updateBuffer(buffer);
        }

        @Override
        public byte[] provide(BufferProvider provider) {
            return provider.provideBuffer();
        }

        @Override
        public byte[] create(int size) {
            return new byte[size];
        }
    };

    BufferType<char[]> CHAR = new BufferType<char[]>() {
        @Override
        public void update(BufferHolder holder, char[] buffer) {
            holder.updateCharBuffer(buffer);
        }

        @Override
        public char[] provide(BufferProvider provider) {
            return provider.provideCharBuffer();
        }

        @Override
        public char[] create(int size) {
            return new char[size];
        }
    };

    void update(BufferHolder holder, T buffer);

    T provide(BufferProvider provider);
    T create(int size);
}
