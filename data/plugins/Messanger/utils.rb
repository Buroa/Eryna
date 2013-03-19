require 'java'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.event.impl.SendFriendEvent'
java_import 'org.apollo.util.NameUtil'

PRIVATE_CHATS = {}
ONLINE_STAFF  = [] # used for staff messaging

def _messaging_notify_friends(player, login)
  _messaging_contains_friend(player.name).each do |friends|
    friends.send SendFriendEvent.new(player.encoded_name, login ? 10 : 0)
  end
end

def _messaging_contains_friend(friend)
  _messaging_container = []
  World.world.player_repository.each do |player|
    if player.frenemy_set.is_friend friend
      _messaging_container.push player
    end
  end
  return _messaging_container
end

def _messaging_send_message(player, friend_encoded, message)
  friend_decoded = NameUtil::decodeBase37 friend_encoded
  if World.world.is_player_online friend_decoded
    friend = World.world.get_player friend_decoded
    message_count = _messaging_append_chat friend
    message_event = SendPrivateChatEvent.new player.encoded_name, player.privilege_level.to_integer, message, message_count
    friend.send message_event
  end
end

def _messaging_send_staff_message(player, message)
  ONLINE_STAFF.each do |staff|
    if staff.name != player.name
      message_count = _messaging_append_chat staff
      message_event = SendPrivateChatEvent.new player.encoded_name, player.privilege_level.to_integer, message, message_count
      staff.send message_event
    end
  end
end

def _messaging_append_chat(player)
  if PRIVATE_CHATS[player] != nil
    PRIVATE_CHATS[player] += 1
  else
    PRIVATE_CHATS[player] = 1
  end
  return PRIVATE_CHATS[player]
end