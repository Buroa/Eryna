C_GEMS = {}

class C_Gem
  attr_reader :uncut, :cut, :level, :exp, :animation, :crackable

  def initialize(uncut, cut, level, exp, animation, crackable)
    @uncut = uncut
    @cut = cut
    @level = level
    @exp = exp
    @animation = animation
    @crackable = crackable
  end
end

def append_gem(gem)
  C_GEMS[gem.uncut] = gem
end

append_gem C_Gem.new(1625, 1609, 1 , 15 , 891,  true) # Opal
append_gem C_Gem.new(1627, 1611, 13, 20 , 891,  true) # Jade
append_gem C_Gem.new(1629, 1613, 16, 25 , 892,  true) # Red topaz
append_gem C_Gem.new(1623, 1607, 1 , 50 , 888, false) # Sapphire
append_gem C_Gem.new(1621, 1605, 27, 68 , 889, false) # Emerald
append_gem C_Gem.new(1619, 1603, 34, 85 , 887, false) # Ruby
append_gem C_Gem.new(1617, 1601, 43, 108, 890, false) # Diamond
append_gem C_Gem.new(1631, 1615, 55, 138, 890, false) # Dragonstone

#append_gem c_Gem.new(6571, 6573, 67,168, 2717, false) # Onyx