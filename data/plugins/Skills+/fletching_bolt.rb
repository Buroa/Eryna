BOLTS = {}

class Bolt
  attr_reader :bolt, :item, :level, :exp

  def initialize(bolt, item, level, exp)
    @bolt = bolt
    @item = item
    @level = level
    @exp = exp
  end
end

def append_bolt(bolt)
  BOLTS[bolt.bolt] = bolt
end

append_bolt Bolt.new(1609, 45, 11, 2)