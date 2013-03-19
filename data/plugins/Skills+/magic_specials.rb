require 'java'
java_import 'org.apollo.game.model.Skill'

MAGIC_SPECIALS = {}

class MagicSpecial
  def execute(source, victim, spell, hit)
  end

  def special(source, victim, spell, hit)
  end

  def hit_multi(source, victim, spell, hit, _num_to_hit)
    hit = rand(hit) < 1 ? hit/2 : hit
    characters = victim.region.characters
    _num_to_hit = rand _num_to_hit

    # calculate if we should hit multiple
    if not rand(3) == 1
      return
    end

    rand_int = 0
    characters.each do |character|
      # check if we can attack
      if can_multi(character)

        # check if we need to break
        if rand_int == _num_to_hit then break end

        # all else
        if character.position.is_within_distance(victim.position, 1) and not character.equals(source) and not character.equals(victim)
          damage_character(character, hit)
          character.play_graphic spell.graphics[1]
          special source, character, spell, hit
          rand_int += 1
          if character.health < 1
            schedule_death source, character
          end
        end
      end
    end
  end

  def can_multi(victim)
    if not victim.is_controlling
      if not victim.definition.is_attackable
        return false
      end
    else
      if victim.is_controlling
        if not victim.position.is_in_wilderness
          return false
        end
      end
    end
    return true
  end
end

class ShadowMagicSpecial < MagicSpecial
  def execute(source, victim, spell, hit)
    special source, victim, spell, hit
    if spell.spell_id == 12999
      hit_multi source, victim, spell, hit, 9
    elsif spell.spell_id == 13023
      hit_multi source, victim, spell, hit, 18
    end
  end

  def special(source, victim, spell, hit)
    att = victim.skill_set.skill Skill::ATTACK
    newatt = Skill.new att.experience, att.current_level - rand(2), att.maximum_level
    victim.skill_set.set_skill Skill::ATTACK, newatt
  end
end

class BloodMagicSpecial < MagicSpecial
  def execute(source, victim, spell, hit)
    special source, victim, spell, hit
    if spell.spell_id == 12911
      # hit_multi source, victim, spell, hit, 9
    elsif spell.spell_id == 12929
      # hit_multi source, victim, spell, hit, 18
    end
  end

  def special(source, victim, spell, hit)
    if rand(2) == 1
      source.add_health hit/4
    end
  end
end

class IceMagicSpecial < MagicSpecial
  def execute(source, victim, spell, hit)
    special source, victim, spell, hit
    if spell.spell_id == 12871
      hit_multi source, victim, spell, hit, 9
    elsif spell.spell_id == 12891
      hit_multi source, victim, spell, hit, 18
    end
  end

  def special(source, victim, spell, hit)
    if spell.spell_id == 12861
      victim.walking_queue.stop 12
    elsif spell.spell_id == 12881
      victim.walking_queue.stop 25
    elsif spell.spell_id == 12871
      victim.walking_queue.stop 37
    elsif spell.spell_id == 12891
      victim.walking_queue.stop 50
    end
  end
end

def append_magic_special(ids, clazz)
  ids.each do |id|
    MAGIC_SPECIALS[id] = clazz
  end
end

append_magic_special [12987, 13011, 12999, 13023], ShadowMagicSpecial.new
append_magic_special [12901, 12919, 12911, 12929], BloodMagicSpecial.new
append_magic_special [12861, 12881, 12871, 12891], IceMagicSpecial.new