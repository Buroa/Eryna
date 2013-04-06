package org.apollo.game.sync.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apollo.game.event.Event;
import org.apollo.game.event.impl.NpcSynchronizationEvent;
import org.apollo.game.event.impl.PlayerSynchronizationEvent;
import org.apollo.game.event.impl.RegionUpdateEvent;
import org.apollo.game.model.Npc;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.region.Chunk;
import org.apollo.game.sync.block.AppearanceBlock;
import org.apollo.game.sync.block.ChatBlock;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.game.sync.seg.AddCharacterSegment;
import org.apollo.game.sync.seg.AddNpcSegment;
import org.apollo.game.sync.seg.MovementSegment;
import org.apollo.game.sync.seg.RemoveCharacterSegment;
import org.apollo.game.sync.seg.SynchronizationSegment;
import org.apollo.game.sync.seg.TeleportSegment;
import org.apollo.util.RegionUtil;

/**
 * A {@link SynchronizationTask} which synchronizes the specified {@link Player}
 * .
 * @author Graham
 */
public final class PlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The maximum number of players to load per cycle. This prevents the update
	 * packet from becoming too large (the
	 * client uses a 5000 byte buffer) and also stops old spec PCs from crashing
	 * when they login or teleport.
	 */
	private static final int NEW_PLAYERS_PER_CYCLE = 20;

	/**
	 * The maximum number of npcs to load per cycle. This prevents the update
	 * packet from becoming too large (the
	 * client uses a 5000 byte buffer).
	 */
	private static final int NEW_NPCS_PER_CYCLE = 20;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PlayerSynchronizationTask} for the specified player.
	 * @param player The player.
	 */
	public PlayerSynchronizationTask(Player player) {
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		updatePlayers(player);
		updateNpcs(player);
		updateRegion(player);
	}

	/**
	 * Gets the facing position.
	 * @param n The npc.
	 * @return The facing position.
	 */
	private Position transformNpcFacing(Npc n) {
		switch (n.getFace()) {
		case 5:
			return n.getPosition().transform(-1, 0, 0);
		case 4:
			return n.getPosition().transform(1, 0, 0);
		case 3:
			return n.getPosition().transform(0, -1, 0);
		case 2:
			return n.getPosition().transform(0, 1, 0);
		default:
			return n.getPosition();
		}
	}

	/**
	 * Updates the npcs.
	 * @param player The player to receive the update.
	 */
	private void updateNpcs(final Player player) {
		SynchronizationBlockSet blockSet = player.getBlockSet();
		final List<Npc> localNPCs = player.getLocalNpcList();
		final int oldLocalNPCs = localNPCs.size();
		final List<SynchronizationSegment> segments = new ArrayList<SynchronizationSegment>();
		boolean update = false; // TODO we are caching everything here

		int added = 0;
		final Collection<Npc> repository = RegionUtil.getLocalNpcs(player);

		for (final Iterator<Npc> it = localNPCs.iterator(); it.hasNext();) {
			final Npc n = it.next();
			final boolean check = n.getPosition().getHeight() == player.getPosition().getHeight();
			if (!n.isActive() || n.isTeleporting() || !check
					|| n.getPosition().getLongestDelta(player.getPosition()) > player.getViewingDistance()) {
				it.remove();
				segments.add(new RemoveCharacterSegment());
				update = true;
			} else
				segments.add(new MovementSegment(n.getBlockSet(), n.getDirections()));
			if (!update && (n.hasMoved() || n.getBlockSet().size() > 0))
				update = true;
		}

		for (final Npc n : repository)
			if (localNPCs.size() >= 255)
				break;
			else {
				if (added >= NEW_NPCS_PER_CYCLE)
					break;
				if (n.isActive() && !localNPCs.contains(n)) {
					localNPCs.add(n);
					blockSet = n.getBlockSet();
					if (n.getFace() > 1) {
						blockSet = blockSet.clone();
						blockSet.add(SynchronizationBlock.createTurnToPositionBlock(transformNpcFacing(n)));
					}
					segments.add(new AddNpcSegment(blockSet, n.getIndex(), n.getPosition(), n.getDefinition().getId()));
					update = true;
					added++;
				}
			}

		if (update) {
			final NpcSynchronizationEvent event = new NpcSynchronizationEvent(player.getPosition(), oldLocalNPCs, segments);
			player.send(event);
		}
	}

	/**
	 * Updates the players.
	 * @param player The player to receive the update.
	 */
	private void updatePlayers(final Player player) {
		final Position lastKnownRegion = player.getLastKnownRegion();
		final boolean regionChanged = player.hasRegionChanged();
		boolean update = false; // TODO we are caching everything here
		SynchronizationSegment segment;
		SynchronizationBlockSet blockSet = player.getBlockSet();
		if (blockSet.contains(ChatBlock.class)) {
			blockSet = blockSet.clone();
			blockSet.remove(ChatBlock.class);
		}

		if (blockSet.size() > 0 || player.hasMoved())
			update = true;
		if (player.isTeleporting() || regionChanged)
			segment = new TeleportSegment(blockSet, player.getPosition());
		else
			segment = new MovementSegment(blockSet, player.getDirections());

		final List<Player> localPlayers = player.getLocalPlayerList();
		final int oldLocalPlayers = localPlayers.size();
		final List<SynchronizationSegment> segments = new ArrayList<SynchronizationSegment>();

		int added = 0;
		final Collection<Player> repository = RegionUtil.getLocalPlayers(player);

		for (final Iterator<Player> it = localPlayers.iterator(); it.hasNext();) {
			final Player p = it.next();
			final boolean check = p.getPosition().getHeight() == player.getPosition().getHeight();
			if (!p.isActive() || p.isTeleporting() || !check
					|| p.getPosition().getLongestDelta(player.getPosition()) > player.getViewingDistance()
					|| p.isHidden()) {
				it.remove();
				segments.add(new RemoveCharacterSegment());
				update = true;
			} else
				segments.add(new MovementSegment(p.getBlockSet(), p.getDirections()));
			if (!update && (p.hasMoved() || p.getBlockSet().size() > 0))
				update = true;
		}

		for (final Player p : repository) {
			if (localPlayers.size() >= 255) {
				player.flagExcessivePlayers();
				break;
			} else if (added >= NEW_PLAYERS_PER_CYCLE)
				break;
			if (p != player && !localPlayers.contains(p)) {
				if (p.isHidden())
					continue;
				blockSet = p.getBlockSet();
				if (!blockSet.contains(AppearanceBlock.class)) {
					blockSet = blockSet.clone();
					blockSet.add(SynchronizationBlock.createAppearanceBlock(p));
				}
				segments.add(new AddCharacterSegment(blockSet, p.getIndex(), p.getPosition()));
				localPlayers.add(p);
				update = true;
				added++;
			}
		}

		if (update || player.getWalkingQueue().size() > 0) {
			final PlayerSynchronizationEvent event = new PlayerSynchronizationEvent(lastKnownRegion, player.getPosition(), regionChanged, segment,
					oldLocalPlayers, segments);
			player.send(event);
		}
	}

	/**
	 * Updates the region.
	 * @param player The player to receive the region update.
	 * @note only required on a region change.
	 */
	private void updateRegion(final Player player) {
		if (!player.hasRegionChanged())
			return;

		final List<Event> localEvents = player.getLocalEventList();
		final List<Chunk> chunks = World.getWorld().getRegionManager().getCenterChunks(player.getPosition());

		// We can do this because we just changed a region.
		// XXX This used to be in region change event handler.
		localEvents.clear();
		for (final Chunk chunk : chunks) {
			if (chunk == null)
				continue;
			final RegionUpdateEvent chunkEvent = chunk.make(player);
			final List<Event> chunkEvents = chunkEvent.getEvents();
			if (chunkEvents.size() > 0) {
				player.send(chunkEvent);
				localEvents.addAll(chunkEvents);
			}
		}
	}
}
