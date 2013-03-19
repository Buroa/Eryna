require 'java'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.Npc'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.def.NpcDefinition'

SPADE_ANIMATION = Animation.new 831

on :event, :object_action do |ctx, player, event|
  if event.option == 1
    barrows_clazz = BARROWS_SARCOPHAGUSS[event.id]
    if barrows_clazz != nil
      if not barrows_clazz.spawned(player) and not barrows_clazz.killed(player)
        npc = Npc.new barrows_clazz.id, player.last_position
        World.world.register npc
        barrows_clazz.add player, npc
        get_combat_set(npc).add_listener BarrowsListener.new(barrows_clazz)
        get_combat_set(npc).set_respawn false

        # special barrows npcs have different attacks
        if npc.id == 2025
          npc.start_action MagicDistancedAction.new(npc, player, COMBAT_SPELLS[13023])
        elsif npc.id == 2028
          npc.start_action RangeDistancedAction.new(npc, player)
        else
          npc.start_action DefaultCombatDistancedAction.new(npc, player)
        end
      else
        player.send_message "You have already woken up #{NpcDefinition.for_id(barrows_clazz.id).name}!"
      end
      ctx.break_handler_chain
    else
      barrows_clazz = BARROWS_STAIRCASES[event.id]
      if barrows_clazz != nil
        if barrows_clazz.spawned(player) and not barrows_clazz.killed(player)
          World.world.unregister barrows_clazz.players[player]
          barrows_clazz.claimed player
        end
        player.teleport barrows_clazz.hill
        ctx.break_handler_chain
      end
    end
  end
end

on :event, :npc_option do |ctx, player, event|
  if event.option == 1
    barrows_clazz = BARROWS_NPCS[event.npc.id]
    if barrows_clazz != nil
      if barrows_clazz.can_attack(player, event.npc)
        # continue
        # do each of the barrows npcs have different attacks?
        # if so, create a new combat action for them (npc.stop_action first)
        # otherwise, the default combat plugin will handle it
      else
        player.stop_action
        ctx.break_handler_chain
      end
    end
  end
end

on :event, :item_option do |ctx, player, event|

  if event.option == 1 and event.id == 952
    player.play_animation SPADE_ANIMATION
    BARROWS_NPCS.each do |id, clazz|
      if clazz.hill.is_within_distance player.position, 6
        ctx.break_handler_chain
        schedule 1 do |task|
          player.teleport clazz.chamber
          player.stop_animation
          task.stop
        end
        break
      end
    end
  end
end

on :logout do |player|
  BARROWS_NPCS.each do |id, clazz|
    if clazz.spawned(player)
      World.world.unregister clazz.players[player]
      clazz.players.delete player
    end
  end
end