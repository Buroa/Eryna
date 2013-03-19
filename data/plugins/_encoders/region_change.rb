on :encode, 317, :region_change do |event|
  builder = GamePacketBuilder.new 73
  builder.put DataType::SHORT, DataTransformation::ADD, event.position.central_region_x
  builder.put DataType::SHORT, event.position.central_region_y
  builder.to_game_packet
end