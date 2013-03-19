require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.EquipmentConstants'
java_import 'org.apollo.game.model.def.ItemDefinition'
java_import 'org.apollo.game.model.obj.DynamicGameObject'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.def.ObjectDefinition'
java_import 'org.apollo.game.scheduling.ScheduledTask'

PROSPECT_PULSES = 3
ORE_SIZE = 1

# TODO: finish implementing this
class MiningAction < DistancedAction

  attr_reader :position, :ore, :started, :counter, :id

  def initialize(character, position, ore, id)
    super 0, true, character, position, (id == 2491) ? 6 : ORE_SIZE
    @position = position
    @ore = ore
    @started = false
    @id = id
  end

  def find_pickaxe
    PICKAXE_IDS.each do |id|
      if character.equipment.contains id
        return PICKAXES[id]
      elsif character.inventory.contains id
        return PICKAXES[id]
      end
    end
    return nil
  end

  def get_mining_success(pickaxe, level)
    level = level * 2
    required = ore.level
    second = pickaxe.level / required
    randNum = rand(second)
    first = ((randNum * level) / 3)
    randNumS = SecureRandom.new().next_double * 100.0
    return first <= randNumS
  end

  # starts the mining animation, sets counters/flags and turns the character to
  # the ore
  def start_mine(pickaxe)
    @started = true
    character.send_message "You swing your pick at the rock."
    character.turn_to position
    character.play_animation pickaxe.animation
    @counter = pickaxe.pulses
  end

  def executeAction
    skills = character.skill_set
    level = skills.get_skill(Skill::MINING).maximum_level # TODO: is using max level correct?
    free = character.inventory.free_slots
    expired = EXPIRED[position]

    # checks if the ore is expired
    if expired != nil
      if expired
        stop
        return
      end
    end

    # Looks for free slots
    if free < 1
      character.send_message "Not enough inventory space"
      stop
      return
    end

    pickaxe = find_pickaxe

    # verify the player can mine with their pickaxe
    if not (pickaxe != nil and level >= pickaxe.level)
      character.send_message "You do not have a pickaxe for which you have the level to use."
      stop
      return
    end

    # verify the player can mine the ore
    if ore.level > level
      character.send_message "You do not have the required level to mine this rock."
      stop
      return
    end

    # check if we need to kick start things
    if not started
      start_mine(pickaxe)
    else
      if counter == 0
        character.play_animation pickaxe.animation
        @counter = pickaxe.pulses
        if get_mining_success pickaxe, level
          character.inventory.add ore.id
          ore_def = ItemDefinition.for_id @ore.id # TODO: split off into some method
          name = ore_def.name.sub(/ ore$/, "").downcase
          character.send_message "You manage to mine some #{name}."
          skills.add_experience Skill::MINING, ore.exp
          if ore.respawn != -1 then
            expire position
            stop
          end
        else
          @counter = pickaxe.pulses
        end
      else
        @counter -= 1
      end
    end

  end

  def stop
    character.stop_animation
    super
  end

  def expire(position)
    ore.objects.each do |obj, expired_obj|
      if obj == id
        append_expired position

        # okay since the system is different, we need to add a dynamic object
        # with the delete flag set, and then we can set the replace flag to have the two in one
        # once the pulses have been set, it will delete the dynamic object and replace with the old static object
        old = World.world.region_manager.get_region_by_position(position).get_object(position)
        ex_game_object = DynamicGameObject.new expired_obj, position, 10, old != nil ? old.rotation : 0, true, true

        players = World.world.get_player_repository.size
        pulses = respawn_pulses(ore.respawn, players)
        World.world.region_manager.get_chunk_by_position(position).add(ex_game_object, pulses)
        schedule pulses do |task|
          EXPIRED[position] = false
          task.stop
        end
      end
    end
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position and @ore == other.ore)
  end

end

class ExpiredProspectingAction < DistancedAction
  attr_reader :position

  def initialize(character, position)
    super 0, true, character, position, ORE_SIZE
  end

  def executeAction
    character.send_message "There is currently no ore available in this rock."
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end

end

class ProspectingAction < DistancedAction
  attr_reader :position, :ore

  def initialize(character, position, ore)
    super PROSPECT_PULSES, true, character, position, (ore.id == 1436) ? 6 : ORE_SIZE
    @position = position
    @ore = ore
    @started = false
  end

  def executeAction
    if not @started
      @started = true

      character.send_message "You examine the rock for ores..."
      character.turn_to @position
    else
      ore_def = ItemDefinition.for_id @ore.id
      name = ore_def.name.sub(/ ore$/, "").downcase

      character.send_message "This rock contains #{name}."

      stop
    end
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position and @ore == other.ore)
  end
end

on :event, :object_action do |ctx, player, event|
  if event.option == 1
    ore = ORES[event.id]
    if ore != nil
      player.startAction MiningAction.new(player, event.position, ore, event.id)
      ctx.break_handler_chain
    end
  end
end

on :event, :object_action do |ctx, player, event|
  if event.option == 2
    ore = ORES[event.id]
    if ore != nil
      player.startAction ProspectingAction.new(player, event.position, ore)
    else
      if EXPIRED_ORES[event.id] != nil
        player.startAction ExpiredProspectingAction.new(player, event.position)
        ctx.break_handler_chain
      end
    end
  end
end

on :event, :npc_option do |ctx, player, event|
  if event.option == 4
    if event.npc.id == 553
      player.teleport Position.new(2910, 4832), false
      ctx.break_handler_chain
    end
  end
end
