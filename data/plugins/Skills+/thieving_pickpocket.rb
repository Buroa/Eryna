require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.event.impl.ServerMessageEvent'

PICKPOCKET_ANIMATION = Animation.new(881)
PICKPOCKET_GRAPHIC = Graphic.new(348, 0, 100)
STEAL_ANIMATION = Animation.new(0x340)

class StealAction < DistancedAction

  attr_reader :started, :stall, :position

  def initialize(player, stall, position)
    super 2, true, player, position, 2
    @stall = stall
    @position = position
    @started = false
  end

  def start_steal
    @started = true
    character.turn_to position
    character.send_message "You take from the stall.."
    character.play_animation STEAL_ANIMATION
  end
  
  def executeAction
    skills = character.skill_set
    level = skills.get_skill(Skill::THIEVING).maximum_level

    if stall.level > level
      character.send_message "You do not have the required level to thieve this stall."
      stop
      return
    end

    if not started
      start_steal
    else
      if rand(2) == 1
        item = stall.item.sample
        if character.inventory.add item
          item_def = ItemDefinition.for_id item.id
          name = item_def.name.sub(/ object$/, "").downcase
          character.send_message "and recieve #{name}."
          character.skill_set.add_experience Skill::THIEVING, stall.exp
        else
          character.send_message "Full inventory."
        end
      else
        character.send_message "You fail to thieve the stall."
      end
      stop
    end
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end

end

class PickpocketAction < DistancedAction

 attr_reader :started, :npc, :npcz

  def initialize(player, npc, npcz)
    super 3, true, player, npc.get_position, 1
    @started = false
    @npc = npc
    @npcz = npcz
  end

  def start_thieve
    character.turn_to npc.get_position
    character.send_message "You attempt to pick the man's pocket."
    character.play_animation PICKPOCKET_ANIMATION
    @started = true
  end
  
  def executeAction
    if not started
      start_thieve
    else
      if rand(2) == 1
        item = npcz.loot.sample
        character.inventory.add item
        character.skill_set.add_experience Skill::THIEVING, 17
        character.send_message "You pick the man's pocket."
      else
        npc.turn_to character.get_position
        npc.send_message "What do you think you're doing?"
        character.play_graphic PICKPOCKET_GRAPHIC
        character.get_walking_queue.stop 8
        character.send_message "You fail to pick the man's pocket."
      end
      stop
    end
  end

  def equals(other)
    return (get_class == other.get_class)
  end

end

on :event, :npc_option do |ctx, player, event|
  if event.get_option == 3
    npcz = NPCZ[event.get_npc.get_id]
    if npcz != nil
      player.startAction PickpocketAction.new(player, event.get_npc, npcz)
      ctx.break_handler_chain
    end
  end
end

on :event, :object_action do |ctx, player, event|
  if event.option == 2
    stall = STALLS[event.id]
    if stall != nil
      player.start_action StealAction.new(player, stall, event.position)
      ctx.break_handler_chain
    end
  end
end
