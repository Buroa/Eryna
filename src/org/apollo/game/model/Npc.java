package org.apollo.game.model;

import org.apollo.game.event.Event;
import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * A {@link Npc} is a {@link Character} which is computer-controlled (Non-Player
 * Character).
 * @author Steve
 */
public final class Npc extends Character {

	/**
	 * The default position of this NPC.
	 */
	private static final Position DEFAULT_POSITION = new Position(0, 0);

	/**
	 * The first position set to this NPC.
	 */
	private final Position FIRST_POSITION;

	/**
	 * The NPC id.
	 */
	private final int id;

	/**
	 * The random walking flag.
	 */
	private boolean randomWalking = false;

	/**
	 * The facing id.
	 */
	private int face;

	/**
	 * The random walking execution.
	 */
	private boolean randomWalkingExec = false;

	/**
	 * Creates a new NPC.
	 * @param id The NPC id.
	 */
	public Npc(int id) {
		this(id, DEFAULT_POSITION);
	}

	/**
	 * Creates a new NPC.
	 * @param id The NPC id.
	 * @param position The position to place the NPC at.
	 */
	public Npc(int id, Position position) {
		super(position);
		setPosition(position);
		this.FIRST_POSITION = position;
		this.id = id;
		loadSkills();
	}

	@Override
	public void exit() {
	}

	/**
	 * Gets the definition of this NPC.
	 * @return The definition.
	 */
	public NpcDefinition getDefinition() {
		return NpcDefinition.forId(id);
	}

	/**
	 * Gets the face.
	 * @return The face.
	 */
	public int getFace() {
		return face;
	}

	/**
	 * Gets the first position that was ever set to this NPC.
	 * @return The first position.
	 */
	public Position getFirstPosition() {
		return FIRST_POSITION;
	}

	/**
	 * Gets the NPC id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the npcs name.
	 * @return The npcs name.
	 */
	public String getName() {
		if (getDefinition() == null)
			return "Unknown";
		return getDefinition().getName();
	}

	@Override
	public int getRunEnergy() {
		return 0;
	}

	@Override
	public boolean isControlling() {
		return false;
	}

	/**
	 * Gets the random walking flag.
	 * @return True if random walking, false if otherwise.
	 */
	public boolean isRandomWalking() {
		return randomWalking;
	}

	/**
	 * Gets the random walking execution.
	 * @return The random walking execution.
	 */
	public boolean isRandomWalkingExec() {
		return randomWalkingExec;
	}

	/**
	 * Loads the skills.
	 */
	private void loadSkills() {
		final NpcDefinition def = getDefinition();
		final SkillSet skillSet = getSkillSet();

		skillSet.stopFiringEvents();
		for (int i = 0; i < SkillSet.SKILL_COUNT; i++) {
			final Skill skill = def.getSkill(i);
			if (skill != null)
				skillSet.setSkill(i, skill);
		}
		skillSet.startFiringEvents();
	}

	@Override
	public void pulse() {
		; 
	}

	@Override
	public void send(Event event) {
		if (event.getEventId() == 3) {
			final String message = ((ServerMessageEvent) event).getMessage();
			getBlockSet().add(SynchronizationBlock.createForceChatBlock(message));
		}
	}

	/**
	 * Sets the face.
	 * @param face The face.
	 */
	public void setFace(int face) {
		this.face = face;
	}

	/**
	 * Sets the random walking flag.
	 * @param randomWalking The random walking flag.
	 */
	public void setRandomWalking(boolean randomWalking) {
		this.randomWalking = randomWalking;
	}

	/**
	 * Sets the random walking execution.
	 * @param randomWalkingExec The random walking execution.
	 */
	public void setRandomWalkingExec(boolean randomWalkingExec) {
		this.randomWalkingExec = randomWalkingExec;
	}

	@Override
	public String toString() {
		return Npc.class.getName() + " [id=" + id + "]";
	}

	@Override
	public int type() {
		return Entity.NPC_TYPE;
	}

}