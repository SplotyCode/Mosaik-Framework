package io.github.splotycode.mosaik.networking.store;

import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.store.packets.WritePacket;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageHandler extends SelfAnnotationHandler<SerializedPacket> {

    private FileProvider provider;
    private int indexBuckets, blockSize;
    private HashMethod method;
    private Charset keyCharset;
    private int totalBlockSize;
    private AtomicInteger blocks;

    public StorageHandler(FileProvider provider, int indexBuckets, int blockSize, HashMethod method, Charset keyCharset) throws IOException {
        this.provider = provider;
        this.indexBuckets = indexBuckets;
        this.blockSize = blockSize;
        this.method = method;
        this.keyCharset = keyCharset;

        totalBlockSize = totalBlockSize();
        //No locks required
        int count = 0;
        for (StorageFile file : provider.allBlockFiles()) {
            count += file.getFile().length();
        }
        blocks = new AtomicInteger(count / totalBlockSize);
    }

    public int totalBlockSize() {
        return blockSize + 8;
    }

    public int indexPosition(int hash) {
        int bucket = method.hash(hash, indexBuckets - 1);
        return bucket * 8;
    }

    public long newBlockID() {
        return blocks.incrementAndGet();
    }

    private void writeBlock(long blockID, long lastBlockID, int position, byte[] data, int write, boolean first) throws IOException {
        StorageFile block = provider.getBlock(blockID);
        try {
            RandomAccessFile file = block.getAndLock();
            file.write(data, position, write);
            position += write;

            file.seek(blockID * totalBlockSize + blockSize);
            file.writeLong(position == data.length || first ? lastBlockID : ++blockID);
        } finally {
            block.unLock();
        }
    }

    private long createIndex(RandomAccessFile index, byte[] key, long createValue, long old) throws IOException {
        long blockID = newBlockID();
        index.writeLong(blockID);

        StorageFile storage = provider.getBlock(blockID);
        try {
            RandomAccessFile file = storage.getAndLock();
            file.seek(blockID * totalBlockSize);
            file.writeInt(key.length);
        } finally {
            storage.unLock();
        }

        byte[] data = new byte[key.length + 8];
        System.arraycopy(key, 0, data, 0, key.length);
        data[key.length] = (byte) ((createValue >>> 56) & 0xFF);
        data[key.length + 1] = (byte) ((createValue >>> 48) & 0xFF);
        data[key.length + 2] = (byte) ((createValue >>> 40) & 0xFF);
        data[key.length + 3] = (byte) ((createValue >>> 32) & 0xFF);
        data[key.length + 4] = (byte) ((createValue >>> 24) & 0xFF);
        data[key.length + 5] = (byte) ((createValue >>> 16) & 0xFF);
        data[key.length + 6] = (byte) ((createValue >>> 8) & 0xFF);
        data[key.length + 7] = (byte) (createValue & 0xFF);


        int positon = 0;
        boolean first = true;
        while (positon != data.length) {
            int write = Math.min(first ? blockSize - 2 : blockSize, data.length - positon);
            writeBlock(blockID, old, positon, data, write, first);
            positon += write;
            first = false;
        }
        return createValue;
    }


    private long getDataBlock(byte[] key, int hash) throws IOException {
        return getDataBlock(key, hash, -1);
    }

    private long getDataBlock(byte[] key, int hash, long createValue) throws IOException {
        int bucketPos = indexPosition(hash);
        int firstByte;
        long dataBlock;
        StorageFile index = null;
        try {
            index = provider.getIndex(bucketPos / 8, hash);
            RandomAccessFile file = index.getAndLock();
            file.seek(bucketPos);
            firstByte = file.read();

            if (firstByte == -1) {
                return createValue != -1 ? createIndex(file, key, createValue, -1) : -1;
            }
            dataBlock = readLong(firstByte, file);
        } finally {
            //TODO index does not need to be locked for the complete createIndex() function
            if (index != null) {
                index.unLock();
            }
        }

        return -1;//TODO
        /*
        blocks.seek(dataBlock * totalBlockSize());
        while (true) {
            int keyLength = indexFile.readInt();
            if (keyLength == key.length) {
                int blockIndex = 0;
                boolean cancel = false;
                for (int i = 0; i < keyLength + 8; i++) {
                    if (key[i] != indexFile.read()) {
                        cancel = true;
                        break;
                    }
                    blockIndex++;
                    if (blockIndex > blockSize) {
                        blockIndex = 0;
                        dataBlock = blocks.readLong();
                        blocks.seek(dataBlock * totalBlockSize);
                    }
                }

            }
            blocks.seek(dataBlock * totalBlockSize - 8);
            dataBlock = blocks.readLong();
        }
        return dataBlock;*/
    }

    @PacketTarget
    public void onWrite(WritePacket packet) throws IOException {
        int rawHash = packet.getKey().hashCode();
        byte[] key = packet.getKey().getBytes(keyCharset);
        byte[] value = packet.getValue();

        long block = getDataBlock(key, rawHash, newBlockID());
        long blockPos = block * totalBlockSize;

        StorageFile storage = provider.getBlock(block);
        try {
            RandomAccessFile file = storage.getAndLock();
            file.seek(block);
            file.writeInt(value.length);
            int neededBlocks = value.length / blockSize;
            for (int i = 0; i < neededBlocks; i++) {
                file.write(value, i * blockSize - (i == 0 ? 0 : 2), i == 0 ? blockSize -2 : blockSize);
                long nextBlock = newBlockID();
                file.writeLong(nextBlock);
                file.seek(nextBlock * totalBlockSize);
            }
        } finally {
            storage.unLock();
        }
    }

    private long readLong(int firstByte, RandomAccessFile file) throws IOException {
        return ((long)(readInt(firstByte, file)) << 32) + (file.readInt() & 0xFFFFFFFFL);
    }

    private int readInt(int firstByte, RandomAccessFile file) throws IOException {
        int ch2 = file.read();
        int ch3 = file.read();
        int ch4 = file.read();
        return ((firstByte << 24) + (ch2 << 16) + (ch3 << 8) + ch4);
    }

}
