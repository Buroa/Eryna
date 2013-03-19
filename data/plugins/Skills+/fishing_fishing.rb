require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Animation'

class FishingAction < DistancedAction

  attr_reader :fish, :fished, :started, :position

  def initialize(player, fish, position)
    super 4, true, player, position, 1
    @fish = fish
    @fished = 0
    @started = false
    @position = position
  end

  def get_fish_success(fish_level, level)
    level = level * 2
    required = fish_level
    second = required / 2
    randNum = rand(second)
    first = ((randNum * level) / 3)
    return first > second
  end

  def executeAction
    skills = character.skill_set
    level = skills.skill(Skill::FISHING).maximum_level # TODO: is using max level correct?
    free = character.inventory.free_slots

    # Looks for free slots
    if free < 1
      character.send_message "Not enough inventory space"
      stop
      return
    end

    want = get_fish fish, level

    # verify level requirements
    if want == -1 or fish.level[want] > level
      character.send_message "You need a Fishing level of at least #{fish.level[want]} to fish this."
      stop
      return
    end

    # verify item requirements
    if not (character.inventory.contains fish.equipment)
      character.send_message "You do not have the required equipment to fish this."
      stop
      return
    end

    # verify item requirements
    if (fish.bait != -1) and not (character.inventory.contains fish.bait)
      character.send_message "You do not have the required bait to fish this."
      stop
      return
    end

    if not started
      character.turn_to position
      character.send_message "You attempt to catch a fish.."
      @started = true
    end

    # start fishing fella's
    if get_fish_success fish.level[want], level
      character.inventory.add fish.fish[want]
      character.send_message "You catch a fish."
      if fish.bait != -1 then character.inventory.remove fish.bait end
      skills.add_experience Skill::FISHING, fish.exp[want]
      if rand(20) == 1
        stop
        return
      end
    end

    character.play_animation Animation.new(fish.animation)
  end

  def stop
    character.stop_animation
    super
  end

  def get_fish(fish, level)
    tempInt = 0
    fish.level.each do |id|
      if level == id or level > id
        tempInt += 1
      end
    end
    return tempInt != -1 ? rand(tempInt) : -1
  end

  def equals(other)
    return (get_class == other.get_class and @fish == other.fish and @position == other.position)
  end

end

on :event, :npc_option do |ctx, player, event|
  fish = FISH[event.npc.id]
  if fish != nil and fish[event.option] != nil
    player.start_action FishingAction.new(player, fish[event.option], event.get_npc.get_position)
    ctx.break_handler_chain
  end
end