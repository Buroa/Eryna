package org.apollo.game.model;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.action.Action;
import org.apollo.game.action.impl.TeleportAction;
import org.apollo.game.event.Event;
import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.game.model.Inventory.StackMode;
import org.apollo.game.model.melee.Prayer.Prayers;
import org.apollo.game.model.region.Region;
import org.apollo.game.model.region.RegionManager;
import org.apollo.game.model.skill.HitpointSkillListener;
import org.apollo.game.model.skill.PrayerSkillListener;
import org.apollo.game.scheduling.impl.SkillNormalizationTask;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.util.CharacterRepository;

/**
 * A {@link Character} is a living creature in the world, such as a player or
 * NPC.
 * @author Graham
 */
public abstract class Character extends Entity {

	/**
	 * The index of this character in the {@link CharacterRepository} it belongs
	 * to.
	 */
	private int index = -1;

	/**
	 * Teleportation flag.
	 */
	private boolean teleporting = false;

	/**
	 * The characters's equipment bonuses.
	 */
	private final CharacterBonuses bonuses = new CharacterBonuses(this);

	/**
	 * The walking queue.
	 */
	private final WalkingQueue walkingQueue = new WalkingQueue(this);

	/**
	 * The first direction.
	 */
	private Direction firstDirection = Direction.NONE;

	/**
	 * The second direction.
	 */
	private Direction secondDirection = Direction.NONE;

	/**
	 * The current position of this character.
	 */
	private Position position;

	/**
	 * A list of active prayers.
	 */
	private final List<Prayers> prayers = new ArrayList<Prayers>();

	/**
	 * A set of {@link SynchronizationBlock}s.
	 */
	private SynchronizationBlockSet blockSet = new SynchronizationBlockSet();

	/**
	 * The character's current action.
	 */
	private Action<?> action; // TODO

	/**
	 * The character's inventory.
	 */
	private final Inventory inventory = new Inventory(InventoryConstants.INVENTORY_CAPACITY);

	/**
	 * The character's equipment.
	 */
	private final Inventory equipment = new Inventory(InventoryConstants.EQUIPMENT_CAPACITY, StackMode.STACK_ALWAYS);

	/**
	 * The character's bank.
	 */
	private final Inventory bank = new Inventory(InventoryConstants.BANK_CAPACITY, StackMode.STACK_ALWAYS);

	/**
	 * The character's skill set.
	 */
	private SkillSet skillSet = new SkillSet();

	/**
	 * The character's current region.
	 */
	private Region region = null;

	/**
	 * The default energy level.
	 */
	private int runEnergy = 100;

	/**
	 * The prayer drain.
	 */
	private int prayerDrain = 0;

	/**
	 * The last position of the player.
	 */
	private Position lastPosition;

	/**
	 * Flag indicating if this character moved since the last sync.
	 */
	private int moved = 0;

	/**
	 * Creates a new character with the specified initial position.
	 * @param position The initial position of this character.
	 */
	public Character(Position position) {
		super(position);
		this.position = position;
		this.lastPosition = position;
		init();
	}

	/**
	 * Add health (eating).
	 * @param health the health
	 */
	public void addHealth(int health) {
		final int temp = health + getHealth() > getHealthMax() ? getHealthMax() : health + getHealth();
		setHealth(temp);
	}

	/**
	 * Decreases the moving size.
	 */
	public void decrementMoving() {
		if (moved > 0)
			moved--;
	}

	/**
	 * Called when a character is removed from the world.
	 */
	public abstract void exit();

	/**
	 * Gets the character's bank.
	 * @return The character's bank.
	 */
	public Inventory getBank() {
		return bank;
	}

	/**
	 * Gets the {@link SynchronizationBlockSet}.
	 * @return The block set.
	 */
	public SynchronizationBlockSet getBlockSet() {
		return blockSet;
	}

	/**
	 * Gets the players equipment bonuses.
	 * @return The players equipment bonuses.
	 */
	public CharacterBonuses getBonuses() {
		return bonuses;
	}

	/**
	 * Gets the directions as an array.
	 * @return A zero, one or two element array containing the directions (in
	 * order).
	 */
	public Direction[] getDirections() {
		if (firstDirection != Direction.NONE) {
			if (secondDirection != Direction.NONE)
				return new Direction[] { firstDirection, secondDirection };
			else
				return new Direction[] { firstDirection };
		}
		else
			return Direction.EMPTY_DIRECTION_ARRAY;
	}

	/**
	 * Gets the character's equipment.
	 * @return The character's equipment.
	 */
	public Inventory getEquipment() {
		return equipment;
	}

