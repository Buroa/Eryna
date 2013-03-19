on :encode, 317, :song do |event|
  builder = nil
  case event.type
    when 1
      builder = GamePacketBuilder.new 74
      builder.put DataType::SHORT, DataOrder::LITTLE, event.sound
    when 2
      builder = GamePacketBuilder.new 121
      builder.put DataType::SHORT, DataOrder::LITTLE, event.sound
      builder.put DataType::SHORT, DataOrder::LITTLE, event.delay
    when 3
      builder = GamePacketBuilder.new 174
      builder.put DataType::SHORT, DataOrder::LITTLE, event.sound
      builder.put DataType::SHORT, DataOrder::LITTLE, event.sound_type
      builder.put DataType::SHORT, DataOrder::LITTLE, event.delay
  end
  builder.to_game_packet
end