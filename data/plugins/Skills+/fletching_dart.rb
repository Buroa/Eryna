DARTS = {}

class Dart
  attr_reader :dart, :item, :level, :exp

  def initialize(dart, item, level, exp)
    @dart = dart
    @item = item
    @level = level
    @exp = exp
  end
end

def append_dart(dart)
  DARTS[dart.dart] = dart
end

append_dart Dart.new(819, 806, 1, 2)
append_dart Dart.new(820, 807, 22, 4)
append_dart Dart.new(821, 808, 37, 8)
append_dart Dart.new(822, 809, 52, 12)
append_dart Dart.new(823, 810, 67, 15)
append_dart Dart.new(824, 811, 81, 19)