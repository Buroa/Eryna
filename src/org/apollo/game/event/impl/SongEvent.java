package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that sends a sound id to the client.
 * @author Steve
 */
public final class SongEvent extends Event {

	/**
	 * The current sound.
	 */
	private final int sound;

	/**
	 * The delay.
	 */
	private int delay = 0;

	/**
	 * The type.
	 */
	private final int type;

	/**
	 * The sound type.
	 */
	private int soundType = 0;

	/**
	 * Create a song.
	 * @param sound The sound id.
	 */
	public SongEvent(int sound) {
		this.sound = sound;
		this.type = 1; // packet 74
	}

	/**
	 * Create a temporary song.
	 * @param sound The sound id.
	 * @param delay The sound delay.
	 */
	public SongEvent(int sound, int delay) {
		this.sound = sound;
		this.delay = delay;
		this.type = 3; // packet 121
	}

	/**
	 * Creates a sound.
	 * @param sound The sound.
	 * @param soundType The type.
	 * @param delay The delay.
	 */
	public SongEvent(int sound, int soundType, int delay) {
		this.sound = sound;
		this.soundType = soundType;
		this.delay = delay;
		this.type = 2; // packet 174
	}

	/**
	 * Gets the delay.
	 * @return The delay.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the current sound id.
	 * @return The sound id.
	 */
	public int getSound() {
		return sound;
	}

	/**
	 * Gets the sound type.
	 * @return The sound type.
	 */
	public int getSoundType() {
		return soundType;
	}

	/**
	 * Gets the type.
	 * @return The type.
	 */
	public int getType() {
		return type;
	}
}
