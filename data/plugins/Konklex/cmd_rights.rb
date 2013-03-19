require 'java'
java_import 'org.apollo.game.model.Player'

on :command, :promote, RIGHTS_OWNER do |player, command|
  args = command.arguments
  if args.length == 1
    find = args[0]
    if World.world.is_player_online find
      recruit = World.world.get_player find
      current = recruit.privilege_level.to_integer
      if current != 5
        recruit.set_privilege_level Player::PrivilegeLevel.value_of(current+1)
      end
    else
      player.send_message "Player is offline"
    end
  else
    player.send_message "Syntax: ::promote [player]"
  end
end

on :command, :demote, RIGHTS_OWNER do |player, command|
  args = command.arguments
  if args.length == 1
    find = args[0]
    if World.world.is_player_online find
      recruit = World.world.get_player find
      current = recruit.privilege_level.to_i
      if current != 0
        recruit.set_privilege_level Player::PrivilegeLevel.value_of(current-1)
      end
    else
      player.send_message "Player is offline"
    end
  else
    player.send_message "Syntax: ::demote [player]"
  end
end

on :command, :rights, RIGHTS_DEV do |player, command|
  args = command.arguments
  if args.length == 1
    rights = args[0].to_i
    if !(rights < 0 or rights > 5)
      recruit.set_privilege_level Player::PrivilegeLevel.value_of(rights)
    end
  else
    player.send_message "Syntax: ::rights [level]"
  end
end
