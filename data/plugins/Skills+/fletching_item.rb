ITEMS = {}
UBOWS = {}

class Itemz
  attr_reader :log, :bow, :model

  def initialize(log, bow, model)
    @log = log
    @bow = bow
    @model = model
  end
end

class UBow
  attr_reader :bow, :item, :level, :exp
  
  def initialize(bow, item, level, exp)
    @bow = bow
    @item = item
    @level = level
    @exp = exp
  end
end

def append_item(item)
  ITEMS[item.log] = item
end

def append_ubow(ubow)
  UBOWS[ubow.bow] = ubow
end

append_item Itemz.new(1511, 839, 841)
append_item Itemz.new(1521, 845, 843)
append_item Itemz.new(1519, 847, 849)
append_item Itemz.new(1517, 851, 853)
append_item Itemz.new(1515, 855, 857)
append_item Itemz.new(1513, 859, 861)

append_ubow UBow.new(50, 841, 1, 5)
append_ubow UBow.new(48, 839, 10, 10)
append_ubow UBow.new(54, 843, 20, 16)
append_ubow UBow.new(56, 845, 25, 25)
append_ubow UBow.new(60, 849, 40, 33)
append_ubow UBow.new(58, 847, 40, 41)
append_ubow UBow.new(64, 853, 50, 50)
append_ubow UBow.new(62, 851, 55, 58)
append_ubow UBow.new(68, 857, 65, 67)
append_ubow UBow.new(66, 855, 70, 75)
append_ubow UBow.new(72, 861, 80, 83)
append_ubow UBow.new(70, 859, 85, 91)