java_import 'java.lang.Byte'

on :encode, 317, :config do |event|
  builder = nil
  value = event.value
  if value > Byte::MIN_VALUE and value < Byte::MAX_VALUE
    builder = GamePacketBuilder.new 36
    builder.put DataType::SHORT, DataOrder::LITTLE, event.id
    builder.put DataType::BYTE, value & 0xFF
  else
    builder = GamePacketBuilder.new 87
    builder.put DataType::SHORT, DataOrder::LITTLE, event.id
    builder.put DataType::INT, DataOrder::MIDDLE, value
  end
  builder.to_game_packet
end

on :encode, 317, :build_player_menu do |event|
  builder = GamePacketBuilder.new 104, PacketType::VARIABLE_BYTE
  builder.put DataType::BYTE, DataTransformation::NEGATE, event.id
  builder.put DataType::BYTE, DataTransformation::ADD, (event.first ? 1 : 0)
  builder.put_string event.message
  builder.to_game_packet
end

on :encode, 317, :update_run_energy do |event|
  builder = GamePacketBuilder.new 110
  builder.put DataType::BYTE, event.energy
  builder.to_game_packet
end

on :encode, 317, :system_update do |event|
  builder = GamePacketBuilder.new 114
  builder.put DataType::SHORT, DataOrder::LITTLE, event.time
  builder.to_game_packet
end

on :encode, 317, :reset_buttons do |event|
  GamePacketBuilder.new(68).to_game_packet
end

on :encode, 317, :chat_privacy_settings do |event|
  builder = GamePacketBuilder.new 206
  builder.put DataType::BYTE, event.public_chat
  builder.put DataType::BYTE, event.private_chat
  builder.put DataType::BYTE, event.trade
  builder.to_game_packet
end

on :encode, 317, :hint_icon do |event|
  builder = GamePacketBuilder.new 254
  case event.val
    when 2
      builder.put DataType::BYTE, event.type
      builder.put DataType::SHORT, event.id
      builder.put DataType::TRI_BYTE, 0
    when 1
      builder.put DataType::BYTE, event.orient
      builder.put DataType::SHORT, event.pos.x
      builder.put DataType::SHORT, event.pos.y
      builder.put DataType::BYTE, event.pos.height
  end
  builder.to_game_packet
end

on :encode, 317, :flash_sidebar do |event|
  builder = GamePacketBuilder.new 24
  builder.put DataType::BYTE, DataTransformation::ADD, event.side_bar
  builder.to_game_packet
end

on :encode, 317, :minimap_state do |event|
  builder = GamePacketBuilder.new 99
  builder.put DataType::BYTE, event.state
  builder.to_game_packet
end

on :encode, 317, :open_welcome_screen do |event|
  builder = GamePacketBuilder.new 176
  builder.put DataType::BYTE, DataTransformation::NEGATE, event.days_since_last_recovery
  builder.put DataType::SHORT, DataTransformation::ADD, event.unread_messages
  builder.put DataType::BYTE, (event.member ? 1 : 0)
  builder.put DataType::INT, event.last_logged_ip
  builder.put DataType::SHORT, event.last_logged_in
  builder.to_game_packet
end

on :encode, 317, :display_weight do |event|
  builder = GamePacketBuilder.new 240
  builder.put DataType::SHORT, event.weight
  builder.to_game_packet
end

on :encode, 317, :camera_reset do |event|
  GamePacketBuilder.new(107).to_game_packet
end

on :encode, 317, :camera_shake do |event|
  builder = GamePacketBuilder.new 35
  builder.put DataType::BYTE, event.type
  for i in 0..3
    builder.put DataType::BYTE event.magnitudes[i]
  end
  builder.to_game_packet
end

on :encode, 317, :yell do |event|
  builder = GamePacketBuilder.new 222, PacketType::VARIABLE_BYTE
  builder.put_string event.prefix
  builder.put_string event.name
  builder.put_string event.message
  builder.put DataType::SHORT, event.rights
  builder.to_game_packet
end

on :encode, 317, :debug_message do |event|
  builder = GamePacketBuilder.new 223, PacketType::VARIABLE_BYTE
  builder.put_string event.message
  builder.put DataType::SHORT, event.type
  builder.to_game_packet
end