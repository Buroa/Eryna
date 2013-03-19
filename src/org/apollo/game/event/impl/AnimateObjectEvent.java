package org.apollo.game.event.impl;

import org.apollo.game.model.Animation;
import org.apollo.game.model.obj.GameObject;

/**
 * An {@link MapEvent} that animates a object.
 * @author Steve
 */
public final class AnimateObjectEvent extends MapEvent {

	/**
	 * The object to animate.
	 */
	private final GameObject object;

	/**
	 * The animation.
	 */
	private final Animation animation;

	/**
	 * Creates the animate object event.
	 * @param object The object to animate.
	 * @param animation The animation.
	 */
	public AnimateObjectEvent(GameObject object, Animation animation) {
		super(object.getPosition());
		this.object = object;
		this.animation = animation;
	}

	/**
	 * Gets the animation.
	 * @return The animation.
	 */
	public Animation getAnimation() {
		return animation;
	}

	/**
	 * Gets the object to animate.
	 * @return The object to animate.
	 */
	public GameObject getObject() {
		return object;
	}

}
