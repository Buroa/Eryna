require 'java'
java_import 'org.jboss.netty.buffer.ChannelBuffers'
java_import 'org.apollo.net.codec.game.GamePacket'

on :encode, 317, :open_interface do |event|
  builder = GamePacketBuilder.new 97
  builder.put DataType::SHORT, event.id
  builder.to_game_packet
end

on :encode, 317, :close_interface do |event|
  builder = GamePacketBuilder.new 219
  builder.to_game_packet
end

on :encode, 317, :switch_tab_interface do |event|
  builder = GamePacketBuilder.new 71
  builder.put DataType::SHORT, event.interface_id
  builder.put DataType::BYTE, DataTransformation::ADD, event.tab_id
  builder.to_game_packet
end

on :encode, 317, :open_interface_overlay do |event|
  builder = GamePacketBuilder.new 208
  builder.put DataType::SHORT, DataOrder::LITTLE, event.id
  builder.to_game_packet
end

on :encode, 317, :logout do |event|
  builder = GamePacketBuilder.new 109
  builder.to_game_packet
end

on :encode, 317, :open_interface_sidebar do |event|
  builder = GamePacketBuilder.new 248
  builder.put DataType::SHORT, DataTransformation::ADD, event.interface_id
  builder.put DataType::SHORT, event.sidebar_id
  builder.to_game_packet
end

on :encode, 317, :enter_amount do |event|
  GamePacket.new 27, PacketType::FIXED, ChannelBuffers::EMPTY_BUFFER
end

on :encode, 317, :set_interface_text do |event|
  builder = GamePacketBuilder.new 126, PacketType::VARIABLE_SHORT
  builder.put_string event.text
  builder.put DataType::SHORT, DataTransformation::ADD, event.interface_id
  builder.to_game_packet
end

on :encode, 317, :display_tab_interface do |event|
  builder = GamePacketBuilder.new 106
  builder.put DataType::BYTE, DataTransformation::NEGATE, event.tab
  builder.to_game_packet
end

on :encode, 317, :set_interface_item_model do |event|
  builder = GamePacketBuilder.new 246
  builder.put DataType::SHORT, DataOrder::LITTLE, event.interface_id
  builder.put DataType::SHORT, event.zoom
  builder.put DataType::SHORT, event.model_id
  builder.to_game_packet
end

on :encode, 317, :set_interface_component do |event|
  builder = GamePacketBuilder.new 171
  builder.put DataType::BYTE, (event.visible ? 0 : 1)
  builder.put DataType::SHORT, event.component_id
  builder.to_game_packet
end

on :encode, 317, :open_interface_dialogue do |event|
  builder = GamePacketBuilder.new 164
  builder.put DataType::SHORT, DataOrder::LITTLE, event.interface_id
  builder.to_game_packet
end

on :encode, 317, :set_interface_player_model do |event|
  builder = GamePacketBuilder.new 185
  builder.put DataType::SHORT, DataOrder::LITTLE, DataTransformation::ADD, event.interface_id
  builder.to_game_packet
end

on :encode, 317, :set_interface_npc_model do |event|
  builder = GamePacketBuilder.new 75
  builder.put DataType::SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.model_id
  builder.put DataType::SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.interface_id
  builder.to_game_packet
end

on :encode, 317, :set_interface_model_mood do |event|
  builder = GamePacketBuilder.new 200
  builder.put DataType::SHORT, event.interface_id & 0xFFFF
  builder.put DataType::SHORT, event.mood
  builder.to_game_packet
end