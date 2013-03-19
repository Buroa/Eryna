require 'java'
java_import 'org.apollo.game.event.impl.SongEvent'

on :command, :sound do |player, command|
  args = command.arguments
  if (1..3).include? args.length
    id = args[0].to_i
    delay = args.length >= 2 ? args[1].to_i : 0
    type = args.length == 3 ? args[2].to_i : 0

    if args.length == 1
      player.send SongEvent.new(id)
    elsif args.length == 2
      player.send SongEvent.new(id, delay)
    elsif args.length == 3
      player.send SongEvent.new(id, type, delay)
    end
  else
    player.send_message "Syntax: ::sound [id] [delay=0] [type=0]"
  end
end