require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.EquipmentConstants'
java_import 'org.apollo.game.model.def.ItemDefinition'
java_import 'org.apollo.game.model.obj.DynamicGameObject'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.def.ObjectDefinition'
java_import 'org.apollo.game.scheduling.ScheduledTask'

LOG_SIZE = 2

class WoodcuttingAction < DistancedAction

  attr_reader :position, :log, :started, :counter, :id

  def initialize(character, position, log, id, log_size)
    super 0, true, character, position, log_size
    @position = position
    @log = log
    @started = false
    @id = id
    @counter = 0
  end

  # Finds if you have the hatchet
  def find_hatchet
    HATCHET_ID.each do |id|
      if character.equipment.contains id
        return HATCHET[id]
      elsif character.inventory.contains id
        return HATCHET[id]
      end
    end
    return nil
  end

  # The Chopping action/animation/message
  def start_chopping(hatchet)
    character.send_message "You swing your hatchet at the tree."
    character.turn_to position
    @started = true
  end

  # The chance of getting a log
  def get_success(hatchet, level)
    level = level * 2
    required = log.level
    second = hatchet.level / required
    randNum = rand(second)
    first = ((randNum * level) / 3)
    randNumS = SecureRandom.new().next_double * 100.0
    return first <= randNumS
  end

  def executeAction
    skills = character.skill_set
    level = skills.get_skill(Skill::WOODCUTTING).maximum_level # TODO: is using max level correct?
    free = character.inventory.free_slots
    expired = EXPIREDD[position]

    # checks if the tree is expired
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

    hatchet = find_hatchet

    # verify the player has a hatchet
    if hatchet == nil
      character.send_message "You do not have a hatchet to cut this tree with."
      stop
      return
    end

    # verify the player can chop with their axe
    if not (level >= hatchet.level)
      character.send_message "You do not have the correct hatchet with your woodcutting level."
      stop
      return
    end

    # verify the player can chop the tree
    if log.level > level
      character.send_message "You do not have the required level to cut this tree."
      stop
      return
    end
  
    if not started
      start_chopping(hatchet)
    else
      if counter == 0
        @counter = hatchet.pulses
        if get_success hatchet, level
          character.inventory.add log.id
          log_def = ItemDefinition.for_id log.id
          name = log_def.name.sub(/ log$/, "").downcase
          character.send_message "You manage to get some #{name}."
          skills.add_experience Skill::WOODCUTTING, log.exp
          if rand(4) == 1
            expirew position
            stop
            return
          end
          @counter = hatchet.pulses
        end
      else
        @counter -= 1
      end
    end

    if counter % 4 == 0 then character.play_animation hatchet.animation end
  end

  def stop
    character.stop_animation
    super
  end

  def expirew(position)
    log.objects.each do |obj, expired_obj|
      if obj == id
        append_expiredd position

        # okay since the system is different, we need to add a dynamic object
        # with the delete flag set, and then we can set the replace flag to have the two in one
        # once the pulses have been set, it will delete the dynamic object and replace with the old static object
        old = World.world.region_manager.get_region_by_position(position).get_object(position)
        ex_game_object = DynamicGameObject.new expired_obj, position, 10, old != nil ? old.rotation : 0, true, true

        players = World.world.get_player_repository.size
        pulses = respawn_pulses(log.respawn, players)
        World.world.region_manager.get_chunk_by_position(position).add(ex_game_object, pulses)
        schedule pulses do |task|
          EXPIREDD[position] = false
          task.stop
        end
      end
    end
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position and @log == other.log)
  end

end

on :event, :object_action do |ctx, player, event|
  if event.option == 1
    log = LOGS[event.id]
    free = player.inventory.free_slots
    if log != nil
      log_size = ObjectDefinition.for_id(event.id).size
      player.startAction WoodcuttingAction.new(player, event.position, log, event.id, log_size)
      ctx.break_handler_chain
    end
  end
end
