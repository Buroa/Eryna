on :encode, 317, :server_message do |event|
  builder = GamePacketBuilder.new 253, PacketType::VARIABLE_BYTE
  builder.put_string event.message
  builder.to_game_packet
end