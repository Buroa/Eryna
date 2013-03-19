SLAYERPOINTS = {}
SLAYERTASK   = {}

require 'java'

# Handle the npc option attack
on :event, :npc_option do |ctx, player, event|
  if event.option == 1
    task = SLAYERTASK[player.name]
    if task != nil
      if event.npc.id == task.slaynpc.id
        get_combat_set(event.npc).add_listener SlayerListener.new
      end
    end
  end
end

# Handles the death of slayable npcs
class SlayerListener < CombatListener
  # We only need to override this method for slayer.
  def death_executed(source, victim)
    if source != nil and victim != nil
      addKill source
      addPoints source, victim
      return true
    else
      return false
    end
  end
end

on :command, :slayer do |player, command|
  if command.arguments.length == 1
    arg = command.arguments[0]
    if arg == "new"
      generateTask player
    elsif arg == "task"
      getTask player
    elsif arg == "count"
      getKills player
    elsif arg == "points"
      getPoints player
    elsif arg == "exp"
      spendPoints player, 1
    end
  else
    player.send_message "Syntax: ::slayer [new, task, count, points, exp]"
  end
end