	/**
	 * Gets the first direction.
	 * @return The first direction.
	 */
	public Direction getFirstDirection() {
		return firstDirection;
	}

	/**
	 * Get the players health.
	 * @return {@link Integer}
	 */
	public int getHealth() {
		return getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
	}

	/**
	 * The max health.
	 * @return {@link Integer}
	 */
	public int getHealthMax() {
		return getSkillSet().getSkill(Skill.HITPOINTS).getMaximumLevel();
	}

	/**
	 * Gets the index of this character.
	 * @return The index of this character.
	 */
	public int getIndex() {
		synchronized (this) {
			return index;
		}
	}

	/**
	 * Gets the character's inventory.
	 * @return The character's inventory.
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Gets the last position.
	 * @return The last position.
	 */
	public Position getLastPosition() {
		return lastPosition;
	}

	/**
	 * Gets the position of this character.
	 * @return The position of this character.
	 */
	@Override
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the prayer drain.
	 * @return The prayer drain.
	 */
	public int getPrayerDrain() {
		return prayerDrain;
	}

	/**
	 * Gets the list of prayers.
	 * @return The list of prayers.
	 */
	public List<Prayers> getPrayers() {
		return prayers;
	}

	/**
	 * Gets the region of this character.
	 * @return The region of this character.
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * Return the energy level.
	 * @return {@link Integer}
	 */
	public int getRunEnergy() {
		return runEnergy;
	}

	/**
	 * Gets the second direction.
	 * @return The second direction.
	 */
	public Direction getSecondDirection() {
		return secondDirection;
	}

	/**
	 * Gets the character's skill set.
	 * @return The character's skill set.
	 */
	public SkillSet getSkillSet() {
		return skillSet;
	}

	/**
	 * Gets the walking queue.
	 * @return The walking queue.
	 */
	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

	/**
	 * Flag indicating if this character has moved.
	 * @return True if the character has moved, false if otherwise.
	 */
	public boolean hasMoved() {
		return moved > 0;
	}

	/**
	 * Initialises this character.
	 */
	private void init() {
		skillSet.addListener(new HitpointSkillListener(this));
		skillSet.addListener(new PrayerSkillListener(this));
		World.getWorld().schedule(new SkillNormalizationTask(this));
	}

	/**
	 * Checks if this character is active.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isActive() {
		return index != -1;
	}

	/**
	 * Checks if this user is a controllable player.
	 * @return True if this character is being controlled, false if otherwise.
	 */
	public abstract boolean isControlling();

	/**
	 * Is the character dead.
	 * @return {@link Boolean}
	 */
	public boolean isDead() {
		return getHealth() <= 0;
	}

	/**
	 * Checks if this player is currently teleporting.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isTeleporting() {
		return teleporting;
	}

	/**
	 * Plays the specified animation.
	 * @param animation The animation.
	 */
	public void playAnimation(Animation animation) {
		if (animation != null)
			blockSet.add(SynchronizationBlock.createAnimationBlock(animation));
	}

	/**
	 * Plays the specified graphic.
	 * @param graphic The graphic.
	 */
	public void playGraphic(Graphic graphic) {
		if (graphic != null)
			blockSet.add(SynchronizationBlock.createGraphicBlock(graphic));
	}

	/**
	 * Called when this character has been done updating.
	 */
	public abstract void pulse();

	/**
	 * Resets the block set.
	 */
	public void resetBlockSet() {
		blockSet = new SynchronizationBlockSet();
	}

	/**
	 * Sends an {@link Event} to either:
	 * <ul>
	 * <li>The client if this {@link Character} is a {@link Player}.</li>
	 * <li>The AI routines if this {@link Character} is an NPC</li>
	 * </ul>
	 * @param event The event.
	 */
	public abstract void send(Event event);

	/**
	 * Sends a message to the character.
	 * @param message The message.
	 */
	public void sendMessage(String message) {
		send(new ServerMessageEvent(message));
	}

	/**
	 * Sets the next directions for this character.
	 * @param first The first direction.
	 * @param second The second direction.
	 */
	public void setDirections(Direction first, Direction second) {
		this.firstDirection = first;
		this.secondDirection = second;
	}

	/**
	 * Set the characters health.
	 * @param health the new health
	 */
	public void setHealth(int health) {
		getSkillSet().setSkill(Skill.HITPOINTS,
				new Skill(getSkillSet().getSkill(Skill.HITPOINTS).getExperience(), health, getHealthMax()));
	}

