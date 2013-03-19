require 'java'
java_import 'org.apollo.game.event.impl.PrivateChatLoadedEvent'
java_import 'org.apollo.game.event.impl.SendPrivateChatEvent'
java_import 'org.apollo.game.event.impl.SendFriendEvent'

STAFF_ENCODED_NAME = 36623716

# register the login player listener
on :login do |player|
  _messaging_notify_friends player, true
  player.frenemy_set.force_refresh
  player.send PrivateChatLoadedEvent.new(2)

  # are we staff? if so register us so staff can communicate
  if player.privilege_level.to_integer > 0
    ONLINE_STAFF.push player
    player.send SendFriendEvent.new(STAFF_ENCODED_NAME, 10)
  end
end

# register the logout player listener
on :logout do |player|
  _messaging_notify_friends player, false

  # are we staff? remove us from the list
  if player.privilege_level.to_integer > 0
    ONLINE_STAFF.delete player
  end
end

# handle the frenemy events
on :event, :frenemy do |ctx, player, event|
  if event.removing
    player.frenemy_set.remove event.frenemy
  else
    player.frenemy_set.add event.frenemy
  end
end

# handles the private messaging portion
on :event, :private_chat do |ctx, player, event|
  if event.friend == STAFF_ENCODED_NAME and player.privilege_level.to_integer > 0
    _messaging_send_staff_message player, event.message
  else
    _messaging_send_message player, event.friend, event.message
  end
end