require 'java'

on :command, :hide, RIGHTS_DEV do |player, command|
  player.settings.set_hide true
end

on :command, :show, RIGHTS_DEV do |player, command|
  player.settings.set_hide false
end