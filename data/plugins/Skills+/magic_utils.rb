require 'java'
java_import 'org.apollo.game.event.impl.ProjectileEvent'
java_import 'org.apollo.game.model.Skill'

def create_magic_projectile(source, victim, spell)
  offset_x = (source.position.x - victim.position.x) * -1
  offset_y = (source.position.y - victim.position.y) * -1
  return ProjectileEvent.new source.position, 0, (victim.is_controlling ? -victim.index-1 : victim.index+1), offset_x, offset_y, spell.projectile, 0, 39, 40, 40, 16
end

def create_magic_special(source, victim, spell, hit)
  special = MAGIC_SPECIALS[spell.spell_id]
  if special != nil
    special.execute source, victim, spell, hit
  end
end

def get_magic_max_hit(source, victim)
  magic_level = source.skill_set.skill(Skill::MAGIC).current_level
  potion_bonus = 0
  style_bonus = 3
  effective_magic = 8 + (magic_level + potion_bonus).floor + style_bonus
  magic_bonus = source.bonuses.bonuses.magic
  base_damage = 5 + effective_magic * (1 + magic_bonus / 64)
  return base_damage / 10
end