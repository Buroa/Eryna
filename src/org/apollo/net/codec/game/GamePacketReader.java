package org.apollo.net.codec.game;

import org.apollo.util.ChannelBufferUtil;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * A utility class for reading {@link GamePacket}s.
 * @author Graham
 */
public final class GamePacketReader {

	/**
	 * The buffer.
	 */
	private final ChannelBuffer buffer;

	/**
	 * The current mode.
	 */
	private AccessMode mode = AccessMode.BYTE_ACCESS;

	/**
	 * The current bit index.
	 */
	private int bitIndex;

	/**
	 * Creates the reader.
	 * @param packet The packet.
	 */
	public GamePacketReader(GamePacket packet) {
		buffer = packet.getPayload();
	}

	/**
	 * Checks that this reader is in the bit access mode.
	 */
	private void checkBitAccess() {
		if (mode != AccessMode.BIT_ACCESS)
			throw new IllegalStateException("For bit-based calls to work, the mode must be bit access");
	}

	/**
	 * Checks that this reader is in the byte access mode.
	 */
	private void checkByteAccess() {
		if (mode != AccessMode.BYTE_ACCESS)
			throw new IllegalStateException("For byte-based calls to work, the mode must be byte access");
	}

	/**
	 * Reads a standard data type from the buffer with the specified order and
	 * transformation.
	 * @param type The data type.
	 * @param order The data order.
	 * @param transformation The data transformation.
	 * @return The value.
	 */
	private long get(DataType type, DataOrder order, DataTransformation transformation) {
		checkByteAccess();
		long longValue = 0;
		final int length = type.getBytes();
		if (order == DataOrder.BIG) {
			if (type == DataType.LONG && transformation == DataTransformation.QUADRUPLE)
				return buffer.readLong();
			else
				for (int i = length - 1; i >= 0; i--)
					if (i == 0 && transformation != DataTransformation.NONE) {
						if (transformation == DataTransformation.ADD)
							longValue |= buffer.readByte() - 128 & 0xFF;
						else if (transformation == DataTransformation.NEGATE)
							longValue |= -buffer.readByte() & 0xFF;
						else if (transformation == DataTransformation.SUBTRACT)
							longValue |= 128 - buffer.readByte() & 0xFF;
						else
							throw new IllegalArgumentException("unknown transformation");
					}
					else
						longValue |= (buffer.readByte() & 0xFF) << i * 8;
		}
		else if (order == DataOrder.LITTLE) {
			for (int i = 0; i < length; i++)
				if (i == 0 && transformation != DataTransformation.NONE) {
					if (transformation == DataTransformation.ADD)
						longValue |= buffer.readByte() - 128 & 0xFF;
					else if (transformation == DataTransformation.NEGATE)
						longValue |= -buffer.readByte() & 0xFF;
					else if (transformation == DataTransformation.SUBTRACT)
						longValue |= 128 - buffer.readByte() & 0xFF;
					else
						throw new IllegalArgumentException("unknown transformation");
				}
				else
					longValue |= (buffer.readByte() & 0xFF) << i * 8;
		}
		else if (order == DataOrder.MIDDLE) {
			if (transformation != DataTransformation.NONE)
				throw new IllegalArgumentException("middle endian cannot be transformed");
			if (type != DataType.INT)
				throw new IllegalArgumentException("middle endian can only be used with an integer");
			longValue |= (buffer.readByte() & 0xFF) << 8;
			longValue |= buffer.readByte() & 0xFF;
			longValue |= (buffer.readByte() & 0xFF) << 24;
			longValue |= (buffer.readByte() & 0xFF) << 16;
		}
		else if (order == DataOrder.INVERSED_MIDDLE) {
			if (transformation != DataTransformation.NONE)
				throw new IllegalArgumentException("inversed middle endian cannot be transformed");
			if (type != DataType.INT)
				throw new IllegalArgumentException("inversed middle endian can only be used with an integer");
			longValue |= (buffer.readByte() & 0xFF) << 16;
			longValue |= (buffer.readByte() & 0xFF) << 24;
			longValue |= buffer.readByte() & 0xFF;
			longValue |= (buffer.readByte() & 0xFF) << 8;
		}
		else
			throw new IllegalArgumentException("unknown order");
		return longValue;
	}

	/**
	 * Gets a bit from the buffer.
	 * @return The value.
	 */
	public int getBit() {
		return getBits(1);
	}

	/**
	 * Gets {@code numBits} from the buffer.
	 * @param numBits The number of bits.
	 * @return The value.
	 */
	public int getBits(int numBits) {
		if (numBits < 0 || numBits > 32)
			throw new IllegalArgumentException("Number of bits must be between 1 and 32 inclusive");
		checkBitAccess();
		int bytePos = bitIndex >> 3;
		int bitOffset = 8 - (bitIndex & 7);
		int value = 0;
		bitIndex += numBits;
		for (; numBits > bitOffset; bitOffset = 8) {
			value += (buffer.getByte(bytePos++) & DataConstants.BIT_MASK[bitOffset]) << numBits - bitOffset;
			numBits -= bitOffset;
		}
		if (numBits == bitOffset)
			value += buffer.getByte(bytePos) & DataConstants.BIT_MASK[bitOffset];
		else
			value += buffer.getByte(bytePos) >> bitOffset - numBits & DataConstants.BIT_MASK[numBits];
		return value;
	}

	/**
	 * Gets bytes.
	 * @param bytes The target byte array.
	 */
	public void getBytes(byte[] bytes) {
		checkByteAccess();
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = buffer.readByte();
	}

