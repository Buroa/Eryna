require 'java'
java_import 'org.apollo.game.event.impl.ProjectileEvent'
java_import 'org.apollo.game.model.Item'
java_import 'org.apollo.game.model.GroundItem'
java_import 'org.apollo.game.model.Skill'
java_import 'org.apollo.game.model.melee.Prayer'
java_import 'org.apollo.game.model.Skill'

def create_range_projectile(source, victim, weapon)
  offset_x = (source.position.x - victim.position.x) * -1
  offset_y = (source.position.y - victim.position.y) * -1
  # 0 is 51, but because its queued in a pulse, it needs to be instant
  # 31 was 70, ^
  return ProjectileEvent.new source.position, 0, (victim.controlling ? -victim.index-1 : victim.index+1), offset_x, offset_y, weapon.projectile, 0, 31, 43, 31, 16
end

def create_range_drop(source, victim, weapon)
  if source.is_controlling
    return GroundItem.new source.name, Item.new(weapon), victim.position
  else
    return GroundItem.new Item.new(weapon), victim.position
  end
end

def is_using_range(source)
  weapon = source.equipment.get EquipmentConstants::WEAPON
  if weapon == nil
    return false
  elsif RANGE_WEAPON.include? weapon.id
    return true
  else
    return false
  end
end

def get_range_max_hit(source, victim)
  range_level = source.skill_set.skill(Skill::RANGED).current_level
  potion_bonus = 0
  prayer_bonus = Prayer.get_bonus_range source
  other_bonus = 1
  style_bonus = 3
  effective_strength = ((range_level + potion_bonus) * prayer_bonus * other_bonus).floor + style_bonus
  range_strength = source.bonuses.bonuses.strength_range
  base_damage = 5 + (effective_strength + 8) * (range_strength + 64) / 64
  return base_damage/10
end

def add_range_experience(source, victim, hit)
  hit = hit * 100
  style = get_combat_set(source).style

  if source.is_controlling
    if style == 1 or style == 2
      source.skill_set.add_experience(Skill::RANGED, hit * 0.4)
    elsif style == 3
      source.skill_set.add_experience(Skill::RANGED, hit * 0.2)
      source.skill_set.add_experience(Skill::DEFENCE, hit * 0.2)
    end
    source.skill_set.add_experience(Skill::HITPOINTS, hit * 0.133)
  end
end