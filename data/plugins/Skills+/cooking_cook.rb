require 'java'
java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.EquipmentConstants'
java_import 'java.security.SecureRandom'
java_import 'org.apollo.game.event.impl.SetInterfaceTextEvent'
java_import 'org.apollo.game.event.impl.SetInterfaceItemModelEvent'

COOK_ANIMATION = Animation.new(833)
COOK_FIRE = Animation.new(897)
COOK_GAUNTLETS = 775

class CookingAction < DistancedAction

 attr_reader :item, :event, :gauntlets, :cooked, :count, :done

  def initialize(player, item, event, count)
    super 2, true, player, event.position, 1
    @item = item
    @event = event
    @count = count
    @done = 0
  end

  def getSuccess(burnBonus, levelReq, stopBurn)
    if event.get_object == 2728
      burnBonus += 3.0
    else
      if event.get_object == 114
        burnBonus += 3.0
      end
    end
    burn_chance = (55.0 - burnBonus)
    cook_level = character.skill_set.get_skill(Skill::COOKING).maximum_level
    lev_needed = levelReq
    burn_stop = stopBurn
    multi_a = (burn_stop - lev_needed)
    burn_dec = (burn_chance / multi_a)
    multi_b = (cook_level - lev_needed)
    burn_chance -= (multi_b * burn_dec)
    randNum = SecureRandom.new().next_double * 100.0
    return burn_chance <= randNum
  end

  def has_gauntlets
    hand = character.get_equipment.get(EquipmentConstants::HANDS)
    skill = character.skill_set.get_skill(Skill::COOKING).maximum_level
    if hand != nil and hand.get_id == COOK_GAUNTLETS
      @gauntlets = true
      @cooked = getSuccess(5.0, item.level, item.burnlevel)
    else
      @gauntlets = false
      @cooked = getSuccess(0.0, item.level, item.burnlevel)
    end
  end

  def start
    if item.level > character.skill_set.get_skill(Skill::COOKING).maximum_level
      character.send_message "You need a Cooking level of #{item.level} to cook this."
      stop
      return
    end
    has_gauntlets
    character.turn_to event.position
    character.play_animation (event.get_object == 2732 or event.get_object == 9374) ? COOK_FIRE : COOK_ANIMATION
    character.inventory.remove item.uncook
  end

  def executeAction
    if character.inventory.contains item.uncook
      start
      if cooked then cook else burn end
      @done += 1
    else
      stop
    end
    if done == count
      stop
      return
    end
  end

  def cook
    if character.inventory.add item.cook
      item_def = ItemDefinition.for_id item.cook
      name = item_def.name.sub(/ item$/, "").downcase
      character.send_message "You cook the #{name}."
      character.skill_set.add_experience Skill::COOKING, item.exp
    else
      stop
    end
  end

  def burn
    if character.inventory.add item.burnt
      character.send_message "You burn the fish."
    else
      stop
    end
  end

  def equals(other)
    return (get_class == other.get_class)
  end

end

class CookingDialogListener
  java_implements 'org.apollo.game.model.inter.EnterAmountListener'

  attr_reader :player, :item, :event

  def initialize(player, item, event)
    @player = player
    @item = item
    @event = event
  end

  def amountEntered(amount)
    player.get_interface_set.close
    player.start_action CookingAction.new(player, item, event, amount)
  end
end

class CookingListener
  java_implements 'org.apollo.game.model.inter.dialog.DialogueListener'

  attr_reader :item, :event

  def initialize(item, event)
    @item = item
    @event = event
  end

  def buttonClicked(player, button)
    click = button_to_id button
    if click == -1
      player.get_interface_set.open_enter_amount_dialog CookingDialogListener.new(player, item, event)
    elsif click != 0
      player.start_action CookingAction.new(player, item, event, click)
      player.get_interface_set.close
    end
  end

  def interfaceClosed(player, manually)
  end

  def continued(player)
  end

  def button_to_id(button)
    if button == 13720
      return 1
    elsif button == 13719
      return 5
    elsif button == 13718
      return -1
    elsif button == 13717
      return 28
    else
      return 0
    end
  end
end

def copen_window(player, item, event)
  item_def = ItemDefinition.for_id item.cook # TODO: split off into some method
  player.send SetInterfaceItemModelEvent.new(13716, item.cook, 250)
  player.get_interface_set.open_dialogue CookingListener.new(item, event), 1743
end

on :event, :item_used_on_object do |ctx, player, event|
  if event.get_object == 2728 or event.get_object == 114 or event.get_object == 2732 or event.get_object == 9374
    item = FOODS_[event.get_id]
    if item != nil
      copen_window player, item, event
      ctx.break_handler_chain
    end
  end
end
