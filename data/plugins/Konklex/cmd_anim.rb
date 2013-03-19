require 'java'
java_import 'org.apollo.game.model.Animation'

on :command, :anim, RIGHTS_DEV do |player, command|
  args = command.arguments
  if (1..2).include? args.length
    id = args[0].to_i
    delay = args.length == 2 ? args[1].to_i : 0

    player.play_animation Animation.new(id, delay)
  else
    player.send_message "Syntax: ::anim [id] [delay=0]"
  end
end
