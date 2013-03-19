FOODS_ = {}

class Food_

 attr_reader :uncook, :cook, :burnt, :exp, :level, :burnlevel, :burnlevelg

  def initialize(uncook, cook, burnt, exp, level, burnlevel, burnlevelg)
    @uncook = uncook
    @cook = cook
    @burnt = burnt
    @exp = exp
    @level = level
    @burnlevel = burnlevel
    @burnlevelg = burnlevelg
  end

end

def append_food1(food)
  FOODS_[food.uncook] = food
end

append_food1 Food_.new(317, 315, 323, 30, 1, 32, 0) # Shrimp
append_food1 Food_.new(327, 325, 343, 40, 1, 38, 0) # Sardine
append_food1 Food_.new(321, 319, 323, 30, 1, 34, 0) # Anchovies
append_food1 Food_.new(345, 347, 357, 50, 5, 39, 0) # Herring
append_food1 Food_.new(359, 361, 367, 100, 30, 64, 62) # Tuna
append_food1 Food_.new(371, 373, 375, 140, 45, 85, 81) # Swordfish
append_food1 Food_.new(383, 385, 387, 210, 80, 0, 94) # Shark
append_food1 Food_.new(377, 379, 381, 30, 40, 74, 68) # Lobster
