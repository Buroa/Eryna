BAR = {}

class Bar
  attr_reader :buttons, :id, :frame, :level, :ore_1, :ore_2, :exp, :ore_2_req, :count

  def initialize(buttons, id, frame, level, ore_1, ore_2, exp, ore_2_req)
    @buttons = buttons
    @id = id
    @frame = frame
    @level = level
    @ore_1 = ore_1
    @ore_2 = ore_2
    @exp = exp
    @ore_2_req = ore_2_req
  end

  def set_count(count)
    @count = count
  end
end

def append_bar(bar)
  bar.buttons.each do |button, count|
    nb = Bar.new(bar.buttons, bar.id, bar.frame, bar.level, bar.ore_1, bar.ore_2, bar.exp, bar.ore_2_req)
    nb.set_count(count)
    BAR[button] = nb
  end
end

BBRONZE_OBJECTS = {
  3987 => 1, 3986 => 5, 2807 => 10
}

BIRON_OBJECTS = {
  3991 => 1, 3990 => 5, 3989 => 10
}

BSILVER_OBJECTS = {
  3995 => 1, 3994 => 5, 3993 => 10
}

BSTEEL_OBJECTS = {
  3999 => 1, 3998 => 5, 3997 => 10
}

BGOLD_OBJECTS = {
  4003 => 1, 4002 => 5, 4001 => 10
}

BMITHRIL_OBJECTS = {
  7441 => 1, 7440 => 5, 6397 => 10
}

BADAMANT_OBJECTS = {
  7446 => 1, 7444 => 5, 7443 => 10
}

BRUNEITE_OBJECTS = {
  7450 => 1, 7449 => 5, 7448 => 10
}

append_bar Bar.new(BBRONZE_OBJECTS, 2349, 2405, 1, 438, 436, 6, 1)
append_bar Bar.new(BIRON_OBJECTS, 2351, 2406, 15, 440, -1, 13, -1)
append_bar Bar.new(BSILVER_OBJECTS, 2355, 2407, 20, 442, -1, 14, -1)
append_bar Bar.new(BSTEEL_OBJECTS, 2353, 2409, 30, 440, 453, 18, 2)
append_bar Bar.new(BGOLD_OBJECTS, 2357, 2410, 40, 444, -1, 23, -1)
append_bar Bar.new(BMITHRIL_OBJECTS, 2359, 2411, 50, 447, 453, 30, 4)
append_bar Bar.new(BADAMANT_OBJECTS, 2361, 2412, 70, 449, 453, 38, 6)
append_bar Bar.new(BRUNEITE_OBJECTS, 2363, 2413, 85, 451, 453, 50, 8)
