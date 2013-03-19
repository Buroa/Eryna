on :encode, 317, :send_friend do |event|
  builder = GamePacketBuilder.new 50
  builder.put DataType::LONG, DataTransformation::QUADRUPLE, event.friend
  builder.put DataType::BYTE, event.status
  builder.to_game_packet
end

on :encode, 317, :send_ignore do |event|
  builder = GamePacketBuilder.new 214, PacketType::VARIABLE_SHORT
  event.friends.each do |ignore|
    builder.put DataType::LONG, DataTransformation::QUADRUPLE, ignore
  end
  builder.to_game_packet
end

on :encode, 317, :send_private_chat do |event|
  builder = GamePacketBuilder.new 196, PacketType::VARIABLE_BYTE
  builder.put DataType::LONG, event.from
  builder.put DataType::INT, event.id
  builder.put DataType::BYTE, event.rights
  builder.put_bytes event.message
  builder.to_game_packet
end

on :encode, 317, :private_chat_loaded do |event|
  builder = GamePacketBuilder.new 221
  builder.put DataType::BYTE, event.id
  builder.to_game_packet
end