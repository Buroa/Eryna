require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.EquipmentConstants'
java_import 'org.apollo.game.model.def.ItemDefinition'
java_import 'org.apollo.game.event.impl.SetInterfaceItemModelEvent'
java_import 'org.apollo.game.event.impl.OpenInterfaceDialogueEvent'
java_import 'org.apollo.game.model.Animation'

SMITHING_ANIMATION = Animation.new(899)

# TODO Finish Smith X
class SmithingAction < DistancedAction
  attr_reader :position, :bar, :smithed, :second

  def initialize(player, position, bar)
    super 3, true, player, position, 1
    @position = position
    @bar = bar
    @smithed = 0
    @second = false
  end

  def executeAction
    skills = character.skill_set
    level = skills.get_skill(Skill::SMITHING).maximum_level # TODO: is using max level correct?

    # count the smiths
    if smithed == bar.count
      stop
      return
    end

    # verify the player can smith the ore
    if bar.level > level
      character.send_message "You need a Smithing level of at least #{bar.level} to smelt this."
      stop
      return
    end

    # verify the player can smith
    if not (character.inventory.contains bar.ore_1)
      character.send_message "You do not have the required ores to smelt this."
      stop
      return
    end

    # verify the player can smith
    if bar.ore_2_req > 0
      if not (character.inventory.contains(bar.ore_2, bar.ore_2_req))
        character.send_message "You do not have the required ores to smelt this."
        stop
        return
      else
        @second = true
      end
    end

    # start smithing fella's
    character.play_animation SMITHING_ANIMATION
    character.inventory.remove bar.ore_1, 1
    if @second
      character.inventory.remove bar.ore_2, bar.ore_2_req
    end
    character.inventory.add bar.id
    skills.add_experience Skill::SMITHING, bar.exp
    @smithed += 1

  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position and @bar == other.bar)
  end
end

class SmithingListener
  java_implements 'org.apollo.game.model.inter.dialog.DialogueListener'

  attr_reader :position

  def initialize(position)
    @position = position
  end

  def buttonClicked(player, button)
    bar = BAR[button]
    if bar != nil
      player.start_action SmithingAction.new(player, position, bar)
    end
    player.get_interface_set.close
  end

  def interfaceClosed(player, manually)
  end

  def continued(player)
  end

end

on :event, :object_action do |ctx, player, event|
  if event.option == 2 and (event.id == 11666 or event.id == 2781 or event.id == 3994)
    player.turn_to event.get_position
    player.send SetInterfaceItemModelEvent.new(2405, 2349, 150)
    player.send SetInterfaceItemModelEvent.new(2406, 2351, 150)
    player.send SetInterfaceItemModelEvent.new(2407, 2355, 150)
    player.send SetInterfaceItemModelEvent.new(2409, 2353, 150)
    player.send SetInterfaceItemModelEvent.new(2410, 2357, 150)
    player.send SetInterfaceItemModelEvent.new(2411, 2359, 150)
    player.send SetInterfaceItemModelEvent.new(2412, 2361, 150)
    player.send SetInterfaceItemModelEvent.new(2413, 2363, 150)
    player.get_interface_set.open_dialogue SmithingListener.new(event.get_position), 2400
    ctx.break_handler_chain
  end
end