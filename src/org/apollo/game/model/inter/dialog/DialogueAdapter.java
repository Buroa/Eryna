package org.apollo.game.model.inter.dialog;

import org.apollo.game.model.Player;

/**
 * An adapter for the {@link DialogueListener}.
 * @author Chris Fletcher
 */
public abstract class DialogueAdapter implements DialogueListener {

	/**
	 * @return {@code false} by default, unless overridden.
	 */
	@Override
	public boolean buttonClicked(Player player, int button) {
		/* empty */
		return false;
	}

	@Override
	public void continued(Player player) {
		/* empty */
	}

	@Override
	public void interfaceClosed(Player player, boolean manually) {
		/* empty */
	}
}