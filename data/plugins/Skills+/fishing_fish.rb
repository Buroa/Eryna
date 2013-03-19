FISH = {}

class Fish
  attr_reader :fish, :equipment, :bait, :level, :exp, :animation

  def initialize(fish, equipment, bait, level, exp, animation)
    @fish = fish
    @equipment = equipment
    @bait = bait
    @level = level
    @exp = exp
    @animation = animation
  end
end

def append_fish(id, options)
  FISH[id] ||= {}
  options.each do |option, fish|
    FISH[id][option] = fish
  end
end

LURE = {
  2 => Fish.new([335, 331], 309, 314, [20,30], [50, 90], 623),
  3 => Fish.new([349], 307, 313, [25], [60], 623)
}

CAGE = {
  2 => Fish.new([377], 301, -1, [40], [90], 619),
  3 => Fish.new([359, 371], 311, -1, [35, 50], [80, 100], 618)
}

BNET = {
  2 => Fish.new([353, 341, 363], 305, -1, [16, 23, 46], [20, 45, 100], 620),
  3 => Fish.new([383], 311, -1, [76], [110], 618)
}

SNET = {
  2 => Fish.new([317, 321], 303, -1, [1, 15], [10, 40], 621),
  3 => Fish.new([327, 345], 307, 313, [5, 10], [20, 30], 623)
}

MNET = {
  2 => Fish.new([7944], 303, -1, [68], [120], 621)
}

append_fish 309, LURE
append_fish 312, CAGE
append_fish 324, CAGE
append_fish 313, BNET
append_fish 316, SNET
append_fish 326, MNET