	/**
	 * Sets the index of this character.
	 * @param index The index of this character.
	 */
	public void setIndex(int index) {
		synchronized (this) {
			this.index = index;
		}
	}

	/**
	 * Sets the last position.
	 * @param lastPosition The last position.
	 */
	public void setLastPosition(Position lastPosition) {
		this.lastPosition = lastPosition;
	}

	/**
	 * Sets the moved flag.
	 * @param moved The moving type.
	 */
	public void setMoved(int moved) {
		this.moved = moved;
	}

	/**
	 * Sets the position of this character.
	 * @param position The position of this character.
	 */
	public void setPosition(Position position) {
		final Region region = World.getWorld().getRegionManager().getRegionByPosition(position);
		if (getRegion() != null) {
			if (getRegion() != region) {
				getRegion().remove(this);
				setRegion(region);
				region.add(this);
			}
		}
		else {
			setRegion(region);
			region.add(this);
		}
		if (moved == 0)
			moved = 1;
		this.position = position;
	}

	/**
	 * Sets the prayer drain.
	 * @param prayerDrain The prayer drain.
	 */
	public void setPrayerDrain(int prayerDrain) {
		this.prayerDrain = prayerDrain;
	}

	/**
	 * Sets the region of this character.
	 * @param region The region of this character.
	 */
	public void setRegion(Region region) {
		this.region = region;
	}

	/**
	 * Set the energy level.
	 * @param runEnergy the new run energy
	 */
	public void setRunEnergy(int runEnergy) {
		this.runEnergy = runEnergy;
	}

	/**
	 * Sets the skill set.
	 * @param skillSet The skill set.
	 */
	protected void setSkillSet(SkillSet skillSet) {
		this.skillSet = skillSet;
	}

	/**
	 * Sets the teleporting flag.
	 * @param teleporting the new teleporting {@code true} if the player is
	 * teleporting, {@code false} if not.
	 */
	public void setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
	}

	/**
	 * Starts a new action, stopping the current one if it exists.
	 * @param action The new action.
	 * @return A flag indicating if the action was started.
	 */
	public boolean startAction(Action<?> action) {
		if (this.action != null) {
			if (this.action.equals(action))
				return false;
			stopAction();
		}
		this.action = action;
		World.getWorld().schedule(action);
		return true; // TODO maybe this should be incorporated into the action
		// class itself?
	}

	/**
	 * Turns the character to face the specified position.
	 * @param id The entity id.
	 */
	public void startFacing(int id) {
		blockSet.add(SynchronizationBlock.createTurnToEntityBlock(id));
	}

	/**
	 * Stops the current action.
	 */
	public void stopAction() {
		if (action != null) {
			action.stop();
			action = null;
		}
	}

	/**
	 * Stops the current animation.
	 */
	public void stopAnimation() {
		playAnimation(Animation.STOP_ANIMATION);
	}

	/**
	 * Stops facing a entity.
	 */
	public void stopFacing() {
		startFacing(65535);
	}

	/**
	 * Stops the current graphic.
	 */
	public void stopGraphic() {
		playGraphic(Graphic.STOP_GRAPHIC);
	}

	/**
	 * Teleports this character to the specified position, setting the
	 * appropriate flags and clearing the walking queue.
	 * @param position The position.
	 */
	public void teleport(Position position) {
		teleport(position, false);
	}

	/**
	 * Teleports this character to the specified position, setting the
	 * appropriate flags and clearing the walking queue.
	 * @param position The position.
	 * @param action True if starting the teleport action, false if not.
	 */
	public void teleport(Position position, boolean action) {
		if (action) {
			startAction(new TeleportAction(this, position));
			return;
		}
		this.teleporting = true;
		setPosition(position);
		this.walkingQueue.clear();
		this.stopAction();
	}

	/**
	 * Turns the character to face the specified position.
	 * @param position The position to face.
	 */
	public void turnTo(Position position) {
		blockSet.add(SynchronizationBlock.createTurnToPositionBlock(position));
	}

	/**
	 * Reinserts the character into the correct region.
	 */
	@SuppressWarnings("unused")
	private void updateCharacterIntoRegion() {
		boolean update = true;
		final RegionManager manager = World.getWorld().getRegionManager();
		final Region oldRegion = region;
		final Region newRegion = manager.getRegionByPosition(position);
		if (oldRegion != null)
			if (oldRegion != newRegion)
				oldRegion.remove(this);
			else
				update = false;
		if (update) {
			newRegion.add(this);
			region = newRegion;
		}
	}
}
