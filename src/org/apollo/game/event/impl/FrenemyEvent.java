package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Frenemy;

/**
 * An {@link Event} which is for adding or removing a frenemy.
 * @author Steve
 */
public final class FrenemyEvent extends Event {

	/**
	 * The frenemy.
	 */
	private final Frenemy frenemy;

	/**
	 * The remove flag.
	 */
	private final boolean removing;

	/**
	 * Create a new frenemy event.
	 * @param frenemy The frenemy.
	 * @param removing The removing flag.
	 */
	public FrenemyEvent(Frenemy frenemy, boolean removing) {
		this.frenemy = frenemy;
		this.removing = removing;
	}

	/**
	 * Gets the frenemy.
	 * @return The frenemy.
	 */
	public Frenemy getFrenemy() {
		return frenemy;
	}

	/**
	 * Gets the removing flag.
	 * @return True if we are removing the frenemy, false if otherwise.
	 */
	public boolean isRemoving() {
		return removing;
	}

}
