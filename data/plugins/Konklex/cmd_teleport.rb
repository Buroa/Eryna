require 'java'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.World'

on :command, :pos, RIGHTS_MOD do |player, command|
  player.send_message "You are at: " + player.position.to_s
end

on :command, :tele, RIGHTS_DEV do |player, command|
  args = command.arguments
  if (2..3).include? args.length
    x = args[0].to_i
    y = args[1].to_i
    z = args.length == 3 ? args[2].to_i : 0

    player.teleport Position.new(x, y, z), false
  else
    player.send_message "Syntax: ::tele [x] [y] [z=0]"
  end
end

on :command, :teleto, RIGHTS_MOD do |player, command|
  args = command.arguments
  if args.length == 1
    other = World.world.get_player(args[0].sub("_", " "))
    if other != nil
      player.teleport other.position, true
    else
      player.send_message "That player is currently offline."
    end
  else
    player.send_message "Syntax: ::teleto [player]"
  end
end

on :command, :teletome, RIGHTS_DEV do |player, command|
  args = command.arguments
  if args.length == 1
    other = World.world.get_player(args[0].sub("_", " "))
    if other != nil
      other.teleport player.position, true
    else
      player.send_message "That player is currently offline."
    end
  else
    player.send_message "Syntax: ::teletome [player]"
  end
end