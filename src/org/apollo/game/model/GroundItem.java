package org.apollo.game.model;

import org.apollo.game.model.inter.GroundItemListener;

/**
 * A class for creating or destroying a ground item.
 * @author Steve
 */
public final class GroundItem extends Entity {

	/**
	 * The name of the player who controls this item.
	 */
	private final String controllerName;

	/**
	 * The item that is on the floor.
	 */
	private final Item item;

	/**
	 * The amount of remaining pulses until this item appears.
	 */
	private int pulses;

	/**
	 * The amount of remaining pulses until this item is deleted.
	 */
	private int delete;

	/**
	 * The listener.
	 */
	private GroundItemListener listener;

	/**
	 * Creates a open ground item.
	 * @param item The item.
	 * @param position The position.
	 */
	public GroundItem(Item item, Position position) {
		this(null, item, position);
	}

	/**
	 * Creates a private ground item.
	 * @param controllerName The controller of this item.
	 * @param item The item.
	 * @param position The position.
	 */
	public GroundItem(String controllerName, Item item, Position position) {
		super(position);
		pulses = 43;
		delete = pulses * 2;
		this.controllerName = controllerName;
		this.item = item;
	}

	/**
	 * Check to see if we can pickup the item.
	 * @param name The name to check against.
	 * @return True if we can pickup, false if otherwise.
	 */
	public boolean continued(String name) {
		if (controllerName == null)
			return true;
		if (controllerName.equalsIgnoreCase(name))
			return true;
		if (pulses < 1)
			return true;
		return false;
	}

	/**
	 * Decreases the deletes.
	 */
	public void decreaseDeletes() {
		delete--;
	}

	/**
	 * Decreases the pulses.
	 */
	public void decreasePulses() {
		pulses--;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final GroundItem other = (GroundItem) obj;
		if (!item.equals(other.item))
			return false;
		if (!getPosition().equals(other.getPosition()))
			return false;
		if (controllerName != other.controllerName)
			return false;
		return true;
	}

	/**
	 * Gets the controller's name.
	 * @return The controller's name.
	 */
	public String getControllerName() {
		return controllerName != null ? controllerName : "";
	}

	/**
	 * Gets the deletes.
	 * @return The deletes.
	 */
	public int getDeletes() {
		return delete;
	}

	/**
	 * Gets the item.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Gets the listener.
	 * @return The listener.
	 */
	public GroundItemListener getListener() {
		return listener;
	}

	/**
	 * Gets the pulses.
	 * @return The pulses.
	 */
	public int getPulses() {
		return pulses;
	}

	/**
	 * Sets the listener.
	 * @param listener The listener.
	 */
	public void setListener(GroundItemListener listener) {
		this.listener = listener;
	}

	@Override
	public int type() {
		return Entity.GROUND_TYPE;
	}
}