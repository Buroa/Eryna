package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * An {@link Event} used to draw hint icon on and/or above the object.
 * @author Solid Snake
 */
public final class HintIconEvent extends Event {

	/**
	 * Hint icon position.
	 */
	private Position position;

	/**
	 * Orient : 2 Middle ? 3 West ? 4 East ? 5 South ? 6 North.
	 */
	private int orient;

	/**
	 * Type : 1 NPC ? 10 Player.
	 */
	private int type;

	/**
	 * The NPC/Player id.
	 */
	private int id;

	/**
	 * Value : Object ? NPC/Player.
	 */
	private final int val;

	/**
	 * Constructor for NPC/Player.
	 * @param type 1 NPC ? 10 Player
	 * @param id NPC/Player id
	 */
	public HintIconEvent(int type, int id) {
		this.val = 2;
		this.type = type;
		this.id = id;
	}

	/**
	 * Constructor for Objects.
	 * @param pos The position of the hint icon
	 * @param orient The orient
	 */
	public HintIconEvent(Position pos, int orient) {
		this.val = 1;
		this.position = pos;
		this.orient = orient;
	}

	/**
	 * Gets icon id.
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets icon facing.
	 * @return facing
	 */
	public int getOrient() {
		return this.orient;
	}

	/**
	 * Gets the position.
	 * @return position
	 */
	public Position getPos() {
		return this.position;
	}

	/**
	 * Gets icon type.
	 * @return type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Gets the value.
	 * @return the value.
	 */
	public int getVal() {
		return this.val;
	}
}