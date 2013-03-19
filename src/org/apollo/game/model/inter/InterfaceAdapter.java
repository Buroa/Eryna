package org.apollo.game.model.inter;

import org.apollo.game.model.Player;

/**
 * An adapter class for the {@link InterfaceListener}.
 * @author Chris Fletcher
 */
public abstract class InterfaceAdapter implements InterfaceListener {

	@Override
	public boolean buttonClicked(Player player, int button) {
		/* empty */
		return false;
	}

	@Override
	public void interfaceClosed(Player player, boolean manually) {
		/* empty */
	}
}
