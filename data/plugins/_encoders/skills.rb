on :encode, 317, :update_skill do |event|
  builder = GamePacketBuilder.new 134
  skill = event.skill
  builder.put DataType::BYTE, event.id
  builder.put DataType::INT, DataOrder::MIDDLE, skill.experience
  builder.put DataType::BYTE, skill.current_level
  builder.to_game_packet
end