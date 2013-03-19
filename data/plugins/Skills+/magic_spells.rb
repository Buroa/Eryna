require 'java'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.Animation'

COMBAT_SPELLS = {}

class CombatSpell < Spell
  attr_reader :projectile, :animation, :graphics, :spell_id

  def initialize(level, elements, experience, projectile, animation, graphics)
    super level, elements, experience
    @level = level
    @elements = elements
    @experience = experience
    @projectile = projectile
    @animation = animation
    @graphics = graphics
    @spell_id = 0
  end
 
  def set_spell_id(spell_id)
    @spell_id = spell_id
  end
end

def append_combat_spell(spell, clazz)
  COMBAT_SPELLS[spell] = clazz
  clazz.set_spell_id spell
end

DEFAULT_SPELL_ANIMATION = Animation.new 711
FIRST_ANCIENT_SPELL_ANIMATION = Animation.new 1978
SECOND_ANCIENT_SPELL_ANIMATION = Animation.new 1979

# Default spells
append_combat_spell 1152, CombatSpell.new(1, { AIR => 1, MIND => 1 }, 6, 91, DEFAULT_SPELL_ANIMATION, [ Graphic.new(90, 0, 100), Graphic.new(92, 0, 100) ])
append_combat_spell 1154, CombatSpell.new(5, { WATER => 1, AIR => 1, MIND => 1 }, 8, 94, DEFAULT_SPELL_ANIMATION, [ Graphic.new(93, 0, 100), Graphic.new(95, 0, 100) ])
append_combat_spell 1156, CombatSpell.new(9, { EARTH => 2, AIR => 1, MIND => 1 }, 10, 97, DEFAULT_SPELL_ANIMATION, [ Graphic.new(96, 0, 100), Graphic.new(98, 0, 100) ])
append_combat_spell 1158, CombatSpell.new(13, { FIRE => 3, AIR => 2, MIND => 1 }, 2, 100, DEFAULT_SPELL_ANIMATION, [ Graphic.new(99, 0, 100), Graphic.new(101, 0, 100) ])
append_combat_spell 1160, CombatSpell.new(17, { AIR => 2, CHAOS => 1 }, 14, 118, DEFAULT_SPELL_ANIMATION, [ Graphic.new(117, 0, 100), Graphic.new(119, 0, 100) ])
append_combat_spell 1163, CombatSpell.new(23, { WATER => 2, AIR => 2, CHAOS => 1 }, 17, 121, DEFAULT_SPELL_ANIMATION, [ Graphic.new(120, 0, 100), Graphic.new(122, 0, 100) ])
append_combat_spell 1166, CombatSpell.new(29, { EARTH => 3, AIR => 2, CHAOS => 1 }, 20, 124, DEFAULT_SPELL_ANIMATION, [ Graphic.new(123, 0, 100), Graphic.new(125, 0, 100) ])
append_combat_spell 1169, CombatSpell.new(35, { FIRE => 4, AIR => 3, CHAOS => 1 }, 22, 127, DEFAULT_SPELL_ANIMATION, [ Graphic.new(126, 0, 100), Graphic.new(128, 0, 100) ])
append_combat_spell 1172, CombatSpell.new(41, { AIR => 3, DEATH => 1 }, 26, 133, DEFAULT_SPELL_ANIMATION, [ Graphic.new(132, 0, 100), Graphic.new(134, 0, 100) ])
append_combat_spell 1175, CombatSpell.new(47, { WATER => 3, AIR => 3, DEATH => 1 }, 29, 136, DEFAULT_SPELL_ANIMATION, [ Graphic.new(135, 0, 100), Graphic.new(137, 0, 100) ])
append_combat_spell 1177, CombatSpell.new(53, { EARTH => 4, AIR => 3, DEATH => 1 }, 32, 139, DEFAULT_SPELL_ANIMATION, [ Graphic.new(138, 0, 100), Graphic.new(140, 0, 100) ])
append_combat_spell 1181, CombatSpell.new(59, { FIRE => 5, AIR => 4, DEATH => 1 }, 36, 130, DEFAULT_SPELL_ANIMATION, [ Graphic.new(129, 0, 100), Graphic.new(131, 0, 100) ])
append_combat_spell 1183, CombatSpell.new(62, { AIR => 5, BLOOD => 1 }, 36, 159, DEFAULT_SPELL_ANIMATION, [ Graphic.new(158, 0, 50), Graphic.new(160, 0, 100) ])
append_combat_spell 1185, CombatSpell.new(65, { WATER => 7, AIR => 5, BLOOD => 1 }, 38, 162, DEFAULT_SPELL_ANIMATION, [ Graphic.new(161, 0, 50), Graphic.new(163, 0, 100) ])
append_combat_spell 1188, CombatSpell.new(70, { EARTH => 7, AIR => 5, BLOOD => 1 }, 40, 165, DEFAULT_SPELL_ANIMATION, [ Graphic.new(164, 0, 50), Graphic.new(166, 0, 100) ])
append_combat_spell 1189, CombatSpell.new(75, { FIRE => 7, AIR => 5, BLOOD => 1 }, 43, 156, DEFAULT_SPELL_ANIMATION, [ Graphic.new(155, 0, 50), Graphic.new(157, 0, 100) ])

