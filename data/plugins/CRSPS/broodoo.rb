## This script deals with all the broodoo functions of the old
## corruption rsps emulator
require 'java'
java_import 'org.apollo.game.model.Position'

door = Position.new 2902, 2896
portal = Position.new 2907, 2896
BROODOOS = [2499, 2501, 2503]
BVARS = {}

on :event, :object_action do |ctx, player, event|
  if event.position.x == door.x and event.position.y == door.y
    broodoo = BVARS[player.name]
    if broodoo == nil
      BVARS[player.name] = BroodooClass.new
      player.teleport Position.new(2903, 2896)
    end
  elsif event.position.x == portal.x and event.position.y == portal.y
    broodoo = BVARS[player.name]
    if broodoo != nil and broodoo.continue(player)
      player.teleport player.position.transform(0,0,1)
    end
  end
end

on :event, :npc_option do |ctx, player, event|
  if event.option == 1
    broodoo = BVARS[player.name]
    if broodoo != nil and BROODOOS.include? event.npc.id
      get_combat_set(player).add_listener BroodooListener.new
    end
  end
end

class BroodooListener < CombatListener
  def executed_death(source, victim)
    if source != nil and victim != nil
      broodoo = BVARS[source.name]
      if broodoo != nil
        broodoo.add victim.id
        return true
      else
        return false
      end
    else
      return false
    end
  end
end

class BroodooClass
  def initialize
    @kill = {}
  end
  def add(broodoo)
    @kill[broodoo] = true
  end
  def continue(player)
    id = BROODOOS[player.position.height]
    if id == nil
      puts "id is nil #{player.position.height}"
      return false
    else 
      return @kill[id] != nil
    end
  end
end