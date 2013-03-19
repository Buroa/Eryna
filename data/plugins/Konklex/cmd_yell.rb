require 'java'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.event.impl.YellEvent'

YELL_DATA = {}
YELL_OFF  = []

on :command, :yell do |player, command|
  args = command.arguments
  if args.length > 0
    # checks if player is muted
    if player.settings.muted
      player.send_message "You are muted and cannot yell!"
      return
    end

    # extracts the arguments into one string
    yell_text = command.arguments.to_a.join(" ").capitalize

    # construct the event
    rights = player.privilege_level.to_integer
    prefix = YELL_DATA[rights]
    event = YellEvent.new prefix, player.name.capitalize, yell_text, rights

    # send it to the players
    World.world.player_repository.each do |receiver|
      if not YELL_OFF.include? receiver.name
        receiver.send event
      end
    end

    # send it to the irc channel
    $pirc.send_message "#eryna", "*** [ 4YELL1 ]: #{player.name.capitalize}: #{yell_text}"
  end
end

on :command, :yellon do |player, command|
  YELL_OFF.delete player.name
end

on :command, :yelloff do |player, command|
  YELL_OFF.push player.name
end

def append_yell_data(rights, prefix)
  YELL_DATA[rights] = prefix
end

append_yell_data 0, "User"
append_yell_data 1, "@gre@Member"
append_yell_data 2, "@blu@Mod"
append_yell_data 3, "@red@Admin"
append_yell_data 4, "@or1@Developer"
append_yell_data 5, "@str@Owner"