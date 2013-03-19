require 'java'

java_import 'org.apollo.game.model.Position'


on :command, :tne4e do |player, command|
  player.teleport Position.new(3370, 3125, 0), true
end

on :command, :pk do |player, command|
  player.teleport Position.new(2540, 4716, 0), true
end

on :command, :pk1v1 do |player, command|
  player.teleport Position.new(3243, 3517, 0), true
end

on :command, :pkship do |player, command|
  player.teleport Position.new(1891, 4825, 0), true
end

on :command, :skiller, RIGHTS_MEMBER do |player, command|
  player.teleport Position.new(2860, 2945, 0), true
end 

on :command, :mall do |player, command|
  player.teleport Position.new(2037, 4523, 0), true
end

on :command, :shops do |player, command|
  player.teleport Position.new(2037, 4523, 0), true
end

on :command, :train do |player, command|
  player.teleport Position.new(2670, 3709, 0), true
end

on :command, :staffzone, RIGHTS_MOD do |player, command|
  player.teleport Position.new(2186, 3148, 0), true
end