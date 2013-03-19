require 'java'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.action.DistancedAction'

class StoreAction < DistancedAction

  attr_reader :store

  def initialize(player, position, store)
    super 1, true, player, position, 1
    @store = store
  end

  def executeAction
    World.get_world.get_stores.open_shop character, store
    stop
  end

  def equals(other)
    return (get_class == other.get_class)
  end

end

STORES = {}

def append_store(npc, id)
  STORES[npc] = id
end

on :event, :npc_option do |ctx, player, event|
  if event.option == 2 or event.option == 3
    store = STORES[event.get_npc.get_id]
    if store != nil
      player.start_action StoreAction.new(player, event.get_npc.get_position, store)
    end
  end
end

append_store 588, 2
append_store 550, 3
append_store 575, 4
append_store 2356, 5
append_store 3796, 6
append_store 1860, 7
append_store 519, 8
append_store 559, 9
append_store 562, 10
append_store 581, 11
append_store 548, 12
append_store 554, 13
append_store 501, 14
append_store 1301, 15
append_store 1039, 16
append_store 2353, 17
append_store 3166, 18
append_store 2161, 19
append_store 2162, 20
append_store 600, 21
append_store 603, 22
append_store 593, 23
append_store 545, 24
append_store 585, 25
append_store 2305, 26
append_store 2307, 27
append_store 2304, 28
append_store 2306, 29
append_store 517, 30
append_store 558, 31
append_store 576, 32
append_store 1369, 33
append_store 557, 34
append_store 1038, 35
append_store 1433, 36
append_store 584, 37
append_store 540, 38
append_store 2157, 39
append_store 538, 40
append_store 1303, 41
append_store 578, 42
append_store 587, 43
append_store 1398, 44
append_store 556, 45
append_store 1865, 46
append_store 543, 47
append_store 2198, 48
append_store 580, 49
append_store 1862, 50
append_store 583, 51
append_store 553, 52
append_store 461, 53
append_store 903, 54
append_store 1435, 56
append_store 3800, 57
append_store 2623, 58
append_store 594, 59
append_store 579, 60
append_store 2160, 61
append_store 2191, 61
append_store 589, 62
append_store 549, 63
append_store 542, 64
append_store 3038, 65
append_store 544, 66
append_store 541, 67
append_store 1434, 68
append_store 577, 69
append_store 539, 70
append_store 1980, 71
append_store 546, 72
append_store 382, 73
append_store 3541, 74
append_store 520, 75
append_store 1436, 76
append_store 590, 77
append_store 971, 78
append_store 1917, 79
append_store 1040, 80
append_store 563, 81
append_store 522, 82
append_store 524, 83
append_store 526, 84
append_store 2154, 85
append_store 1334, 86
append_store 2552, 87
append_store 528, 88
append_store 1254, 89
append_store 2086, 90
append_store 3824, 91
append_store 1866, 92
append_store 1699, 93
append_store 1282, 94
append_store 530, 95
append_store 516, 96
append_store 560, 97
append_store 471, 98
append_store 1208, 99
append_store 532, 100
append_store 606, 101
append_store 534, 102
append_store 836, 103
append_store 551, 104
append_store 586, 105
append_store 564, 106
append_store 573, 108
append_store 1316, 108
append_store 547, 108
append_store 2233, 161