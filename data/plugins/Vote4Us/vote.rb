VOTING = {}

# register the command
on :command, :vote do |player, command|
  args = command.arguments
  if args.length == 0
    voted = voted_on_all(player.name)
    if voted == nil
      player.interface_set.open_walkable -1
      player.send_message "@blu@Thanks for supporting our server!"
      player.inventory.add 995, 50000 * VOTE_SITES.size
      voting_points = player.voting_points
      player.set_voting_points player.voting_points+VOTE_SITES.size
      remove_votes player.name
      VOTING.delete player.name
    else
      is_voting = VOTING[player.name] != nil
      if not is_voting
        player.send ConfigEvent.new(560, 1)
      end

      player.send SetInterfaceTextEvent.new(2805, voted.name)
      player.send SetInterfaceTextEvent.new(2806, "@red@Waiting for vote")
      if not is_voting
        player.interface_set.open_walkable 2804
      end

      player.send_message "#{voted.url.gsub("%usrrep%", player.name)}:url:"
      if not is_voting
        VOTING[player.name] = true
      end
    end
  elsif args.length == 1
    cmd = args[0].downcase
    if cmd == "points"
      player.send_message "You have@blu@ #{player.voting_points} @bla@voting points."
    elsif cmd == "shop"
      World.world.stores.open_shop player, 201
    end
  end
end

# function to check if we voted on all of the sites
def voted_on_all(name)
  VOTE_SITES.each do |id, site|
    if not site.has_voted(name.downcase)
      return site
    end
  end
  return nil
end

# function to remove all the votes
# normally used to claim rewards
def remove_votes(name)
  VOTE_SITES.each do |id, site|
    site.remove name.downcase
  end
end