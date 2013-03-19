require 'java'

on :button, 153 do |player|
  player.walking_queue.set_running_queue true
end

on :button, 152 do |player|
  player.walking_queue.set_running_queue false
end

on :button, 19158 do |player|
  running = !player.walking_queue.get_running_queue
  player.walking_queue.set_running_queue running
end