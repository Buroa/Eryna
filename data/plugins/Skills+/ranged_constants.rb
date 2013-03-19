RANGE_WEAPON = []
RANGE_ARROWS = {}
RANGE_KNIVES = {}
RANGE_BOW    = {}

class Ranged
  attr_reader :projectile, :drawback

  def initialize(projectile, drawback)
    @projectile = projectile
    @drawback = drawback
  end
end

class RangedArrow < Ranged
  attr_reader :id

  def initialize(id, projectile, drawback)
    super projectile, drawback
    @id = id
  end
end

class RangedKnive < Ranged

  def initialize(projectile, drawback)
    super projectile, drawback
  end
end

class RangedBow
  attr_reader :arrows, :arrows_ids

  def initialize(arrows)
    @arrows = arrows
    @arrows_ids = []
    arrows.each do |arrow|
      @arrows_ids.push arrow.id
    end
  end

  def continue(id)
    return @arrows_ids.include? id
  end
end

def append_knive(knive, clazz)
  RANGE_KNIVES[knive] = clazz
  RANGE_WEAPON.push knive
end

def append_weapon(bow, clazz)
  RANGE_BOW[bow] = clazz
  clazz.arrows.each do |arrow|
    RANGE_ARROWS[arrow.id] = arrow
  end
  RANGE_WEAPON.push bow
end

def append_arrow(clazz)
  RANGE_ARROWS[clazz.id] = clazz
end

# Arrow constants
bronze_arrow = RangedArrow.new(882, 10, 18)
iron_arrow = RangedArrow.new(884, 9 , 19)
steel_arrow = RangedArrow.new(886, 11, 20)
mithril_arrow = RangedArrow.new(888, 12, 21)
adamant_arrow = RangedArrow.new(890, 13, 22)
rune_arrow = RangedArrow.new(892, 15, 24)
bolt_rack = RangedArrow.new(4740, 27, -1)

append_weapon 841, RangedBow.new([bronze_arrow, iron_arrow]) # Shortbow
append_weapon 843, RangedBow.new([bronze_arrow, iron_arrow]) # Oak Shortbow
append_weapon 849, RangedBow.new([bronze_arrow, iron_arrow, steel_arrow, mithril_arrow]) # Willow Shortbow
append_weapon 853, RangedBow.new([bronze_arrow, iron_arrow, steel_arrow, mithril_arrow]) # Maple Shortbow
append_weapon 857, RangedBow.new([bronze_arrow, iron_arrow, steel_arrow, mithril_arrow, adamant_arrow]) # Yew Shortbow
append_weapon 861, RangedBow.new([bronze_arrow, iron_arrow, steel_arrow, mithril_arrow, adamant_arrow, rune_arrow]) # Magic Shortbow
append_weapon 4734, RangedBow.new([bolt_rack]) # Karils x-bow

append_knive 864, RangedKnive.new(212, 219)
append_knive 863, RangedKnive.new(213, 220)
append_knive 865, RangedKnive.new(214, 221)
append_knive 866, RangedKnive.new(216, 223)
append_knive 867, RangedKnive.new(217, 224)
append_knive 868, RangedKnive.new(218, 225)
append_knive 869, RangedKnive.new(219, 222)

append_arrow bronze_arrow
append_arrow iron_arrow
append_arrow steel_arrow
append_arrow mithril_arrow
append_arrow adamant_arrow
append_arrow rune_arrow
append_arrow bolt_rack