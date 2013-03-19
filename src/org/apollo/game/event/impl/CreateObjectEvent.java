package org.apollo.game.event.impl;

import org.apollo.game.model.obj.GameObject;

/**
 * An {@link ObjectEvent} which creates a object.
 * @author Steve
 */
public final class CreateObjectEvent extends ObjectEvent {

	/**
	 * Creates a new create object action event.
	 * @param object The object to create.
	 */
	public CreateObjectEvent(GameObject object) {
		super(object.getPosition(), object);
	}

}
