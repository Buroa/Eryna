on :command, :item, RIGHTS_DEV do |player, command|
  args = command.arguments
  if (1..2).include? args.length
    id = args[0].to_i
    amount = args.length == 2 ? args[1].to_i : 1

    player.inventory.add id, amount
  else
    player.send_message "Syntax: ::item [id] [amount=1]"
  end
end

on :command, :destroy, RIGHTS_DEV do |player, command|
  args = command.arguments
  if (1..2).include? args.length
    id = args[0].to_i
    amount = args.length == 2 ? args[1].to_i : 1

    player.inventory.remove id, amount
  else
    player.send_message "Syntax: ::destroy [id] [amount=1]"
  end
end

on :command, :give, RIGHTS_OWNER do |player, command|
  args = command.arguments
  if (2..3).include? args.length
    name = args[0]
    id = args[1].to_i
    amount = args.length == 3 ? args[2].to_i : 1

    recieve = World.world.get_player(name)
    if recieve != nil
      recieve.inventory.add id, amount
    else
      player.send_message "That player is currently offline."
    end
  else
    player.send_message "Syntax: ::give [player] [id] [amount=1]"
  end
end

on :command, :empty, RIGHTS_ADMIN do |player, command|
  player.inventory.clear
end
