require 'java'

java_import 'org.apollo.game.model.Position'


on :command, :tn4ne do |player, command|
  player.teleport Position.new(3370, 3125, 0)
end

on :command, :pk do |player, command|
  player.teleport Position.new(2540, 4716, 0)
end

on :command, :pk1v1 do |player, command|
  player.teleport Position.new(3243, 3517, 0)
end

on :command, :pkship do |player, command|
  player.teleport Position.new(1891, 4825, 0)
end

on :command, :skiller, RIGHTS_MEMBER do |player, command|
  player.teleport Position.new(2860, 2945, 0)
end 

on :command, :mall do |player, command|
  player.teleport Position.new(2037, 4523, 0)
end

on :command, :shops do |player, command|
  player.teleport Position.new(2037, 4523, 0)
end

on :command, :train do |player, command|
  player.teleport Position.new(2670, 3709, 0)
end