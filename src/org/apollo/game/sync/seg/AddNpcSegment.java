package org.apollo.game.sync.seg;

import org.apollo.game.model.Position;
import org.apollo.game.sync.block.SynchronizationBlockSet;

/**
 * The Class AddNpcSegment.
 * @author Zuppers
 */
public final class AddNpcSegment extends SynchronizationSegment {

	/**
	 * The index.
	 */
	private final int index;

	/**
	 * The position.
	 */
	private final Position position;

	/**
	 * The npc id.
	 */
	private final int npcid;

	/**
	 * Creates the add character segment.
	 * @param blockSet The block set.
	 * @param index The characters's index.
	 * @param position The position.
	 * @param npcid The npc id.
	 */
	public AddNpcSegment(SynchronizationBlockSet blockSet, int index, Position position, int npcid) {
		super(blockSet);
		this.index = index;
		this.position = position;
		this.npcid = npcid;
	}

	/**
	 * Gets the character's index.
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the npcid.
	 * @return the npcid
	 */
	public int getNpcid() {
		return npcid;
	}

	/**
	 * Gets the position.
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.game.sync.seg.SynchronizationSegment#getType()
	 */
	@Override
	public SegmentType getType() {
		return SegmentType.ADD_CHARACTER;
	}
}