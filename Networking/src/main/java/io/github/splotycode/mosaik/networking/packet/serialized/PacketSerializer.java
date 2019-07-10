package io.github.splotycode.mosaik.networking.packet.serialized;

import io.github.splotycode.mosaik.networking.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class PacketSerializer extends ByteBuf {

    private final ByteBuf buf;

    public PacketSerializer(ByteBuf buf) {
        this.buf = buf;
    }

    public void writeVarInt(int value) {
        byte part;
        do {
            part = (byte) (value & 0x7F);
            value >>>= 7;
            if (value != 0)
                part |= 0x80;
            buf.writeByte(part);
        } while (value != 0);
    }

    public int readVarInt() {
        int out = 0, bytes = 0;
        byte part;
        do {
            part = buf.readByte();
            out |= (part & 0x7F) << (bytes++ * 7);
            if (bytes > 5)
                throw new DecoderException(String.format("VarInt is too long! (%d > 5)", bytes));
        } while ((part & 0x80) == 0x80);
        return out;
    }

    public <T> T readEnum(Class<T> enumClass) {
        return enumClass.getEnumConstants()[readVarInt()];
    }

    public void writeEnum(Enum<?> source) {
        writeVarInt(source.ordinal());
    }

    public UUID readUUID() {
        return new UUID(buf.readLong(), buf.readLong());
    }

    public void writeUUID(UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    public String readString() {
        byte[] bytes = new byte[readVarInt()];
        readBytes(bytes);
        return new String(bytes, CharsetUtil.UTF_8);
    }

    public void writeString(String string) {
        byte[] bytes = string.getBytes(CharsetUtil.UTF_8);
        writeVarInt(bytes.length);
        writeBytes(bytes);
    }

    public ArrayList<String> readStringList() {
        int i = readVarInt();
        ArrayList<String> list = new ArrayList<>();
        while (i != 0) {
            list.add(readString());
            i--;
        }
        return list;
    }

    public ArrayList<Integer> readIntList() {
        int i = readVarInt();
        ArrayList<Integer> list = new ArrayList<>();
        while (i != 0) {
            list.add(readVarInt());
            i--;
        }
        return list;
    }

    public void writeIntList(ArrayList<Integer> list) {
        writeVarInt(list.size());
        for (Integer i : list)
            writeVarInt(i);
    }

    public void writeStringList(ArrayList<String> list) {
        writeVarInt(list.size());
        for (String s : list)
            writeString(s);
    }

    @Override
    public int capacity() {
        return buf.capacity();
    }

    @Override
    public ByteBuf capacity(int newCapacity) {
        buf.capacity(newCapacity);
        return this;
    }

    @Override
    public int maxCapacity() {
        return buf.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return buf.alloc();
    }

    @Override
    public ByteOrder order() {
        return buf.order();
    }

    @Override
    public ByteBuf order(ByteOrder byteorder) {
        buf.order(byteorder);
        return this;
    }

    @Override
    public ByteBuf unwrap() {
        buf.unwrap();
        return this;
    }

    @Override
    public boolean isDirect() {
        return buf.isDirect();
    }

    @Override
    public int readerIndex() {
        return buf.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int readerIndex) {
        buf.readerIndex(readerIndex);
        return this;
    }

    @Override
    public int writerIndex() {
        return buf.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int writerIndex) {
        buf.writerIndex(writerIndex);
        return this;
    }

    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        buf.setIndex(readerIndex, writerIndex);
        return this;
    }

    @Override
    public int readableBytes() {
        return buf.readableBytes();
    }

    @Override
    public int writableBytes() {
        return buf.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return buf.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return buf.isReadable();
    }

    @Override
    public boolean isReadable(int size) {
        return buf.isReadable(size);
    }

    @Override
    public boolean isWritable() {
        return buf.isWritable();
    }

    @Override
    public boolean isWritable(int size) {
        return buf.isWritable(size);
    }

    @Override
    public ByteBuf clear() {
        buf.clear();
        return this;
    }

    @Override
    public ByteBuf markReaderIndex() {
        buf.markReaderIndex();
        return this;
    }

    @Override
    public ByteBuf resetReaderIndex() {
        buf.resetReaderIndex();
        return this;
    }

    @Override
    public ByteBuf markWriterIndex() {
        buf.markWriterIndex();
        return this;
    }

    @Override
    public ByteBuf resetWriterIndex() {
        buf.resetWriterIndex();
        return this;
    }

    @Override
    public ByteBuf discardReadBytes() {
        buf.discardReadBytes();
        return this;
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        buf.discardSomeReadBytes();
        return this;
    }

    @Override
    public ByteBuf ensureWritable(int minWritableBytes) {
        buf.ensureWritable(minWritableBytes);
        return this;
    }

    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {
        return buf.ensureWritable(minWritableBytes, force);
    }

    @Override
    public boolean getBoolean(int index) {
        return buf.getBoolean(index);
    }

    @Override
    public byte getByte(int index) {
        return buf.getByte(index);
    }

    @Override
    public short getUnsignedByte(int index) {
        return buf.getUnsignedByte(index);
    }

    @Override
    public short getShort(int index) {
        return buf.getShort(index);
    }

    @Override
    public int getUnsignedShort(int index) {
        return buf.getUnsignedShort(index);
    }

    @Override
    public int getMedium(int index) {
        return buf.getMedium(index);
    }

    @Override
    public int getUnsignedMedium(int index) {
        return buf.getUnsignedMedium(index);
    }

    @Override
    public int getInt(int index) {
        return buf.getInt(index);
    }

    @Override
    public long getUnsignedInt(int index) {
        return buf.getUnsignedInt(index);
    }

    @Override
    public long getLong(int index) {
        return buf.getLong(index);
    }

    @Override
    public char getChar(int index) {
        return buf.getChar(index);
    }

    @Override
    public float getFloat(int index) {
        return buf.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return buf.getDouble(index);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf byteBuf) {
        buf.getBytes(index, byteBuf);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf byteBuf, int length) {
        buf.getBytes(index, byteBuf, length);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf bytebuf, int dstIndex, int length) {
        buf.getBytes(index, bytebuf, dstIndex, length);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst) {
        buf.getBytes(index, dst);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        buf.getBytes(index, dst, dstIndex, length);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        buf.getBytes(index, dst);
        return this;
    }

    @Override
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        buf.getBytes(index, out, length);
        return this;
    }

    @Override
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return buf.getBytes(index, out, length);
    }

    @Override
    public ByteBuf setBoolean(int index, boolean value) {
        buf.setBoolean(index, value);
        return this;
    }

    @Override
    public ByteBuf setByte(int index, int value) {
        buf.setByte(index, value);
        return this;
    }

    @Override
    public ByteBuf setShort(int index, int value) {
        buf.setShort(index, value);
        return this;
    }

    @Override
    public ByteBuf setMedium(int index, int value) {
        buf.setMedium(index, value);
        return this;
    }

    @Override
    public ByteBuf setInt(int index, int value) {
        buf.setInt(index, value);
        return this;
    }

    @Override
    public ByteBuf setLong(int index, long value) {
        buf.setLong(index, value);
        return this;
    }

    @Override
    public ByteBuf setChar(int index, int value) {
        buf.setChar(index, value);
        return this;
    }

    @Override
    public ByteBuf setFloat(int index, float value) {
        buf.setFloat(index, value);
        return this;
    }

    @Override
    public ByteBuf setDouble(int index, double value) {
        buf.setDouble(index, value);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src) {
        buf.setBytes(index, src);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        buf.setBytes(index, src, length);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        buf.setBytes(index, src, srcIndex, length);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src) {
        buf.setBytes(index, src);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        buf.setBytes(index, src, srcIndex, length);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuffer src) {
        buf.setBytes(index, src);
        return this;
    }

    @Override
    public int setBytes(int index, InputStream in, int length) throws IOException {
        return buf.setBytes(index, in, length);
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        return buf.setBytes(index, in, length);
    }

    @Override
    public ByteBuf setZero(int index, int length) {
        buf.setZero(index, length);
        return this;
    }

    @Override
    public boolean readBoolean() {
        return buf.readBoolean();
    }

    @Override
    public byte readByte() {
        return buf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return buf.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return buf.readShort();
    }

    @Override
    public int readUnsignedShort() {
        return buf.readUnsignedShort();
    }

    @Override
    public int readMedium() {
        return buf.readMedium();
    }

    @Override
    public int readUnsignedMedium() {
        return buf.readUnsignedMedium();
    }

    @Override
    public int readInt() {
        return buf.readInt();
    }

    @Override
    public long readUnsignedInt() {
        return buf.readUnsignedInt();
    }

    @Override
    public long readLong() {
        return buf.readLong();
    }

    @Override
    public char readChar() {
        return buf.readChar();
    }

    @Override
    public float readFloat() {
        return buf.readFloat();
    }

    @Override
    public double readDouble() {
        return buf.readDouble();
    }

    @Override
    public ByteBuf readBytes(int length) {
        buf.readBytes(length);
        return this;
    }

    @Override
    public ByteBuf readSlice(int length) {
        buf.readSlice(length);
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst) {
        buf.readBytes(dst);
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int length) {
        buf.readBytes(dst, length);
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        buf.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override
    public ByteBuf readBytes(byte[] dst) {
        buf.readBytes(dst);
        return this;
    }

    @Override
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        buf.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override
    public ByteBuf readBytes(ByteBuffer bytebuffer) {
        buf.readBytes(bytebuffer);
        return this;
    }

    @Override
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        buf.readBytes(out, length);
        return this;
    }

    @Override
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return buf.readBytes(out, length);
    }

    @Override
    public ByteBuf skipBytes(int length) {
        buf.skipBytes(length);
        return this;
    }

    @Override
    public ByteBuf writeBoolean(boolean value) {
        buf.writeBoolean(value);
        return this;
    }

    @Override
    public ByteBuf writeByte(int value) {
        buf.writeByte(value);
        return this;
    }

    @Override
    public ByteBuf writeShort(int value) {
        buf.writeShort(value);
        return this;
    }

    @Override
    public ByteBuf writeMedium(int value) {
        buf.writeMedium(value);
        return this;
    }

    @Override
    public ByteBuf writeInt(int value) {
        buf.writeInt(value);
        return this;
    }

    @Override
    public ByteBuf writeLong(long value) {
        buf.writeLong(value);
        return this;
    }

    @Override
    public ByteBuf writeChar(int value) {
        buf.writeChar(value);
        return this;
    }

    @Override
    public ByteBuf writeFloat(float value) {
        buf.writeFloat(value);
        return this;
    }

    @Override
    public ByteBuf writeDouble(double value) {
        buf.writeDouble(value);
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src) {
        buf.writeBytes(src);
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int length) {
        buf.writeBytes(src, length);
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        buf.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override
    public ByteBuf writeBytes(byte[] src) {
        buf.writeBytes(src);
        return this;
    }

    @Override
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        buf.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer src) {
        buf.writeBytes(src);
        return this;
    }

    @Override
    public int writeBytes(InputStream in, int length) throws IOException {
        return buf.writeBytes(in, length);
    }

    @Override
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        return buf.writeBytes(in, length);
    }

    @Override
    public ByteBuf writeZero(int length) {
        buf.writeZero(length);
        return this;
    }

    @Override
    public int indexOf(int fromIndex, int toIndex, byte value) {
        return buf.indexOf(fromIndex, toIndex, value);
    }

    @Override
    public int bytesBefore(byte value) {
        return buf.bytesBefore(value);
    }

    @Override
    public int bytesBefore(int length, byte value) {
        return buf.bytesBefore(length, value);
    }

    @Override
    public int bytesBefore(int index, int length, byte value) {
        return buf.bytesBefore(index, length, value);
    }

    @Override
    public ByteBuf copy() {
        return buf.copy();
    }

    @Override
    public ByteBuf copy(int index, int length) {
        return buf.copy(index, length);
    }

    @Override
    public ByteBuf slice() {
        return buf.slice();
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return buf.slice(index, length);
    }

    @Override
    public ByteBuf duplicate() {
        return buf.duplicate();
    }

    @Override
    public int nioBufferCount() {
        return buf.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return buf.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return buf.nioBuffer(index, length);
    }

    public ByteBuffer internalNioBuffer(int index, int length) {
        return buf.internalNioBuffer(index, length);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return buf.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        return buf.nioBuffers(index, length);
    }

    @Override
    public boolean hasArray() {
        return buf.hasArray();
    }

    @Override
    public byte[] array() {
        return buf.array();
    }

    @Override
    public int arrayOffset() {
        return buf.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return buf.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return buf.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return buf.toString(charset);
    }

    @Override
    public String toString(int index, int length, Charset charset) {
        return buf.toString(index, length, charset);
    }

    @Override
    public int hashCode() {
        return buf.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof PacketSerializer) {
            return buf.equals(((PacketSerializer) o).buf);
        } else if (o instanceof ByteBuf) {
            return buf.equals(o);
        }
        return false;
    }

    @Override
    public int compareTo(ByteBuf bytebuf) {
        return buf.compareTo(bytebuf);
    }

    @Override
    public String toString() {
        return buf.toString();
    }

    @Override
    public ByteBuf retain(int increment) {
        return buf.retain(increment);
    }

    @Override
    public ByteBuf retain() {
        return buf.retain();
    }

    @Override
    public int refCnt() {
        return buf.refCnt();
    }

    @Override
    public boolean release() {
        return buf.release();
    }

    @Override
    public boolean release(int decrement) {
        return buf.release(decrement);
    }

    @Override
    public ByteBuf asReadOnly() {
        return buf.asReadOnly();
    }

    @Override
    public int forEachByte(ByteProcessor byteProcessor) {
        return buf.forEachByte(byteProcessor);
    }

    @Override
    public int forEachByte(int index, int length, ByteProcessor byteProcessor) {
        return buf.forEachByte(index, length, byteProcessor);
    }

    @Override
    public int forEachByteDesc(ByteProcessor processor) {
        return buf.forEachByteDesc(processor);
    }

    @Override
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        return buf.forEachByteDesc(index, length, processor);
    }

    @Override
    public int getBytes(int index, FileChannel channel, long position, int length) throws IOException {
        return buf.getBytes(index, channel, index, length);
    }

    @Override
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        return buf.getCharSequence(index, length, charset);
    }

    @Override
    public int getIntLE(int index) {
        return buf.getIntLE(index);
    }

    @Override
    public long getLongLE(int index) {
        return buf.getLongLE(index);
    }

    @Override
    public int getMediumLE(int index) {
        return buf.getMediumLE(index);
    }

    @Override
    public short getShortLE(int index) {
        return buf.getShortLE(index);
    }

    @Override
    public long getUnsignedIntLE(int index) {
        return buf.getUnsignedShortLE(index);
    }

    @Override
    public int getUnsignedMediumLE(int index) {
        return buf.getUnsignedMediumLE(index);
    }

    @Override
    public int getUnsignedShortLE(int index) {
        return buf.getUnsignedShortLE(index);
    }

    @Override
    public boolean isReadOnly() {
        return buf.isReadOnly();
    }

    @Override
    public int readBytes(FileChannel channel, long position, int length) throws IOException {
        return buf.readBytes(channel, position, length);
    }

    @Override
    public CharSequence readCharSequence(int length, Charset charset) {
        return buf.readCharSequence(length, charset);
    }

    @Override
    public int readIntLE() {
        return buf.readIntLE();
    }

    @Override
    public long readLongLE() {
        return buf.readLongLE();
    }

    @Override
    public int readMediumLE() {
        return buf.readMediumLE();
    }

    @Override
    public ByteBuf readRetainedSlice(int length) {
        return buf.readRetainedSlice(length);
    }

    @Override
    public short readShortLE() {
        return buf.readShortLE();
    }

    @Override
    public long readUnsignedIntLE() {
        return buf.readUnsignedIntLE();
    }

    @Override
    public int readUnsignedMediumLE() {
        return buf.readUnsignedMediumLE();
    }

    @Override
    public int readUnsignedShortLE() {
        return buf.readUnsignedShortLE();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return buf.retainedDuplicate();
    }

    @Override
    public ByteBuf retainedSlice() {
        return buf.retainedSlice();
    }

    @Override
    public ByteBuf retainedSlice(int index, int length) {
        return buf.retainedSlice(index, length);
    }

    @Override
    public int setBytes(int index, FileChannel channel, long position, int length) throws IOException {
        return buf.setBytes(index, channel, position, length);
    }

    @Override
    public int setCharSequence(int index, CharSequence text, Charset charset) {
        return buf.setCharSequence(index, text, charset);
    }

    @Override
    public ByteBuf setIntLE(int index, int value) {
        buf.setIntLE(index, value);
        return this;
    }

    @Override
    public ByteBuf setLongLE(int index, long value) {
        buf.setLongLE(index, value);
        return this;
    }

    @Override
    public ByteBuf setMediumLE(int index, int value) {
        buf.setMediumLE(index, value);
        return this;
    }

    @Override
    public ByteBuf setShortLE(int index, int value) {
        buf.setShortLE(index, value);
        return this;
    }

    @Override
    public ByteBuf touch() {
        return buf.touch();
    }

    @Override
    public ByteBuf touch(Object hint) {
        return buf.touch(hint);
    }

    @Override
    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        return buf.writeBytes(in, position, length);
    }

    @Override
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        return buf.writeCharSequence(sequence, charset);
    }

    @Override
    public ByteBuf writeIntLE(int value) {
        buf.writeIntLE(value);
        return this;
    }

    @Override
    public ByteBuf writeLongLE(long value) {
        buf.writeLongLE(value);
        return this;
    }

    @Override
    public ByteBuf writeShortLE(int value) {
        buf.writeShortLE(value);
        return this;
    }

    @Override
    public ByteBuf writeMediumLE(int value) {
        buf.writeMediumLE(value);
        return this;
    }
}
