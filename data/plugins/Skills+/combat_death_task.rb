require 'java'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.Inventory'
java_import 'org.apollo.util.CombatUtil'

## TODO Remove the combat util

DEFAULT_DEATH_ANIMATION = Animation.new 2304
DEFAULT_SPAWN_POSITION = Position.new 3096, 3494

def schedule_death(source, victim)
  if victim.is_controlling
    victim.send_message "Oh dear, you are dead!"
  end

  # set the defunct flag
  get_combat_set(victim).set_defunct true

  # stop the source from attacking us now
  if get_combat_set(source).attacking
    get_combat_set(source).set_embattled false
    source.stop_action
  end

  if not victim.is_controlling
    npc_death_anim = NPC_DEATH_ANIMATIONS[victim.id]
    victim.play_animation npc_death_anim != nil ? npc_death_anim : DEFAULT_DEATH_ANIMATION
  else
    victim.play_animation DEFAULT_DEATH_ANIMATION
  end

  inventory = Inventory.new victim.equipment.size+victim.inventory.size
  inventory.add_all victim.equipment
  inventory.add_all victim.inventory

  inventory_keep = CombatUtil::getItemsKeptOnDeath 3, inventory
  victim.inventory.stop_firing_events
  victim.equipment.stop_firing_events
  victim.inventory.clear
  victim.equipment.clear

  inventory.remove_all inventory_keep
  inventory.shift

  position = victim.position

  schedule 3 do |task|
    victim.teleport victim.is_controlling ? DEFAULT_SPAWN_POSITION : victim.first_position
    victim.add_health victim.health_max
    victim.stop_animation

    create_death_drop source, victim, position, inventory, inventory_keep
    get_combat_set(victim).set_defunct false

    if not victim.is_controlling
      World.world.unregister victim
      if get_combat_set(victim).respawn
        schedule 150 do |respawner|
          World.world.register victim
          respawner.stop
        end
      end
    end
    task.stop
  end
end