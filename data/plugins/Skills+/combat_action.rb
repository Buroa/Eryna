require 'java'
java_import 'org.apollo.game.action.MovingDistancedCharacterAction'
java_import 'org.apollo.game.model.EquipmentConstants'

class CombatDistancedAction < MovingDistancedCharacterAction
  attr_reader :victim, :started, :rwalk, :retal

  def initialize(source, victim, time, size)
    super time, true, source, victim, size
    @victim = victim
    @started = false
    @rwalk = false
    @retal = false
  end

  def executeAction
    # check if both are active
    if not victim.active or not character.active
      stop
      return
    end

    # check if we are in the wild
    if victim.is_controlling and not victim.position.is_in_wilderness
      if character.is_controlling
        character.send_message "#{victim.name} is not in the wilderness!"
        stop
        return
      end
    end

    # check if any of the combatives died
    if get_combat_set(victim).defunct or get_combat_set(character).defunct
      stop
      return
    end

    # set the combat set values
    if not @started
      if get_combat_set(victim).embattled
        if character.is_controlling then character.send_message "#{victim.name.capitalize} is already embattled!" end
        stop
        return
      else
        character.start_facing(victim.is_controlling ? victim.index+32768 : victim.index)
        get_combat_set(character).set_attacking true
        get_combat_set(victim).set_embattled true
        battle_executed character, victim
      end
      @started = true
    end

    # check if npc random walk
    if not character.is_controlling
      if character.is_random_walking
        character.set_random_walking false
        @rwalk = true
      end
    end

    # all else is good
    executeCombat

    # check if we have to retaliate
    if self.running and not @retal
      if ((get_combat_set(victim).retaliate or not victim.is_controlling) and not get_combat_set(victim).attacking)

        # auto retal magic?? they need to manually do it?!
        if not is_using_range(victim)
          victim.start_action DefaultCombatDistancedAction.new(victim, character)
        else
          victim.start_action RangeDistancedAction.new(victim, character)
        end
      end
      @retal = true
    end

    # set the attack speed
    set_delay EquipmentConstants.get_attack_speed(character)

    # check the victim's health and if so execute the death action
    if victim.health < 1
      schedule_death character, victim
      death_executed character, victim
    end
  end

  def executeCombat
    # needs to be overridden
  end

  def damage_victim(hit)
    if victim.health > 0
      damage_character victim, hit
      return true
    end
    return false
  end

  def stop
    if @started
      get_combat_set(victim).set_embattled false
      character.stop_facing
    end
    get_combat_set(character).set_attacking false
    
    # set random walk again
    if not character.is_controlling
      character.set_random_walking @rwalk
    end
    super
  end

  def equals(other)
    return (get_class == other.get_class and @victim == other.victim)
  end
end