require 'java'

on :command, :pnpc, RIGHTS_DEV do |player, command|
  args = command.arguments

  if args.length == 1
    npc = args[0].to_i
    player.get_appearance.set_npc_id npc
    player.set_appearance player.appearance
  else
    player.send_message "Syntax: ::pnpc [npc]"
  end
end