STAVES = {}

class Stave
  attr_reader :orb, :item, :level, :exp

  def initialize(orb, item, level, exp)
    @orb = orb
    @item = item
    @level = level
    @exp = exp
  end
end

def append_stave(stave)
  STAVES[stave.orb] = stave
end

append_stave Stave.new(571, 1395, 54, 100)
append_stave Stave.new(575, 1399, 58, 112)
append_stave Stave.new(569, 1393, 62, 125)
append_stave Stave.new(573, 1397, 66, 137)