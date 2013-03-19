package org.apollo.game.pf;

import java.util.HashSet;
import java.util.Set;

import org.apollo.game.event.Event;
import org.apollo.game.event.impl.DestroyObjectEvent;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.obj.DynamicGameObject;
import org.apollo.game.model.obj.GameObject;
import org.apollo.game.model.region.Chunk;
import org.apollo.game.model.region.Region;
import org.apollo.game.model.region.RegionManager;

/**
 * A class which assist in building <code>TileMap</code>s from a collection of
 * <code>GameObject</code>s.
 * @author Graham Edgecombe
 */
public class TileMapBuilder {

	/**
	 * The tile map being built.
	 */
	private final TileMap tileMap;

	/**
	 * The center position.
	 */
	private final Position centerPosition;

	/**
	 * The radius.
	 */
	private final int radius;

	/**
	 * Sets up the tile map builder with the specified radius, and center
	 * position.
	 * @param position
	 * The center position.
	 * @param radius
	 * The radius.
	 */
	public TileMapBuilder(Position position, int radius) {
		this.centerPosition = position;
		this.tileMap = new TileMap(radius * 2 + 1, radius * 2 + 1);
		this.radius = radius;
	}

	/**
	 * Builds the tile map.
	 * @return The built tile map.
	 */
	public TileMap build() {
		// the region manager
		final RegionManager mgr = World.getWorld().getRegionManager();
		// a set of chunks covered by our center position + radius
		final Set<Chunk> coveredChunks = new HashSet<Chunk>();
		// the region that contains the chunks.
		final Region region = mgr.getRegionByPosition(centerPosition);

		// populates the set of covered regions
		for (int x = -radius - 1; x <= radius + 1; x++)
			for (int y = -radius - 1; y <= radius + 1; y++) {
				final Position loc = centerPosition.transform(x, y, 0);
				coveredChunks.add(mgr.getChunkByPosition(loc));
			}

		// calculate top left positions
		final int topX = centerPosition.getX() - radius;
		final int topY = centerPosition.getY() - radius;

		// now fills in the tile map
		for (final Chunk chunk : coveredChunks) {
			for (final GameObject obj : region.getObjects()) {
				fill(topX, topY, obj);
			}
			for (final GameObject obj : chunk.getObjects()) {
				fill(topX, topY, obj);
			}
			for (final Event event : chunk.getEvents()) {
				if (event.getClass().equals(DestroyObjectEvent.class))
					fill(topX, topY, ((DestroyObjectEvent) event).getObject());
			}
		}

		return tileMap;
	}
	
	/**
	 * Fills the map with a object.
	 * @param topX The top x position.
	 * @param topY The top y position.
	 * @param obj The object to check against.
	 */
	private void fill(int topX, int topY, GameObject obj) {
		if (obj.getDefinition().isWalkable())
			return;

		final Position loc = obj.getPosition();
		if (loc.getHeight() != centerPosition.getHeight())
			return;

		int sizeX = obj.getDefinition().getSizeX();
		int sizeY = obj.getDefinition().getSizeY();
		// position in the tile map
		final int posX = loc.getX() - topX;
		final int posY = loc.getY() - topY;
		// check if we are deleting the object
		boolean delete = false;
		if (!obj.isStatic()) {
			DynamicGameObject dgo = ((DynamicGameObject) obj);
			if (dgo.isDeleting() && !dgo.isReplacing())
				delete = true;
		}

		// calculate real posx
		if (obj.getRotation() == 1 || obj.getRotation() == 3) {
			// switch sizes if rotated
			final int temp = sizeX;
			sizeX = sizeY;
			sizeY = temp;
		}

		if (posX + sizeX < 0 || posY + sizeY < 0 || posX >= tileMap.getWidth() || posY >= tileMap.getHeight())
			return;

		if (obj.getType() >= 0 && obj.getType() <= 3) {
			// walls
			if (posX >= 0 && posY >= 0 && posX < tileMap.getWidth() && posY < tileMap.getHeight()) {
				final int finalRotation = obj.getRotation();
				final Tile t = tileMap.getTile(posX, posY);
				int flags = t.getTraversalMask();
				// clear flags
				if (finalRotation == 0)
					flags &= ~Tile.WEST_TRAVERSAL_PERMITTED;
				else if (finalRotation == 1)
					flags &= ~Tile.NORTH_TRAVERSAL_PERMITTED;
				else if (finalRotation == 2)
					flags &= ~Tile.EAST_TRAVERSAL_PERMITTED;
				else
					flags &= ~Tile.SOUTH_TRAVERSAL_PERMITTED;
				if (flags != t.getTraversalMask())
					tileMap.setTile(posX, posY, delete ? TileMap.EMPTY_TILE : new Tile(flags));
			}
		} else if (obj.getType() == 9) {
			// diagonal walls
			if (posX >= 0 && posY >= 0 && posX < tileMap.getWidth() && posY < tileMap.getHeight())
				tileMap.setTile(posX, posY, delete ? TileMap.EMPTY_TILE : TileMap.SOLID_TILE);
		} else if (obj.getType() == 10 || obj.getType() == 11)
			// world objects
			for (int offX = 0; offX <= sizeX; offX++)
				for (int offY = 0; offY <= sizeY; offY++) {
					final int x = offX + posX;
					final int y = offY + posY;
					if (x >= 0 && y >= 0 && x < tileMap.getWidth() && y < tileMap.getHeight())
						tileMap.setTile(x, y, delete ? TileMap.EMPTY_TILE : TileMap.SOLID_TILE);
				}
		else if (obj.getType() == 22) {
			// floor decoration
			if (obj.getDefinition().hasActions())
				if (posX >= 0 && posY >= 0 && posX < tileMap.getWidth() && posY < tileMap.getHeight())
					tileMap.setTile(posX, posY, delete ? TileMap.EMPTY_TILE : TileMap.SOLID_TILE);
		} else {
			// others
		}
	}

}
