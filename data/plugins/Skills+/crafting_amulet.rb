AMULETS = {}

class Amulet
  attr_reader :amulet, :item, :level, :exp

  def initialize(amulet, item, level, exp)
    @amulet = amulet
    @item = item
    @level = level
    @exp = exp
  end
end

def append_amulet(amulet)
  AMULETS[amulet.amulet] = amulet
end

append_amulet Amulet.new(1673, 1692, 8, 30)
append_amulet Amulet.new(1675, 1694, 24, 65)
append_amulet Amulet.new(1677, 1696, 31, 70)
append_amulet Amulet.new(1679, 1698, 50, 85)
append_amulet Amulet.new(1681, 1700, 70, 100)
append_amulet Amulet.new(1683, 1702, 80, 150)
append_amulet Amulet.new(6579, 6581, 90, 165)