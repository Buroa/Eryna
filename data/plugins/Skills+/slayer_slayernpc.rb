EASY_TASK = 1
MEDIUM_TASK = 2
HARD_TASK = 3

EASYSNPCS = {}
MEDIUMSNPCS = {}
HARDSNPCS = {}

class SlayerNpc
  attr_reader :id, :req, :difficulty, :location

  def initialize(id, req, difficulty, location)
    @id = id
    @req = req
    @difficulty = difficulty
    @location = location
  end

end

def append_task(slayernpc)

  case slayernpc.difficulty
    when 1
      EASYSNPCS[slayernpc.id] = slayernpc
      return
    when 2
      MEDIUMSNPCS[slayernpc.id] = slayernpc
      return
    when 3
      HARDSNPCS[slayernpc.id] = slayernpc
      return
  end
end



# Add slayer tasks
append_task SlayerNpc.new(1604,  60, 2, "Slayer Tower") #             Aberrant Spectre
append_task SlayerNpc.new(1615,  85, 3, "Slayer Tower") #             Abyssal Demon
append_task SlayerNpc.new(1612,  15, 2, "Slayer Tower") #             Banshee
append_task SlayerNpc.new(1616,  40, 2, "Fremennik Slayer Dungeon") # Basilisk
append_task SlayerNpc.new(412,   1,  1, "Silvarea") #                 Bat
append_task SlayerNpc.new(84,    1,  2, "Taverly Dungeon") #          Black demon
append_task SlayerNpc.new(54,    1,  3, "Taverly Dungeon") #          Black dragon
append_task SlayerNpc.new(1618, 50,  2, "Slayer Tower") #             Bloodveld
append_task SlayerNpc.new(55,    1,  2, "Taverly Dungeon") #          Blue dragon
append_task SlayerNpc.new(1590,  1,  3, "Brimhaven Dungeon") #        Bronze dragon
append_task SlayerNpc.new(1600, 10,  1, "Fremennik Slayer Dungeon") # Cave crawler
append_task SlayerNpc.new(4353, 58,  2, "Slayer Tower") #             Cave horror
append_task SlayerNpc.new(1620, 25,  2, "Fremennik Slayer Dungeon") # Cockatrice
append_task SlayerNpc.new(1648,  5,  1, "Slayer Tower") #             Crawling hand
append_task SlayerNpc.new(2455,  1,  1, "Waterbirth Island") #        Dagganoth
append_task SlayerNpc.new(2783, 90,  3, "Slayer Tower") #             Dark beast
append_task SlayerNpc.new(1624, 65,  2, "Slayer Tower") #             Dust devil
append_task SlayerNpc.new(124,   1,  1, "Edgeville Dungeon") #        Earth warrior
append_task SlayerNpc.new(110,   1,  1, "Brimhaven Dungeon") #        Fire giant
append_task SlayerNpc.new(1610, 75,  3, "Slayer Tower") #             Gargoyle
append_task SlayerNpc.new(103,   1,  1, "Taverly Dungeon") #          Ghost
append_task SlayerNpc.new(83,    1,  2, "Brimhaven Dungeon") #        Greater Demon
append_task SlayerNpc.new(83,    1,  3, "Brimhaven Dungeon") #        Greater Demon
append_task SlayerNpc.new(941,   1,  2, "The Wilderness") #           Green Dragon
append_task SlayerNpc.new(941,   1,  3, "The Wilderness") #           Green Dragon
append_task SlayerNpc.new(49,    1,  3, "Traverly Dungeon") #         Hellhound
append_task SlayerNpc.new(117,   1,  2, "Edgeville Dungeon") #        Hill giant
append_task SlayerNpc.new(111,   1,  2, "Asgarnian Ice Caves") #      Ice giant
append_task SlayerNpc.new(125,   1,  2, "Asgarnian Ice Caves") #      Ice warrior
append_task SlayerNpc.new(1643, 45,  2, "Slayer Tower") #             Infernal mage
append_task SlayerNpc.new(1591,  1,  3, "Brimhaven Dungeon") #        Iron dragon
append_task SlayerNpc.new(1637, 52,  2, "Fremennik Slayer Dungeon") # Jelly
append_task SlayerNpc.new(1153,  1,  2, "Kalphite Lair")            # Kalaphite
append_task SlayerNpc.new(1608, 70,  2, "Fremennik Slayer Dungeon") # Kurask
append_task SlayerNpc.new(1608, 70,  3, "Fremennik Slayer Dungeon") # Kurask
append_task SlayerNpc.new(82,    1,  2, "Karamja Dungeon") #          Lesser demon
append_task SlayerNpc.new(112,   1,  2, "Brimhaven Dungeon") #        Moss giant
append_task SlayerNpc.new(1613, 80,  3, "Slayer Tower") #             Nechryaels
append_task SlayerNpc.new(1633, 30,  1, "Fremennik Slayer Dungeon") # Pyrefiend
append_task SlayerNpc.new(53,    1,  2, "Brimhaven Dungeon") #        Red dragon
append_task SlayerNpc.new(53,    1,  3, "Brimhaven Dungeon") #        Red dragon
append_task SlayerNpc.new(1622, 20,  1, "Fremennik Slayer Dungeon") # Rockslug
append_task SlayerNpc.new(90,    1,  1, "Edgeville Dungeon") #        Skeleton
append_task SlayerNpc.new(1591,  1,  2, "Brimhaven Dungeon") #        Steel dragon
append_task SlayerNpc.new(1591,  1,  3, "Brimhaven Dungeon") #        Steel dragon
append_task SlayerNpc.new(1626, 55,  2, "Fremennik Slayer Dungeon") # Turoth
append_task SlayerNpc.new(1626, 55,  3, "Fremennik Slayer Dungeon") # Turoth