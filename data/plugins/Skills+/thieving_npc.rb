require 'java'
java_import 'org.apollo.game.model.Item'

NPCZ = []

class Npcz
  attr_reader :id, :level, :exp, :loot
  
  def initialize(id, level, exp, loot)
    @id = id
    @level = level
    @exp = exp
    @loot = loot
  end
end

def append_npc(npc)
  NPCZ[npc.id] = npc
end

append_npc Npcz.new( 1,  1,   8, [ Item.new(995, 3)  ])
append_npc Npcz.new( 4,  1,   8, [ Item.new(995, 3)  ])
append_npc Npcz.new(18, 25,  26, [ Item.new(995, 18) ])
append_npc Npcz.new( 9, 40,  47, [ Item.new(995, 30) ])
append_npc Npcz.new(26, 55,  84, [ Item.new(995, 50) ])
append_npc Npcz.new(20, 70, 152, [ Item.new(995, 80), Item.new(562, 2) ])
append_npc Npcz.new(21, 80, 273, [ Item.new(995, 200), Item.new(995, 300), Item.new(560, 2), Item.new(565, 1), Item.new(569, 1), Item.new(1601, 1), Item.new(444, 1), Item.new(245, 1) ])
