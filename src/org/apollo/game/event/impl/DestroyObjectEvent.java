package org.apollo.game.event.impl;

import org.apollo.game.model.obj.GameObject;

/**
 * An {@link ObjectEvent} that deletes an object.
 * @author Steve
 */
public final class DestroyObjectEvent extends ObjectEvent {

	/**
	 * Creates a new destroy object event.
	 * @param object The object to destroy.
	 */
	public DestroyObjectEvent(GameObject object) {
		super(object.getPosition(), object);
	}

}
