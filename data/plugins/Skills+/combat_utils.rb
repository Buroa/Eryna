require 'java'
java_import 'org.apollo.game.sync.block.SynchronizationBlock'
java_import 'org.apollo.game.model.GroundItem'
java_import 'org.apollo.game.model.Skill'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.EquipmentConstants'
java_import 'org.apollo.util.CombatUtil'
java_import 'org.apollo.game.model.Item'

CHOP_ANIMATION = Animation.new 412
SLASH_ANIMATION = Animation.new 451
KICK_ANIMATION = Animation.new 423
DEFAULT_ANIMATION = Animation.new 422
FOREARM_BLOCK_ANIMATION = Animation.new 424
BLOCK_ANIMATION = Animation.new 404
SHIELD_BLOCK_ANIMATION = Animation.new 403
SPECIAL_RELOAD = {}

def damage_character(victim, hit)
  hit = hit > victim.health ? victim.health : hit
  health = victim.health - hit
  victim.set_health health
  victim.block_set.add SynchronizationBlock::createHitUpdateBlock(hit, health < 2 ? 1 : health, victim.health_max)
end

def create_death_drop(source, victim, position, inventory, inventory_keep)
  if victim.is_controlling
    victim.inventory.start_firing_events
    victim.inventory.add_all inventory_keep
    victim.inventory.force_refresh
    victim.equipment.start_firing_events
    victim.equipment.force_refresh

    if source != nil
      inventory.each do |item|
        to_ground = GroundItem.new(!source.is_controlling ? victim.name : source.name, item, position)
        World.world.register to_ground
      end
    end
  else
    # lookup the npc drops
    npc_drop_inventory = NPC_DROPS[victim.id]
    if npc_drop_inventory != nil
      probability = rand npc_drop_inventory.size
      rate = probability < 1 ? 1 : probability
      CombatUtil.getNpcGroundItems(rate, npc_drop_inventory).each do |drop|
        World.world.register GroundItem.new(!source.is_controlling ? victim.name : source.name, drop, position)
      end
    end

    # register bones
    World.world.register GroundItem.new(!source.is_controlling ? victim.name : source.name, Item.new(526), position)
  end
end

def battle_executed(source, victim)
  get_combat_set(victim).listeners.each do |listener|
    listener.battle_executed source, victim
  end
end

def death_executed(source, victim)
  get_combat_set(victim).listeners.each do |listener|
    listener.death_executed source, victim
  end
end

def get_combat_max_hit(source, victim)
  strength_level = source.skill_set.skill(Skill::STRENGTH).current_level
  potion_bonus = 0
  prayer_bonus = 1
  _style_bonus = get_combat_set(source).style
  style_bonus = _style_bonus != 4 ? 3 : 2
  effective_strength = 8 + ((strength_level + potion_bonus) * prayer_bonus).floor + style_bonus
  strength_bonus = source.bonuses.bonuses.strength_melee
  base_damage = 5 + effective_strength * (1 + strength_bonus / 64)
  return base_damage / 10
end

def get_combat_hit_animation(source)
  animation = Animation.new EquipmentConstants.get_attack_anim(source)
  style = get_combat_set(source).style

  if animation.id == 422 #Default, we can override
    # only default? no weapon
    if style == 1
      if not source.is_controlling
        npc_attack_anim = NPC_ATTACK_ANIMATIONS[source.id]
        return npc_attack_anim != nil ? npc_attack_anim : DEFAULT_ANIMATION
      else
        return DEFAULT_ANIMATION
      end
    elsif style == 2
      return KICK_ANIMATION
    elsif style == 3 or style == 4
      return rand(2) == 1 ? DEFAULT_ANIMATION : KICK_ANIMATION
    end
  elsif animation.id == 451 # Swords
    if style == 1 or style == 2 or style == 4
      return SLASH_ANIMATION
    elsif style == 3
      return CHOP_ANIMATION
    end
  end
  return animation
end

def special_schedule(character)
  special = SPECIAL_RELOAD[character]
  if special == nil
    SPECIAL_RELOAD[character] = true
    schedule 15 do |task|
      if character.is_active
        special_amount = get_combat_set(character).special_amount
        if special_amount < 100
          get_combat_set(character).set_special_amount special_amount+10
          character.send ConfigEvent.new(300, (special_amount+10) * 10)
        else
          SPECIAL_RELOAD.delete character
          task.stop
        end
      else
        SPECIAL_RELOAD.delete character
        task.stop
      end
    end
  end
end

def add_combat_experience(source, victim, hit)
  hit = hit * 100
  style = get_combat_set(source).style

  if source.is_controlling
    if style == 1
      source.skill_set.add_experience(Skill::ATTACK, hit * 0.4)
    elsif style == 2
      source.skill_set.add_experience(Skill::STRENGTH, hit * 0.4)
    elsif style == 3
      source.skill_set.add_experience(Skill::DEFENCE, hit * 0.4)
    elsif style == 4
      source.skill_set.add_experience(Skill::ATTACK, hit * 0.133)
      source.skill_set.add_experience(Skill::STRENGTH, hit * 0.133)
      source.skill_set.add_experience(Skill::DEFENCE, hit * 0.133)
    end
    source.skill_set.add_experience(Skill::HITPOINTS, hit * 0.133)
  end
end