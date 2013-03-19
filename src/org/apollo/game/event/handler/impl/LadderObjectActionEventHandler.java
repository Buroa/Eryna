package org.apollo.game.event.handler.impl;

import org.apollo.game.action.DistancedAction;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ObjectActionEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.def.ObjectDefinition;

/**
 * An {@link EventHandler} for the {@link ObjectActionEvent}
 * @author Steve
 */
public final class LadderObjectActionEventHandler extends EventHandler<ObjectActionEvent> {

	/**
	 * An {@link DistancedAction}
	 * @author Steve
	 */
	private final class LadderClimbingAction extends DistancedAction<Player> {

		/**
		 * The player.
		 */
		private final Player player;

		/**
		 * The definition.
		 */
		private final ObjectDefinition definition;

		/**
		 * The option of the action.
		 */
		private final int option;

		/**
		 * Creates a new ladder climbing action.
		 * @param player The player.
		 * @param ladderPosition The ladder position.
		 * @param definition The ladder definition.
		 * @param option The option.
		 */
		private LadderClimbingAction(Player player, Position ladderPosition, ObjectDefinition definition, int option) {
			super(0, true, player, ladderPosition, 1);
			this.player = player;
			this.definition = definition;
			this.option = option;
		}

		/**
		 * Climbs the ladder.
		 * @param position The position to climb to.
		 */
		private void climb(Position position) {
			player.turnTo(position);
			player.teleport(position);
		}

		@Override
		public void executeAction() {
			final String actionName = definition.getActions()[option - 1];
			final Position playerPosition = player.getPosition();
			if (actionName.contains("down") || actionName.contains("Down")) {
				if (playerPosition.getHeight() == 0)
					climb(new Position(playerPosition.getX(), playerPosition.getY() + 6400, playerPosition.getHeight()));
				else
					climb(new Position(playerPosition.getX(), playerPosition.getY(), playerPosition.getHeight() - 1));
			}
			else if (actionName.contains("up") || actionName.contains("Up"))
				if (playerPosition.getY() > 6400)
					climb(new Position(playerPosition.getX(), playerPosition.getY() - 6400, playerPosition.getHeight()));
				else
					climb(new Position(playerPosition.getX(), playerPosition.getY(), playerPosition.getHeight() + 1));
		}
	}

	@Override
	public void handle(EventHandlerContext ctx, Player player, ObjectActionEvent event) {
		final ObjectDefinition definition = ObjectDefinition.forId(event.getId());
		if (definition != null && definition.getName().contains("Ladder")) {
			player.startAction(new LadderClimbingAction(player, event.getPosition(), definition, event.getOption()));
			ctx.breakHandlerChain();
		}
	}

}