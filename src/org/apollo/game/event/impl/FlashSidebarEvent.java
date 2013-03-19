package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} for the sidebar switching.
 * @author Solid Snake
 */
public class FlashSidebarEvent extends Event {

	/**
	 * The sidebar which will be flashing.
	 */
	private final int sideBar;

	/**
	 * Create a new flash sidebar event.
	 * @param sidebar The sidebar to flash.
	 */
	public FlashSidebarEvent(int sidebar) {
		this.sideBar = sidebar;
	}

	/**
	 * Gets the sidebar value.
	 * @return The sidebar value.
	 */
	public int getSideBar() {
		return sideBar;
	}
}