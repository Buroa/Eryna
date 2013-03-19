package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOptionEvent;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An event handler which equips items.
 * @author Graham
 */
public final class EquipEventHandler extends EventHandler<ItemOptionEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemOptionEvent event) {
		if (event.getOption() == 2 && event.getInterfaceId() == SynchronizationInventoryListener.INVENTORY_ID) {
			final int slot = event.getSlot();
			final Item item = player.getInventory().get(slot);
			final ItemDefinition itemDef = item.getDefinition();
			final EquipmentDefinition equipDef = EquipmentDefinition.forId(item.getId());
			if (equipDef == null) {
				ctx.breakHandlerChain();
				return;
			}
			final SkillSet skillSet = player.getSkillSet();
			if (skillSet.getSkill(Skill.ATTACK).getMaximumLevel() < equipDef.getAttackLevel()) {
				player.sendMessage("You need an Attack level of " + equipDef.getAttackLevel() + " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}
			if (skillSet.getSkill(Skill.STRENGTH).getMaximumLevel() < equipDef.getStrengthLevel()) {
				player.sendMessage("You need a Strength level of " + equipDef.getStrengthLevel()
						+ " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}
			if (skillSet.getSkill(Skill.DEFENCE).getMaximumLevel() < equipDef.getDefenceLevel()) {
				player.sendMessage("You need a Defence level of " + equipDef.getDefenceLevel() + " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}
			if (skillSet.getSkill(Skill.RANGED).getMaximumLevel() < equipDef.getRangedLevel()) {
				player.sendMessage("You need a Ranged level of " + equipDef.getRangedLevel() + " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}
			if (skillSet.getSkill(Skill.MAGIC).getMaximumLevel() < equipDef.getMagicLevel()) {
				player.sendMessage("You need a Magic level of " + equipDef.getMagicLevel() + " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}
			final Inventory inventory = player.getInventory();
			final Inventory equipment = player.getEquipment();
			final int equipmentSlot = equipDef.getSlot();
			// TODO: equip event decoder for 317, and remove event decoder for
			// both
			// TODO: put all this into another method somewhere
			// check if there is enough space for a two handed weapon
			if (equipDef.isTwoHanded()) {
				final Item currentShield = equipment.get(EquipmentConstants.SHIELD);
				if (currentShield != null)
					if (inventory.freeSlots() < 1) {
						inventory.forceCapacityExceeded();
						ctx.breakHandlerChain();
						return;
					}
			}
			// check if a shield is being added with a two handed weapon
			boolean removeWeapon = false;
			if (equipmentSlot == EquipmentConstants.SHIELD) {
				final Item currentWeapon = equipment.get(EquipmentConstants.WEAPON);
				if (currentWeapon != null) {
					final EquipmentDefinition weaponDef = EquipmentDefinition.forId(currentWeapon.getId());
					if (weaponDef.isTwoHanded()) {
						if (inventory.freeSlots() < 1) {
							inventory.forceCapacityExceeded();
							ctx.breakHandlerChain();
							return;
						}
						removeWeapon = true;
					}
				}
			}
			final Item previous = equipment.get(equipmentSlot);
			if (itemDef.isStackable() && previous != null && previous.getId() == item.getId()) {
				// we know the item is there, so we can let the inventory class
				// do its stacking magic
				inventory.remove(item);
				final Item tmp = equipment.add(item);
				if (tmp != null)
					inventory.add(tmp);
			}
			else {
				// swap the weapons around
				final Item tmp = equipment.reset(equipmentSlot);
				equipment.set(equipmentSlot, item);
				inventory.reset(slot);
				if (tmp != null)
					inventory.add(tmp);
			}
			// remove the shield if this weapon is two handed
			if (equipDef.isTwoHanded())
				if (equipment.get(EquipmentConstants.SHIELD) != null) {
					final Item tmp = equipment.reset(EquipmentConstants.SHIELD);
					inventory.add(tmp);
				}
			if (removeWeapon) {
				final Item tmp = equipment.reset(EquipmentConstants.WEAPON);
				// we know tmp will not be null from the check about
				inventory.add(tmp);
			}
			ctx.breakHandlerChain();
		}
	}
}
