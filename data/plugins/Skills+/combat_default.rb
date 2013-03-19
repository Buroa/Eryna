require 'java'
java_import 'org.apollo.game.model.EquipmentConstants'

class DefaultCombatDistancedAction < CombatDistancedAction

  def initialize(source, victim)
    super source, victim, 2, 0
  end

  def executeCombat
    # calculate max damage and randomize it
    max_damage = get_combat_max_hit character, victim
    random_damage = rand (1+max_damage).to_i

    # damage the victim
    if damage_victim random_damage
      weapon = character.equipment.get EquipmentConstants::WEAPON
      weapon = weapon == nil ? 0 : weapon.id

      if get_combat_set(character).using_special
        special = SPECIALS[weapon]
        if special != nil
          special.execute character, victim
        else
          SPECIALS[0].execute character, victim
        end
      else
        SPECIALS[0].execute character, victim
      end

      # finally add experience
      add_combat_experience character, victim, random_damage
    end
  end

end