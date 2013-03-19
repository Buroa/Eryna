require 'java'
java_import 'org.apollo.game.model.Position'

# holds the barrows npcs

BARROWS_NPCS = {}
BARROWS_SARCOPHAGUSS = {}
BARROWS_STAIRCASES = {}

class BarrowsNpc
  attr_reader :id, :sarcophagus, :staircase, :hill, :chamber, :players, :kills

  def initialize(id, sarcophagus, staircase, hill, chamber)
    @id = id
    @sarcophagus = sarcophagus
    @staircase = staircase
    @hill = hill
    @chamber = chamber
    @players = {}
    @kills = {}
  end

  def add(player, npc)
    @players[player] = npc
  end

  def remove(player)
    @kills[player] = true
  end

  def claimed(player)
    @players.delete player
    @kills.delete player
  end

  def can_attack(player, npc)
    return (@players[player] != nil and (@players[player].index == npc.index))
  end

  def killed(player)
    return @kills[player] != nil
  end

  def spawned(player)
    return @players[player] != nil
  end
end

def append_barrows_npc(clazz)
  BARROWS_NPCS[clazz.id] = clazz
  BARROWS_SARCOPHAGUSS[clazz.sarcophagus] = clazz
  BARROWS_STAIRCASES[clazz.staircase] = clazz
end

append_barrows_npc BarrowsNpc.new(2025, 6821, 6702, Position.new(3565, 3289), Position.new(3557, 9703, 3)) # Ahrim
append_barrows_npc BarrowsNpc.new(2026, 6771, 6703, Position.new(3575, 3298), Position.new(3556, 9718, 3)) # Dharok
append_barrows_npc BarrowsNpc.new(2027, 6773, 6704, Position.new(3566, 3276), Position.new(3534, 9704, 3)) # Guthan
append_barrows_npc BarrowsNpc.new(2028, 6822, 6705, Position.new(3566, 3276), Position.new(3546, 9684, 3)) # Karil
append_barrows_npc BarrowsNpc.new(2029, 6772, 6706, Position.new(3554, 3283), Position.new(3568, 9683, 3)) # Torag
append_barrows_npc BarrowsNpc.new(2030, 6823, 6707, Position.new(3557, 3298), Position.new(3578, 9706, 3)) # Verac