ARROWS = {}

class Arrow
  attr_reader :item_1, :item_2, :item, :xp, :level
  
  def initialize(item_1, item_2, item, xp, level)
    @item_1 = item_1
    @item_2 = item_2
    @item = item
    @xp = xp
    @level = level
  end
end

def append_arrow(arrow)
  ARROWS[arrow.item_2] = arrow
end

append_arrow Arrow.new(52, 314, 53, 15, 1)
append_arrow Arrow.new(53, 39, 882, 40, 1)
append_arrow Arrow.new(53, 40, 884, 58, 15)
append_arrow Arrow.new(53, 41, 886, 95, 30)
append_arrow Arrow.new(53, 42, 888, 132, 45)
append_arrow Arrow.new(53, 43, 890, 170, 60)
append_arrow Arrow.new(53, 44, 892, 207, 75)