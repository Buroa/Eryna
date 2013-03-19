require 'java'
java_import 'org.apollo.game.event.impl.ConfigEvent'

on :command, :config, RIGHTS_DEV do |player, command|
  args = command.arguments
  if args.length == 2
    id = args[0].to_i
    config = args[1].to_i
    player.send ConfigEvent.new(id, config)
  else
    player.send_message "Syntax: ::config [id] [config]"
  end
end