# Ancient magicks
append_combat_spell 12939, CombatSpell.new(50, { CHAOS => 2, DEATH => 2, FIRE => 1, AIR => 1 }, 30, 384, FIRST_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(385) ])
append_combat_spell 12987, CombatSpell.new(52, { CHAOS => 2, DEATH => 2, AIR => 1, SOUL => 1 }, 31, 378, FIRST_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(379) ])
append_combat_spell 12901, CombatSpell.new(56, { CHAOS => 2, DEATH => 2, BLOOD => 1 }, 33, 0, FIRST_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(373) ])
append_combat_spell 12861, CombatSpell.new(58, { CHAOS => 2, DEATH => 2, WATER => 2 }, 34, 360, FIRST_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(361) ])
append_combat_spell 12963, CombatSpell.new(62, { CHAOS => 4, DEATH => 2, FIRE => 2, AIR => 2 }, 36, 0, SECOND_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(389) ])
append_combat_spell 13011, CombatSpell.new(64, { CHAOS => 4, DEATH => 2, AIR => 2, SOUL => 2 }, 37, 0, SECOND_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(382) ])
append_combat_spell 12919, CombatSpell.new(68, { CHAOS => 4, DEATH => 2, BLOOD => 2 }, 39, 0, SECOND_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(376) ])
append_combat_spell 12881, CombatSpell.new(70, { CHAOS => 4, DEATH => 2, WATER => 4 }, 40, 0, SECOND_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(363) ])
append_combat_spell 12951, CombatSpell.new(74, { DEATH => 2, BLOOD => 2, FIRE => 2, AIR => 2 }, 42, 386, FIRST_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(387) ])
append_combat_spell 12999, CombatSpell.new(76, { DEATH => 2, BLOOD => 2, AIR => 2, SOUL => 2 }, 43, 380, FIRST_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(381) ])
append_combat_spell 12911, CombatSpell.new(80, { DEATH => 2, BLOOD => 4 }, 45, 374, FIRST_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(375) ])
append_combat_spell 12871, CombatSpell.new(82, { DEATH => 2, BLOOD => 2, WATER => 3 }, 45, 0, FIRST_ANCIENT_SPELL_ANIMATION, [ Graphic.new(366, 0, 100), Graphic.new(367, 0, 100) ])
append_combat_spell 12975, CombatSpell.new(86, { DEATH => 4, BLOOD => 2, FIRE => 4, AIR => 4 }, 48, 0, SECOND_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(391) ])
append_combat_spell 13023, CombatSpell.new(88, { DEATH => 4, BLOOD => 2, AIR => 4, SOUL => 3 }, 48, 0, SECOND_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(383) ])
append_combat_spell 12929, CombatSpell.new(92, { DEATH => 4, BLOOD => 4, SOUL => 1 }, 51, 0, SECOND_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(377) ])
append_combat_spell 12891, CombatSpell.new(94, { DEATH => 4, BLOOD => 2, WATER => 6 }, 52, 0, SECOND_ANCIENT_SPELL_ANIMATION, [ Graphic.new(65535), Graphic.new(369) ])