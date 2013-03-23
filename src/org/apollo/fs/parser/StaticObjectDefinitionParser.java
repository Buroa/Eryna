package org.apollo.fs.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.obj.StaticGameObject;
import org.apollo.util.ByteBufferUtil;
import org.apollo.util.CompressionUtil;

/**
 * A class which parses the placement information of in-game objects on the map.
 * @author Chris Fletcher
 */
public final class StaticObjectDefinitionParser {

	/**
	 * The number of parsed landscapes.
	 */
	private int parsedLandscapes = 0;

	/**
	 * The number of parsed tiles.
	 */
	private int parsedTiles = 0;

	/**
	 * The indexed file system.
	 */
	private final IndexedFileSystem fs;

	/**
	 * Creates a new static object parser.
	 * @param fs The indexed file system.
	 */
	public StaticObjectDefinitionParser(IndexedFileSystem fs) {
		this.fs = fs;
	}

	/**
	 * Parses all static objects and tiles.
	 * @return The clipped objects and tiles.
	 * @throws IOException if an I/O error occurs.
	 */
	public int[] parse() throws IOException {
		final Archive versionList = Archive.decode(fs.getFile(0, 5));
		final ByteBuffer buffer = versionList.getEntry("map_index").getBuffer();

		final int indices = buffer.remaining() / 7;
		final int[] areas = new int[indices];
		final int[] landscapes = new int[indices];
		final int[] tiles = new int[indices];

		for (int i = 0; i < indices; i++) {
			areas[i] = buffer.getShort() & 0xFFFF;
			tiles[i] = buffer.getShort() & 0xFFFF;
			landscapes[i] = buffer.getShort() & 0xFFFF;

			@SuppressWarnings("unused")
			final boolean members = (buffer.get() & 0xFF) == 1;
		}

		for (int i = 0; i < indices; i++) {
			final ByteBuffer compressed_objects = fs.getFile(4, landscapes[i]);
			final ByteBuffer uncompressed_objects = ByteBuffer.wrap(CompressionUtil.ungzip(compressed_objects));
			final ByteBuffer compressed_tiles = fs.getFile(4, tiles[i]);
			final ByteBuffer uncompressed_tiles = ByteBuffer.wrap(CompressionUtil.ungzip(compressed_tiles));
			parseLandscape(areas[i], uncompressed_objects);
			parseTiles(areas[i], uncompressed_tiles);
		}

		return new int[] { parsedLandscapes, parsedTiles };
	}

	/**
	 * Parses a single landscape from the specified buffer.
	 * @param area The identifier of that area.
	 * @param object_buffer The buffer which holds the area's object data.
	 * @param tile_buffer The buffer which holds the area's tile data.
	 */
	private void parseLandscape(int area, ByteBuffer landscape) {
		final int x = (area >> 8 & 0xFF) * 64;
		final int y = (area & 0xFF) * 64;

		int id = -1;
		int idOffset;

		while ((idOffset = ByteBufferUtil.readSmart(landscape)) != 0) {
			id += idOffset;

			int position = 0;
			int positionOffset;

			while ((positionOffset = ByteBufferUtil.readSmart(landscape)) != 0) {
				position += positionOffset - 1;

				final int localX = position >> 6 & 0x3F;
				final int localY = position & 0x3F;
				final int height = position >> 12;

				final int info = landscape.get() & 0xFF;
				final int type = info >> 2;
				final int rotation = info & 3;

				final Position pos = new Position(x + localX, y + localY, height);

				final StaticGameObject object = new StaticGameObject(id, pos, type, rotation);
				World.getWorld().getRegionManager().getRegionByPosition(pos).add(object);
				parsedLandscapes++;
			}
		}
	}

	/**
	 * Parses a single floor from the specified buffer.
	 * @param area The identifier of that area.
	 * @param object_buffer The buffer which holds the area's object data.
	 * @param tile_buffer The buffer which holds the area's tile data.
	 */
	@SuppressWarnings("unused")
	private void parseTiles(int area, ByteBuffer tiles) {
		final int x = (area >> 8 & 0xFF) * 64;
		final int y = (area & 0xFF) * 64;

		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					int v = tiles.get();
					if (v == 0) {
						break;
					} else if (v == 1) {
						tiles.get();
						break;
					} else if (v <= 49) {
						tiles.get();
					} else if (v <= 81) {
						v = v - 49;
						if ((v & 0x1) == 1 && (v & 2) != 2) {
							Position position = new Position(x + i2, y + i3, i);
							// TODO Should we clip these tiles?
							// how do we know they are a water tile?
							parsedTiles++;
						}
					}
				}
			}
		}
	}

}