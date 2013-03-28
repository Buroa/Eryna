on :encode, 317, :create_ground do |event|
  builder = GamePacketBuilder.new 44
  item = event.ground_item
  position = event.position
  offset = position.local_sector_x << 4 | position.local_sector_y
  builder.put DataType::SHORT, DataOrder::LITTLE, DataTransformation::ADD, item.item.id
  builder.put DataType::SHORT, item.item.amount
  builder.put DataType::BYTE, offset
  builder.to_game_packet
end

on :encode, 317, :destroy_ground do |event|
  builder = GamePacketBuilder.new 156
  position = event.position
  offset = position.local_sector_x << 4 | position.local_sector_y
  builder.put DataType::BYTE, DataTransformation::ADD, offset
  builder.put DataType::SHORT, event.ground_item.item.id
  builder.to_game_packet
end

on :encode, 317, :create_object do |event|
  builder = GamePacketBuilder.new 151
  object = event.object
  position = event.position
  offset = position.local_sector_x << 4 | position.local_sector_y
  builder.put DataType::BYTE, DataTransformation::ADD, offset
  builder.put DataType::SHORT, DataOrder::LITTLE, object.definition.id
  builder.put DataType::BYTE, DataTransformation::SUBTRACT, (object.get_type << 2)+(object.rotation & 3)
  builder.to_game_packet
end

on :encode, 317, :destroy_object do |event|
  builder = GamePacketBuilder.new 101
  object = event.object
  position = event.position
  offset = position.local_sector_x << 4 | position.local_sector_y
  builder.put DataType::BYTE, DataTransformation::NEGATE, (object.get_type << 2)+(object.rotation & 3)
  builder.put DataType::BYTE, offset
  builder.to_game_packet
end

on :encode, 317, :projectile do |event|
  builder = GamePacketBuilder.new 117
  position = event.position
  offset = position.local_sector_x << 4 | position.local_sector_y
  builder.put DataType::BYTE, offset
  builder.put DataType::BYTE, event.offset_x_
  builder.put DataType::BYTE, event.offset_y_
  builder.put DataType::SHORT, event.lock_on
  builder.put DataType::SHORT, event.projectile_id
  builder.put DataType::BYTE, event.start_height
  builder.put DataType::BYTE, event.end_height
  builder.put DataType::SHORT, event.delay
  builder.put DataType::SHORT, event.duration
  builder.put DataType::BYTE, event.curve
  builder.put DataType::BYTE, 64
  builder.to_game_packet
end

on :encode, 317, :graphic do |event|
  builder = GamePacketBuilder.new 4
  graphic = event.graphic
  position = event.position
  offset = position.local_sector_x << 4 | position.local_sector_y
  builder.put DataType::BYTE, offset
  builder.put DataType::SHORT, graphic.id
  builder.put DataType::BYTE, graphic.height
  builder.put DataType::SHORT, graphic.delay
  builder.to_game_packet
end

on :encode, 317, :animate_object do |event|
  builder = GamePacketBuilder.new 160
  object = event.object
  animation = event.animation
  position = event.position
  offset = position.local_sector_x << 4 | position.local_sector_y
  builder.put DataType::BYTE, DataTransformation::SUBTRACT, offset
  builder.put DataType::BYTE, DataTransformation::SUBTRACT, (object.type << 2)+(object.rotation & 3)
  builder.put DataType::SHORT, DataTransformation::ADD, animation.id
  builder.to_game_packet
end

on :encode, 317, :construct_map_region do |event|
  builder = GamePacketBuilder.new 241, PacketType::VARIABLE_SHORT
  palette = event.palette
  position = event.position
  builder.put DataType::SHORT, DataTransformation::ADD, position.central_region_y
  builder.switch_to_bit_access
  (0..3).each do |z|
    (0..12).each do |x|
      (0..12).each do |y|
        tile = palette.get_tile x, y, z
        builder.put_bit tile != nil
        if tile != nil
          builder.put_bits 26, tile.hash_code
        end
      end
    end
  end
  builder.switch_to_byte_access
  builder.put DataType::SHORT, position.central_region_x
  builder.to_game_packet
end

on :encode, 317, :position do |event|
  builder = GamePacketBuilder.new 85
  base = event.base
  pos = event.position
  builder.put DataType::BYTE, DataTransformation::NEGATE, pos.local_y(base) - event.offset_y
  builder.put DataType::BYTE, DataTransformation::NEGATE, pos.local_x(base) - event.offset_x
  builder.to_game_packet
end

on :encode, 317, :region_update do |event|
  builder = GamePacketBuilder.new 60, PacketType::VARIABLE_SHORT
  base = event.base
  pos = event.position
  builder.put DataType::BYTE, pos.local_y(base)
  builder.put DataType::BYTE, DataTransformation::NEGATE, pos.local_x(base)
  event.events.each do |e|
    encoder = World.world.release.get_event_encoder e.class
    packet = encoder.encode e
    builder.put DataType::BYTE, packet.opcode
    builder.put_bytes packet.payload
  end
  builder.to_game_packet
end