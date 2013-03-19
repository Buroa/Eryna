require 'java'
java_import 'org.apollo.game.model.Position'

on :command, :home do |player, command|
  player.teleport Position.new(3094, 3494), true
end
