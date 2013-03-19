RUNES = {}
N_NESS = 1436
P_NESS = 7936

class Rune

  attr_reader :level, :exp, :item, :ess, :mult

  def initialize(level, exp, item, ess, mult)
    @level = level
    @exp = exp
    @item = item
    @ess = ess
    @mult = mult
  end

end

def append_rune(altar, rune)
  RUNES[altar] = rune
end

append_rune 2478, Rune.new(1 , 5.0 , 556, N_NESS,  11)
append_rune 2479, Rune.new(2 , 5.5 , 558, N_NESS,  14)
append_rune 2480, Rune.new(5 , 6.0 , 555, N_NESS,  19)
append_rune 2481, Rune.new(9 , 6.5 , 557, N_NESS,  26)
append_rune 2482, Rune.new(14, 7.0 , 554, N_NESS,  35)
append_rune 2483, Rune.new(20, 7.5 , 559, N_NESS,  46)
append_rune 2484, Rune.new(27, 8.0 , 564, P_NESS,  59)
append_rune 2487, Rune.new(35, 8.5 , 562, P_NESS,  74)
append_rune 2486, Rune.new(44, 9.0 , 561, P_NESS,  91)
append_rune 2485, Rune.new(54, 9.5 , 563, P_NESS, 999)
append_rune 2488, Rune.new(65, 10.0, 560, P_NESS, 999)