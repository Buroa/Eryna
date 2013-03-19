on :encode, 317, :id_assignment do |event|
  builder = GamePacketBuilder.new 249
  builder.put DataType::BYTE, DataTransformation::ADD, (event.members ? 1 : 0)
  builder.put DataType::SHORT, DataOrder::LITTLE, DataTransformation::ADD, event.id
  builder.to_game_packet
end