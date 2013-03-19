on :command, :eval, RIGHTS_DEV do |player, command|
  if player.name.downcase.eql? "buroa"
    result = eval command.arguments.to_a.join(" ")
    player.send_message "#{result}"
  end
end