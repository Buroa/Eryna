F_LOGS = {}

class F_Log
  attr_reader :log, :xp, :level

  def initialize(log, xp, level)
    @log = log
    @xp = xp
    @level = level
  end
end

def append_log(log)
  F_LOGS[log.log] = log
end

append_log F_Log.new(1511, 40,   1) # Regular Log
append_log F_Log.new(2862, 40,   1) # Achey Tree Log
append_log F_Log.new(1521, 60,  15) # Oak Log
append_log F_Log.new(1519, 105, 30) # Willow Log
append_log F_Log.new(6333, 105, 35) # Teak Log
append_log F_Log.new(1517, 135, 45) # Maple Log
append_log F_Log.new(6332, 158, 50) # Mahogany Log
append_log F_Log.new(1515, 203, 60) # Yew Log
append_log F_Log.new(1513, 304, 75) # Magic Log