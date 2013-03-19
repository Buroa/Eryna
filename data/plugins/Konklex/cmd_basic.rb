require 'java'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.util.NameUtil'
java_import 'org.apollo.util.TextUtil'

on :command, :players do |player, command|
  players = World.world.player_repository.size
  player.send_message "There is currently #{players} players online."
end

on :command, :msg, RIGHTS_OWNER do |player, command|
  if command.arguments.length == 2
    reciever = NameUtil::encodeBase37 command.arguments[0]
    message_string = command.arguments[1].gsub("_", " ")
    message_string = TextUtil::filter_invalid_characters message_string
    message_string = TextUtil::capitalize message_string
    message_bytes  = Java::byte[message_string.length].new
    TextUtil::compress message_string, message_bytes
    _messaging_send_message player, reciever, message_bytes
  end
end