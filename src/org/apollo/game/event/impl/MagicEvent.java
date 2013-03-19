package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Character;
import org.apollo.game.model.World;

/**
 * An {@link Event} for when a player uses magic on a npc or player.
 * @author Steve
 */
public class MagicEvent extends Event {

	/**
	 * The character that was clicked.
	 */
	private final int characterId;

	/**
	 * The spell id that was used.
	 */
	private final int spellId;

	/**
	 * The option.
	 */
	private final int option;

	/**
	 * Creates the magic on player event.
	 * @param characterId The character that was being casted on.
	 * @param spellId The spell id that was used.
	 * @param option The option.
	 */
	public MagicEvent(int characterId, int spellId, int option) {
		this.characterId = characterId;
		this.spellId = spellId;
		this.option = option;
	}

	/**
	 * Gets the character id.
	 * @return The character id.
	 */
	public int getCharacterId() {
		return characterId;
	}

	/**
	 * Gets the option.
	 * @return The option.
	 */
	public int getOption() {
		return option;
	}

	/**
	 * Gets the spell id.
	 * @return The spell id.
	 */
	public int getSpellId() {
		return spellId;
	}

	/**
	 * Gets the victim.
	 * @return The victim.
	 */
	public Character getVictim() {
		return option == 0 ? World.getWorld().getPlayerRepository().forIndex(characterId) : World.getWorld()
				.getNpcRepository().forIndex(characterId);
	}

}
