require 'java'
java_import 'org.apollo.game.model.EquipmentConstants'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.Animation'

RANGE_ANIMATION = Animation.new 426
KARILS_ANIMATION = Animation.new 2075

class RangeDistancedAction < CombatDistancedAction

  def initialize(source, victim)
    super source, victim, 2, 8
  end

  def executeCombat
    # initiate some constants every time
    region = character.region

    # verify we have a ranged weapon
    weapon = character.equipment.get EquipmentConstants::WEAPON
    if weapon == nil or not RANGE_WEAPON.include? weapon.id
      stop
      return
    end

    # calculate max damage and randomize it
    max_damage = get_range_max_hit character, victim
    random_damage = rand (1+max_damage).to_i

    # check if we are using a throwable ranged weapon
    knive = RANGE_KNIVES[weapon.id]
    if knive != nil
      to_drop = create_range_drop character, victim, weapon.id
      projectile = create_range_projectile character, victim, knive

      # check if we can damage the victim
      if damage_victim random_damage
        character.equipment.set EquipmentConstants::WEAPON, Item.new(weapon.id, weapon.amount-1)

        World.world.region_manager.get_chunk_by_position(character.position).add projectile, 1
        World.world.register to_drop

        add_range_experience character, victim, random_damage
      end
    else
      bow = RANGE_BOW[weapon.id]
      arrows = character.equipment.get EquipmentConstants::ARROWS

      # check if there is ammo
      if arrows == nil
        character.send_message "There is no more ammo in your quiver!"
        stop
        return
      end

      # check if we can use the arrows with the given bow
      if bow.continue arrows.id
        arrow = RANGE_ARROWS[arrows.id]
        projectile = create_range_projectile character, victim, arrow
        to_drop = create_range_drop character, victim, arrow.id

        # check if we can damage the victim
        if damage_victim random_damage
          character.equipment.set EquipmentConstants::ARROWS, Item.new(arrows.id, arrows.amount-1)

          # check if we arent dead after the hit
          if victim.health > 0
            if arrow.drawback != -1 then character.play_graphic Graphic.new(arrow.drawback, 0, 100) end
            character.play_animation arrows.id == 4740 ? KARILS_ANIMATION : RANGE_ANIMATION

            World.world.region_manager.get_chunk_by_position(character.position).add projectile, 1
            if arrows.id != 4740 then World.world.register to_drop end
          end

          add_range_experience character, victim, random_damage
        end
      else
        character.send_message "You have the wrong arrows for this bow!"
        stop
      end
    end
  end
end

# The rest of combat
on :event, :player_option do |ctx, player, event|
  if event.option == 1
    if is_using_range player
      player.walking_queue.clear # this is a must
      player.start_action RangeDistancedAction.new(player, event.player)
      ctx.break_handler_chain
    else
      player.start_action DefaultCombatDistancedAction.new(player, event.player)
      ctx.break_handler_chain
    end
  end
end

on :event, :npc_option do |ctx, player, event|
  if event.option == 1
    if is_using_range player
      player.walking_queue.clear # this is a must
      player.start_action RangeDistancedAction.new(player, event.npc)
      ctx.break_handler_chain
    else
      player.start_action DefaultCombatDistancedAction.new(player, event.npc)
      ctx.break_handler_chain
    end
  end
end