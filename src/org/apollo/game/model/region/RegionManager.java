package org.apollo.game.model.region;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apollo.game.model.Position;

/**
 * The region manager.
 * @author Steve
 */
public final class RegionManager {

	/**
	 * The map size.
	 */
	public static final int MAP_SIZE = Region.SIZE * 8;

	/**
	 * The regions.
	 */
	private final Map<Position, Region> regions = new HashMap<Position, Region>();

	/**
	 * The chunks.
	 */
	private final Map<Position, Chunk> chunks = new HashMap<Position, Chunk>();

	/**
	 * Gets the center chunks.
	 * @param position The position.
	 * @return The center chunks (will contain nulls).
	 */
	public List<Chunk> getCenterChunks(Position position) {
		final List<Chunk> surrounding = new LinkedList<Chunk>();
		final int chunkX = position.getRegionX() + 6;
		final int chunkY = position.getRegionY() + 6;

		final int radius = 21;
		for (int i = 1; i <= radius; i++) {
			surrounding.add(getChunk(chunkX - i, chunkY - i));
			surrounding.add(getChunk(chunkX + i, chunkY + i));
			surrounding.add(getChunk(chunkX - i, chunkY));
			surrounding.add(getChunk(chunkX, chunkY - i));
			surrounding.add(getChunk(chunkX + i, chunkY));
			surrounding.add(getChunk(chunkX, chunkY + i));
			surrounding.add(getChunk(chunkX - i, chunkY + i));
			surrounding.add(getChunk(chunkX + i, chunkY - i));
		}
		surrounding.add(getChunk(chunkX, chunkY));

		return surrounding;
	}

	/**
	 * Gets a chunk by its x and y coordinates.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The region.
	 */
	private Chunk getChunk(int x, int y) {
		final Position key = new Position(x, y);
		return chunks.get(key);
	}

	/**
	 * Gets a region by it's position.
	 * @param position The position.
	 * @return The region.
	 */
	public Chunk getChunkByPosition(Position position) {
		final int x = position.getRegionX() + 6;
		final int y = position.getRegionY() + 6;
		final Position key = new Position(x, y);
		Chunk chunk = chunks.get(key);
		if (chunk == null) {
			chunk = new Chunk(key);
			chunks.put(key, chunk);
		}
		return chunk;
	}

	/**
	 * Gets the chunk size.
	 * @return The chunk size.
	 */
	public int getChunkSize() {
		return chunks.size();
	}

	/**
	 * Gets a region by it's position.
	 * @param position The position.
	 * @return The region.
	 */
	public Region getRegionByPosition(Position position) {
		final int x = position.getRegionX() + 6 >> 3;
		final int y = position.getRegionY() + 6 >> 3;
		final Position key = new Position(x, y);
		Region region = regions.get(key);
		if (region == null) {
			region = new Region(key);
			regions.put(key, region);
		}
		return region;
	}

	/**
	 * Gets the chunks inside a region.
	 * @param region The region.
	 * @return The chunks inside the region (will contain nulls).
	 */
	public List<Chunk> getRegionChunks(Region region) {
		final Position position = region.getRegionPosition();
		final List<Chunk> surrounding = new LinkedList<Chunk>();
		final int chunkX = position.getLocalX() + 6;
		final int chunkY = position.getLocalY() + 6;

		final int radius = Region.SIZE;
		for (int i = 1; i <= radius; i++) {
			surrounding.add(getChunk(chunkX - i, chunkY - i));
			surrounding.add(getChunk(chunkX + i, chunkY + i));
			surrounding.add(getChunk(chunkX - i, chunkY));
			surrounding.add(getChunk(chunkX, chunkY - i));
			surrounding.add(getChunk(chunkX + i, chunkY));
			surrounding.add(getChunk(chunkX, chunkY + i));
			surrounding.add(getChunk(chunkX - i, chunkY + i));
			surrounding.add(getChunk(chunkX + i, chunkY - i));
		}
		surrounding.add(getChunk(chunkX, chunkY));

		return surrounding;
	}

	/**
	 * Gets the region size.
	 * @return The region size.
	 */
	public int getRegionSize() {
		return regions.size();
	}

}
