require 'java'
java_import 'org.apollo.game.model.Position'

ALTARS =    {}
RIFTS =     {}
TALISMANS = {}

class Altar

  attr_reader :talisman, :tiara, :outside, :inside

  def initialize(talisman, tiara, outside, inside)
    @talisman = talisman
    @tiara = tiara
    @outside = outside
    @inside = inside
  end

end

def append_talisman(object, rift, altar)
  ALTARS[object] = altar
  RIFTS[rift] = altar
  TALISMANS[altar.talisman] = altar.outside
end

append_talisman 2452, 7139, Altar.new(1438, 5527, Position.new(2985, 3292), Position.new(2842, 4829)) # Air
append_talisman 2453, 7140, Altar.new(1448, 5529, Position.new(2982, 3514), Position.new(2793, 4828)) # Mind
append_talisman 2457, 7131, Altar.new(1446, 5533, Position.new(3053, 3445), Position.new(2521, 4834)) # Body
append_talisman 2455, 7130, Altar.new(1440, 5535, Position.new(3305, 3473), Position.new(2655, 4831)) # Earth
append_talisman 2456, 7129, Altar.new(1442, 5537, Position.new(3313, 3255), Position.new(2577, 4845)) # Fire
append_talisman 2458, 7132, Altar.new(1454, 5539, Position.new(2407, 4376), Position.new(2162, 4833)) # Cosmic
append_talisman 2460, 7133, Altar.new(1462, 5541, Position.new(2868, 3018), Position.new(2400, 4835)) # Nature
append_talisman 2461, 7134, Altar.new(1452, 5543, Position.new(3059, 3590), Position.new(2268, 4842)) # Chaos
append_talisman 2459, 7135, Altar.new(1458, 5545, Position.new(2857, 3380), Position.new(2464, 4818)) # Law
append_talisman 0000, 7136, Altar.new(1456, 5547,                      nil, Position.new(2208, 4831)) # Death
append_talisman 2454, 7137, Altar.new(1444, 5531, Position.new(3185, 3165), Position.new(2713, 4836)) # Water
append_talisman 2489, 7141, Altar.new(1450, 5549,                      nil,                      nil) # Blood
append_talisman 2490, 7138, Altar.new(1460, 5551,                      nil,                      nil) # Soul
