package org.apollo.fs.archive;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.util.ByteBufferUtil;
import org.apollo.util.CompressionUtil;

/**
 * Represents an archive.
 * @author Graham
 */
public final class Archive {

	/**
	 * Decodes the archive in the specified buffer.
	 * @param buffer The buffer.
	 * @return The archive.
	 * @throws IOException if an I/O error occurs.
	 */
	public static Archive decode(ByteBuffer buffer) throws IOException {
		final int extractedSize = ByteBufferUtil.readUnsignedTriByte(buffer);
		final int size = ByteBufferUtil.readUnsignedTriByte(buffer);
		boolean extracted = false;
		if (size != extractedSize) {
			final byte[] compressed = new byte[size];
			final byte[] uncompressed = new byte[extractedSize];
			buffer.get(compressed);
			CompressionUtil.unbzip2(compressed, uncompressed);
			buffer = ByteBuffer.wrap(uncompressed);
			extracted = true;
		}
		final int entries = buffer.getShort() & 0xFFFF;
		final int[] identifiers = new int[entries];
		final int[] extractedSizes = new int[entries];
		final int[] sizes = new int[entries];
		for (int i = 0; i < entries; i++) {
			identifiers[i] = buffer.getInt();
			extractedSizes[i] = ByteBufferUtil.readUnsignedTriByte(buffer);
			sizes[i] = ByteBufferUtil.readUnsignedTriByte(buffer);
		}
		final ArchiveEntry[] entry = new ArchiveEntry[entries];
		for (int i = 0; i < entries; i++) {
			ByteBuffer entryBuffer;
			if (!extracted) {
				final byte[] compressed = new byte[sizes[i]];
				final byte[] uncompressed = new byte[extractedSizes[i]];
				buffer.get(compressed);
				CompressionUtil.unbzip2(compressed, uncompressed);
				entryBuffer = ByteBuffer.wrap(uncompressed);
			}
			else {
				final byte[] buf = new byte[extractedSizes[i]];
				buffer.get(buf);
				entryBuffer = ByteBuffer.wrap(buf);
			}
			entry[i] = new ArchiveEntry(identifiers[i], entryBuffer);
		}
		return new Archive(entry);
	}

	/**
	 * The entries in this archive.
	 */
	private final ArchiveEntry[] entries;

	/**
	 * Creates a new archive.
	 * @param entries The entries in this archive.
	 */
	public Archive(ArchiveEntry[] entries) {
		this.entries = entries;
	}

	/**
	 * Gets an entry by its name.
	 * @param name The name.
	 * @return The entry.
	 * @throws FileNotFoundException if the file could not be found.
	 */
	public ArchiveEntry getEntry(String name) throws FileNotFoundException {
		int hash = 0;
		name = name.toUpperCase();
		for (int i = 0; i < name.length(); i++)
			hash = hash * 61 + name.charAt(i) - 32;
		for (final ArchiveEntry entry : entries)
			if (entry.getIdentifier() == hash)
				return entry;
		throw new FileNotFoundException();
	}
}
