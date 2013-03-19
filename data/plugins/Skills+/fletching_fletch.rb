require 'java'
java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.event.impl.SetInterfaceTextEvent'
java_import 'org.apollo.game.event.impl.SetInterfaceItemModelEvent'

FLETCHING_ANIMATION = Animation.new(1248)

class FletchingAction < Action
  attr_reader :item, :fletched, :log, :count

  def initialize(player, item, log, count)
    super 2, true, player
    @item = item
    @log = log
    @count = count
    @fletched = 0
  end

  def execute
    skills = character.skill_set
    level = skills.skill(Skill::FLETCHING).maximum_level # TODO: is using max level correct?

    # verify counted fletches
    if fletched == count
      stop
      return
    end

    # verify level requirements
    if item.level > level
      character.send_message "You need a Fletching level of at least #{item.level} to fletch this."
      stop
      return
    end

    # verify item requirements
    if not (character.inventory.contains log)
      character.send_message "You do not have the required logs to fletch this."
      stop
      return
    end

    # start fletcing fella's
    character.play_animation FLETCHING_ANIMATION
    character.inventory.remove log, 1
    character.inventory.add item.item, item.item == 52 ? 5 : 1
    skills.add_experience Skill::FLETCHING, item.item == 52 ? 5 : item.xp
    @fletched += 1

  end

  def stop
    character.stop_animation
    super
  end

  def equals(other)
    return (get_class == other.get_class and @item == other.item and @log == other.log)
  end

end

class FletchingDialogListener
  java_implements 'org.apollo.game.model.inter.EnterAmountListener'

  attr_reader :player, :bow, :log

  def initialize(player, bow, log)
    @player = player
    @bow = bow
    @log = log
  end

  def amountEntered(amount)
    player.get_interface_set.close
    player.start_action FletchingAction.new(player, bow, log, amount)
  end
end

class FletchingListener
  java_implements 'org.apollo.game.model.inter.dialog.DialogueListener'

  attr_reader :log

  def initialize(log)
    @log = log
  end

  def buttonClicked(player, button)
    bow = BOWS[log][button]
    if bow != nil
      if bow.count == -1
        player.get_interface_set.open_enter_amount_dialog FletchingDialogListener.new(player, bow, log)
      else
        player.get_interface_set.close
        player.start_action FletchingAction.new(player, bow, log, bow.count)
      end
    end
  end

  def interfaceClosed(player, manually)
  end

  def continued(player)
  end

end

class StringingAction < Action
  attr_reader :item

  def initialize(player, item)
    super 0, true, player
    @item = item
  end

  def execute
    skills = character.skill_set
    level = skills.skill(Skill::FLETCHING).maximum_level # TODO: is using max level correct?

    # verify level requirements
    if item.level > level
      character.send_message "You need a Fletching level of at least #{item.level} to string this."
      stop
      return
    end

    # start fletcing fella's
    character.inventory.remove 1777, 1  
    character.inventory.remove item.bow, 1
    character.inventory.add item.item
    skills.add_experience Skill::FLETCHING, item.exp
    stop

  end

  def equals(other)
    return (get_class == other.get_class and @item == other.item)
  end

end

class ArrowAction < Action
  attr_reader :item

  def initialize(player, item)
    super 0, true, player
    @item = item
  end

  def execute
    skills = character.skill_set
    level = skills.skill(Skill::FLETCHING).maximum_level # TODO: is using max level correct?

    # verify level requirements
    if item.level > level
      character.send_message "You need a Fletching level of at least #{item.level} to make this."
      stop
      return
    end

    # verify item requirements
    if not (character.inventory.contains item.item_1)
      character.send_message "You do not have the required items to make this."
      stop
      return
    end

    # start fletcing fella's
    character.inventory.remove item.item_1, 1  
    character.inventory.remove item.item_2, 1
    character.inventory.add item.item
    skills.add_experience Skill::FLETCHING, item.xp
    stop

  end

  def equals(other)
    return (get_class == other.get_class and @item == other.item)
  end

