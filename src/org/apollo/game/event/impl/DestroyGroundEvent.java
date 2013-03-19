package org.apollo.game.event.impl;

import org.apollo.game.model.GroundItem;

/**
 * An {@link GroundEvent} that destroys a ground item.
 * @author Steve
 */
public final class DestroyGroundEvent extends GroundEvent {

	/**
	 * Creates a destroy ground event.
	 * @param groundItem The ground item to destroy.
	 */
	public DestroyGroundEvent(GroundItem groundItem) {
		super(groundItem.getPosition(), groundItem);
	}

}
