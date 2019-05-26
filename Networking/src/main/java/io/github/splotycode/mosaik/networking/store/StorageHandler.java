package io.github.splotycode.mosaik.networking.store;

import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.store.packets.WritePacket;

import java.io.IOException;
import java.io.RandomAccessFile;

public class StorageHandler extends SelfAnnotationHandler<SerializedPacket> {

    private int indexBuckets, blockSize;
    private HashMethod method;
    private RandomAccessFile index;
    private RandomAccessFile blocks;

    public int totalBlockSize() {
        return blockSize + 8;
    }

    public int indexPosition(int hash) {
        int bucket = method.hash(hash, indexBuckets - 1);
        return bucket * 8;
    }

    @PacketTarget
    public void onWrite(WritePacket packet) throws IOException {
        int rawHash = packet.getKey().hashCode();
        byte[] key = packet.getKey().getBytes();
        byte[] value = packet.getValue();
        int totalBlockSize = totalBlockSize();

        int bucketPos = indexPosition(rawHash);
        index.seek(bucketPos);
        int firstByte = index.read();
        long blockStart;
        if (firstByte == -1) {
            index.seek(bucketPos);
            blockStart = blocks.length() / totalBlockSize + 1;
            index.writeLong(blockStart);
            blockStart *= totalBlockSize;
        } else {
            long dataBlock = readLong(firstByte, index);
            index.seek(dataBlock * totalBlockSize);
            while (true) {
                int keyLength = index.readInt();
                if (keyLength == key.length) {
                    boolean cancel = false;
                    boolean firstBlock = true;
                    for (int i = 0; i + i < keyLength; i++) {
                        if (key[i] != index.read()) {
                            cancel = true;
                            break;
                        }
                        if (i % blockSize - (firstBlock ? 2 : 0) == 0) {
                            index.seek(index.readLong() * totalBlockSize);
                            firstBlock = false;
                        }
                    }
                    if (cancel) {
                        index.seek(dataBlock * totalBlockSize - 8);
                        dataBlock = index.readLong();
                    } else {
                        dataBlock = index.readLong();
                        break;
                    }
                }
                index.seek(dataBlock * totalBlockSize - 8);
                dataBlock = index.readLong();
            }
            blockStart = dataBlock * totalBlockSize;
        }
        index.seek(blockStart);
        index.writeInt(value.length);
        int neededBlocks = value.length / blockSize;
        for (int i = 0; i < neededBlocks; i++) {
            index.write(value, i * blockSize - (i == 0 ? 0 : 2), i == 0 ? blockSize -2 : blockSize);
            long nextBlock = blocks.length() / totalBlockSize + 1;
            index.writeLong(nextBlock);
            index.seek(nextBlock * totalBlockSize);
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
