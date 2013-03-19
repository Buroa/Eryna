LOGS = {}

EXPIRED_LOGS = {}

# Only normal trees support atm

class Log
  attr_reader :id, :objects, :level, :exp, :respawn

  def initialize(id, objects, level, exp, respawn)
    @id = id
    @objects = objects
    @level = level
    @exp = exp
    @respawn = respawn
  end
end

def append_log(log)
  log.objects.each do |obj, expired_obj|
    LOGS[obj] = log
    EXPIRED_LOGS[expired_obj] = true
  end
end

NORMAL_TREE = {
 1315 => 1342, 1316 => 1342, 1318 => 1342, 1319 => 1342,
 3033 => 1342, 1278 => 1342, 1276 => 1342, 1286 => 1342,
 1282 => 1342, 1383 => 1342, 1286 => 1342,
}

OAK_TREE = {
 1281 => 1342
}
 
WILLOW_TREE = {
 1308 => 1342
}
 
MAPLE_TREE = {
 1307 => 1342
}
 
YEW_TREE = {
 1309 => 1342
}

MAGIC_TREE = {
 1306 => 1342
}

append_log Log.new(1511, NORMAL_TREE,  1,    25,  1) # normal log
append_log Log.new(1521, OAK_TREE,    15,    38,  3) # oak log
append_log Log.new(1519, WILLOW_TREE, 30,    68,  5) # willow log
append_log Log.new(1517, MAPLE_TREE,  45,   100,  9) # maple log
append_log Log.new(1515, YEW_TREE,    60,   175, 12) # yew log
append_log Log.new(1513, MAGIC_TREE,  75,   250, 15) # magic log