	/**
	 * Gets bytes with the specified transformation.
	 * @param transformation The transformation.
	 * @param bytes The target byte array.
	 */
	public void getBytes(DataTransformation transformation, byte[] bytes) {
		if (transformation == DataTransformation.NONE)
			getBytesReverse(bytes);
		else
			for (int i = 0; i < bytes.length; i++)
				bytes[i] = (byte) getSigned(DataType.BYTE, transformation);
	}

	/**
	 * Gets bytes in reverse.
	 * @param bytes The target byte array.
	 */
	public void getBytesReverse(byte[] bytes) {
		checkByteAccess();
		for (int i = bytes.length - 1; i >= 0; i--)
			bytes[i] = buffer.readByte();
	}

	/**
	 * Gets bytes in reverse with the specified transformation.
	 * @param transformation The transformation.
	 * @param bytes The target byte array.
	 */
	public void getBytesReverse(DataTransformation transformation, byte[] bytes) {
		if (transformation == DataTransformation.NONE)
			getBytesReverse(bytes);
		else
			for (int i = bytes.length - 1; i >= 0; i--)
				bytes[i] = (byte) getSigned(DataType.BYTE, transformation);
	}

	/**
	 * Gets the length of this reader.
	 * @return The length of this reader.
	 */
	public int getLength() {
		checkByteAccess();
		return buffer.writableBytes();
	}

	/**
	 * Gets a signed data type from the buffer.
	 * @param type The data type.
	 * @return The value.
	 */
	public long getSigned(DataType type) {
		return getSigned(type, DataOrder.BIG, DataTransformation.NONE);
	}

	/**
	 * Gets a signed data type from the buffer with the specified order.
	 * @param type The data type.
	 * @param order The byte order.
	 * @return The value.
	 */
	public long getSigned(DataType type, DataOrder order) {
		return getSigned(type, order, DataTransformation.NONE);
	}

	/**
	 * Gets a signed data type from the buffer with the specified order and
	 * transformation.
	 * @param type The data type.
	 * @param order The byte order.
	 * @param transformation The data transformation.
	 * @return The value.
	 */
	public long getSigned(DataType type, DataOrder order, DataTransformation transformation) {
		long longValue = get(type, order, transformation);
		if (type != DataType.LONG) {
			final int max = (int) (Math.pow(2, type.getBytes() * 8 - 1) - 1);
			if (longValue > max)
				longValue -= (max + 1) * 2;
		}
		return longValue;
	}

	/**
	 * Gets a signed data type from the buffer with the specified
	 * transformation.
	 * @param type The data type.
	 * @param transformation The data transformation.
	 * @return The value.
	 */
	public long getSigned(DataType type, DataTransformation transformation) {
		return getSigned(type, DataOrder.BIG, transformation);
	}

	/**
	 * Gets a signed smart from the buffer.
	 * @return The smart.
	 */
	public int getSignedSmart() {
		checkByteAccess();
		final int peek = buffer.getByte(buffer.readerIndex());
		if (peek < 128)
			return buffer.readByte() - 64;
		else
			return buffer.readShort() - 49152;
	}

	/**
	 * Gets a string from the buffer.
	 * @return The string.
	 */
	public String getString() {
		checkByteAccess();
		return ChannelBufferUtil.readString(buffer);
	}

	/**
	 * Gets an unsigned data type from the buffer.
	 * @param type The data type.
	 * @return The value.
	 */
	public long getUnsigned(DataType type) {
		return getUnsigned(type, DataOrder.BIG, DataTransformation.NONE);
	}

	/**
	 * Gets an unsigned data type from the buffer with the specified order.
	 * @param type The data type.
	 * @param order The byte order.
	 * @return The value.
	 */
	public long getUnsigned(DataType type, DataOrder order) {
		return getUnsigned(type, order, DataTransformation.NONE);
	}

	/**
	 * Gets an unsigned data type from the buffer with the specified order and
	 * transformation.
	 * @param type The data type.
	 * @param order The byte order.
	 * @param transformation The data transformation.
	 * @return The value.
	 */
	public long getUnsigned(DataType type, DataOrder order, DataTransformation transformation) {
		final long longValue = get(type, order, transformation);
		if (type == DataType.LONG)
			throw new IllegalArgumentException("due to java restrictions, longs must be read as signed types");
		return longValue & 0xFFFFFFFFFFFFFFFFL;
	}

	/**
	 * Gets an unsigned data type from the buffer with the specified
	 * transformation.
	 * @param type The data type.
	 * @param transformation The data transformation.
	 * @return The value.
	 */
	public long getUnsigned(DataType type, DataTransformation transformation) {
		return getUnsigned(type, DataOrder.BIG, transformation);
	}

	/**
	 * Gets an unsigned smart from the buffer.
	 * @return The smart.
	 */
	public int getUnsignedSmart() {
		checkByteAccess();
		final int peek = buffer.getByte(buffer.readerIndex());
		if (peek < 128)
			return buffer.readByte();
		else
			return buffer.readShort() - 32768;
	}

	/**
	 * Switches this builder's mode to the bit access mode.
	 */
	public void switchToBitAccess() {
		if (mode == AccessMode.BIT_ACCESS)
			throw new IllegalStateException("Already in bit access mode");
		mode = AccessMode.BIT_ACCESS;
		bitIndex = buffer.readerIndex() * 8;
	}

	/**
	 * Switches this builder's mode to the byte access mode.
	 */
	public void switchToByteAccess() {
		if (mode == AccessMode.BYTE_ACCESS)
			throw new IllegalStateException("Already in byte access mode");
		mode = AccessMode.BYTE_ACCESS;
		buffer.readerIndex((bitIndex + 7) / 8);
	}
}
