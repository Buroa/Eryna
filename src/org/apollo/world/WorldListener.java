package org.apollo.world;

import org.apollo.net.codec.world.WorldRequest;
import org.apollo.net.codec.world.WorldResponse;

/**
 * Represents a world listener.
 * @author Steve
 */
public interface WorldListener {

	/**
	 * Executes a world request.
	 * @param request The world request.
	 * @return The world response.
	 */
	public WorldResponse execute(WorldRequest request);

}
