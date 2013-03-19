package org.apollo.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apollo.game.model.Character;
import org.apollo.game.model.Npc;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.region.Region;
import org.apollo.game.model.region.RegionManager;

/**
 * A utility used for the {@link RegionManager}.
 * @author Steve
 */
public final class RegionUtil {

	/**
	 * The max distance a character can see.
	 */
	private static final int MAX_DISTANCE = Position.MAX_DISTANCE;

	/**
	 * Gets the local npcs.
	 * @param character The character.
	 * @return The local npcs.
	 */
	public static Collection<Npc> getLocalNpcs(Character character) {
		final List<Npc> localNpcs = new LinkedList<Npc>();
		final Region region = character.getRegion();
		for (final Npc npc : region.getNpcs())
			if (npc.getPosition().getHeight() == character.getPosition().getHeight())
				if (npc.getPosition().getDistance(character.getPosition()) <= MAX_DISTANCE)
					localNpcs.add(npc);
		return Collections.unmodifiableCollection(localNpcs);
	}

	/**
	 * Gets the local players.
	 * @param character The character.
	 * @return The local players.
	 */
	public static Collection<Player> getLocalPlayers(Character character) {
		final List<Player> localPlayers = new LinkedList<Player>();
		final Region region = character.getRegion();
		for (final Player player : region.getPlayers())
			if (player.getPosition().getHeight() == character.getPosition().getHeight())
				if (player.getPosition().getDistance(character.getPosition()) <= MAX_DISTANCE)
					localPlayers.add(player);
		return Collections.unmodifiableCollection(localPlayers);
	}

	/**
	 * Default constructor preventing instantation.
	 */
	private RegionUtil() {

	}

}
