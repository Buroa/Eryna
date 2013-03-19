require 'java'
java_import 'org.apollo.game.model.Graphic'

on :command, :gfx, RIGHTS_DEV do |player, command|
  args = command.arguments
  if (1..3).include? args.length
    id = args[0].to_i
    delay = args.length >= 2 ? args[1].to_i : 0
    height = args.length == 3 ? args[2].to_i : 0

    player.play_graphic Graphic.new(id, delay, height)
  else
    player.send_message "Syntax: ::gfx [id] [delay=0] [height=0]"
  end
end
