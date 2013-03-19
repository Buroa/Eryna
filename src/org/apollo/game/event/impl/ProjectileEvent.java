package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * An {@link Event} that sends a projectile to the player.
 * @author Steve
 */
public final class ProjectileEvent extends MapEvent {

	/**
	 * The size.
	 */
	private final int size;

	/**
	 * The target.
	 */
	private final int lockOn;

	/**
	 * The offset x.
	 */
	private final byte offsetX;

	/**
	 * The offset y.
	 */
	private final byte offsetY;

	/**
	 * The projectile id.
	 */
	private final int projectileId;

	/**
	 * The delay.
	 */
	private final int delay;

	/**
	 * The duration.
	 */
	private final int duration;

	/**
	 * The start height.
	 */
	private final int startHeight;

	/**
	 * The end height.
	 */
	private final int endHeight;

	/**
	 * The angle of the projectile.
	 */
	private final int curve;

	/**
	 * Create a new projectile event.
	 * @param start The starting position.
	 * @param offsetX The offset x.
	 * @param offsetY The offset y.
	 * @param projectileId The projectile id.
	 * @param startHeight The start height.
	 * @param endHeight The ending height.
	 * @param speed The speed.
	 * @param lockOn The victim.
	 * @param mage The magic flag.
	 */
	public ProjectileEvent(Position start, byte offsetX, byte offsetY, int projectileId, int startHeight,
			int endHeight, int speed, int lockOn, boolean mage) {
		this(start, 0, lockOn, offsetX, offsetY, projectileId, mage ? 50 : 41, speed, startHeight, endHeight, 16);
	}

	/**
	 * Creates a new projectile event.
	 * @param start The starting position.
	 * @param size The size.
	 * @param lockOn The target.
	 * @param offsetX The offset x.
	 * @param offsetY The offset y.
	 * @param projectileId The projectile id.
	 * @param delay The delay.
	 * @param duration The duration.
	 * @param startHeight The start height.
	 * @param endHeight The ending height.
	 * @param curve The angle of the projectile.
	 */
	public ProjectileEvent(Position start, int size, int lockOn, byte offsetX, byte offsetY, int projectileId,
			int delay, int duration, int startHeight, int endHeight, int curve) {
		super(start);
		this.size = size;
		this.lockOn = lockOn;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.projectileId = projectileId;
		this.delay = delay;
		this.duration = duration;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.curve = curve;
	}

	/**
	 * Gets the curve.
	 * @return The curve.
	 */
	public int getCurve() {
		return curve;
	}

	/**
	 * Gets the delay.
	 * @return The delay.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the duration.
	 * @return The duration.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Gets the end height.
	 * @return The end height.
	 */
	public int getEndHeight() {
		return endHeight;
	}

	/**
	 * Gets the victim.
	 * @return The victim.
	 */
	public int getLockOn() {
		return lockOn;
	}

	/**
	 * Gets the offset x.
	 * @return The offset x.
	 */
	public byte getOffsetX_() {
		return offsetX;
	}

	/**
	 * Gets the offset y.
	 * @return The offset y.
	 */
	public byte getOffsetY_() {
		return offsetY;
	}

	/**
	 * Gets the projectile id.
	 * @return The projectile id.
	 */
	public int getProjectileId() {
		return projectileId;
	}

	/**
	 * Gets the size.
	 * @return The size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the start height.
	 * @return The start height.
	 */
	public int getStartHeight() {
		return startHeight;
	}

}