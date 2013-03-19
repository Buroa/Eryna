# This command is executed upon a http request to the server

on :command, :_vote, RIGHTS_OWNER do |player, command|
  args = command.arguments
  if args.length == 2
    name = args[0].downcase
    id = args[1].to_i

    vote_site = VOTE_SITES[id]
    if vote_site != nil
      vote_site.add name
      player = World.world.get_player name
      if player != nil and VOTING[player.name] != nil
        player.send SetInterfaceTextEvent.new(2806, "@gre@Vote has been recieved")
      end
    end
  else
    player.send_message "Syntax: _vote [player] [id]"
  end
end