end

class DartAction < Action
  attr_reader :item

  def initialize(player, item)
    super 0, true, player
    @item = item
  end

  def execute
    skills = character.skill_set
    level = skills.skill(Skill::FLETCHING).maximum_level # TODO: is using max level correct?

    # verify level requirements
    if item.level > level
      character.send_message "You need a Fletching level of at least #{item.level} to make this."
      stop
      return
    end

    # start fletcing fella's
    character.inventory.remove item.dart, 1
    character.inventory.remove 314, 1
    character.inventory.add item.item
    skills.add_experience Skill::FLETCHING, item.exp
    stop

  end

  def equals(other)
    return (get_class == other.get_class and @item == other.item)
  end

end

class BoltAction < Action
  attr_reader :item

  def initialize(player, item)
    super 0, true, player
    @item = item
  end

  def execute
    skills = character.skill_set
    level = skills.skill(Skill::FLETCHING).maximum_level # TODO: is using max level correct?

    # verify level requirements
    if item.level > level
      character.send_message "You need a Fletching level of at least #{item.level} to make this."
      stop
      return
    end

    # start fletcing fella's
    character.inventory.remove item.bolt, 1
    character.inventory.add item.item
    skills.add_experience Skill::FLETCHING, item.exp
    stop

  end

  def equals(other)
    return (get_class == other.get_class and @item == other.item)
  end

end

def open_window(player, item)
  short_def = ItemDefinition.for_id item.bow # TODO: split off into some method
  long_def = ItemDefinition.for_id item.model # TODO: split off into some method
  if item.bow == 839
    player.send SetInterfaceTextEvent.new(8879, "What would you like to make?")
    player.send SetInterfaceItemModelEvent.new(8885, 52, 190)
    player.send SetInterfaceTextEvent.new(8897, "Arrow Shafts")
  end
  player.send SetInterfaceTextEvent.new(item.bow == 839 ? 8889 : 8874, short_def.name)
  player.send SetInterfaceTextEvent.new(item.bow == 839 ? 8893 : 8878, long_def.name)
  player.send SetInterfaceItemModelEvent.new(item.bow == 839 ? 8884 : 8869, item.bow, 190)
  player.send SetInterfaceItemModelEvent.new(item.bow == 839 ? 8883 : 8870, item.model, 190)
  player.get_interface_set.open_dialogue FletchingListener.new(item.log), item.bow == 839 ? 8880 : 8866
end

on :event, :item_on_item do |ctx, player, event|
  primary = event.id
  secondary = event.target_id

  if primary == 946
    item = ITEMS[secondary]
    if item != nil
      open_window player, item
      ctx.break_handler_chain
    end
  end
end

on :event, :item_on_item do |ctx, player, event|
  primary = event.id
  secondary = event.target_id

  if primary == 1777
    item = UBOWS[secondary]
    if item != nil
      player.start_action StringingAction.new(player, item)
      ctx.break_handler_chain
    end
  end
end

on :event, :item_on_item do |ctx, player, event|
  primary = event.id
  secondary = event.target_id

  item = ARROWS[primary]
  if item != nil
    player.start_action ArrowAction.new(player, item)
    ctx.break_handler_chain
  end
end

on :event, :item_on_item do |ctx, player, event|
  primary = event.id
  secondary = event.target_id

  if primary == 314
    item = DARTS[secondary]
    if item != nil
      player.start_action DartAction.new(player, item)
      ctx.break_handler_chain
    end
  end
end

on :event, :item_on_item do |ctx, player, event|
  primary = event.id
  secondary = event.target_id

  if primary == 1755
    item = BOLTS[secondary]
    if item != nil
      player.start_action BoltAction.new(player, item)
      ctx.break_handler_chain
    end
  end
end