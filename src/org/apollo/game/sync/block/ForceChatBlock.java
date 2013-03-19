package org.apollo.game.sync.block;

/**
 * The force chat {@link SynchronizationBlock}.
 * @author Steve
 */
public final class ForceChatBlock extends SynchronizationBlock {

	/**
	 * The force chat text.
	 */
	private final String text;

	/**
	 * Creates the force chat block.
	 * @param text The chat text.
	 */
	ForceChatBlock(String text) {
		this.text = text;
	}

	/**
	 * Gets the force chat text.
	 * @return The force chat text.
	 */
	public String getText() {
		return text;
	}
}
