package org.apollo.game.pf;

import org.apollo.game.model.Position;

/**
 * An interface which represents a path finding algorithm.
 * @author Graham Edgecombe
 */
public abstract class PathFinder {

	/**
	 * Finds a path between two points.
	 * @param location The central point of the tile map.
	 * @param radius The radius of the tile map.
	 * @param map The map the points are on.
	 * @param srcX Source point, x coordinate.
	 * @param srcY Source point, y coordinate.
	 * @param dstX Destination point, x coordinate.
	 * @param dstY Destination point, y coordinate.
	 * @return A path between two points if such a path exists, or
	 * <code>null</code> if no path exists.
	 */
	public Path findPath(Position location, int radius, TileMap map, int srcX, int srcY, int dstX, int dstY) {
		return null;
	}

	/**
	 * Finds a path between two points.
	 * @param location The central point of the tile map.
	 * @param radius The radius of the tile map.
	 * @param map The map the points are on.
	 * @param source Source point.
	 * @param destination Destination point.
	 * @return A path between two points if such a path exists, or
	 * <code>null</code> if no path exists.
	 */
	public Path findPath(Position location, int radius, TileMap map, Position source, Position destination) {
		return findPath(location, radius, map, source.getX(), source.getY(), destination.getX(), destination.getY());
	}

}
