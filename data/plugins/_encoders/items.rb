require 'java'
java_import 'org.apollo.game.model.Item'
java_import 'org.apollo.game.model.SlottedItem'

on :encode, 317, :update_items do |event|
  builder = GamePacketBuilder.new 53, PacketType::VARIABLE_SHORT
  items = event.items
  builder.put DataType::SHORT, event.interface_id
  builder.put DataType::SHORT, items.length
  items.each do |item|
    id = item == nil ? -1 : item.id
    amount = item == nil ? 0 : item.amount
    if amount > 254
      builder.put DataType::BYTE, 255
      builder.put DataType::INT, DataOrder::INVERSED_MIDDLE, amount
    else
      builder.put DataType::BYTE, amount
    end
    builder.put DataType::SHORT, DataOrder::LITTLE, DataTransformation::ADD, id+1
  end
  builder.to_game_packet
end

on :encode, 317, :update_slotted_items do |event|
  builder = GamePacketBuilder.new 34, PacketType::VARIABLE_SHORT
  items = event.slotted_items
  builder.put DataType::SHORT, event.interface_id
  items.each do |slotteditem|
    builder.put_smart slotteditem.slot
    item = slotteditem.item
    id = item == nil ? -1 : item.id
    amount = item == nil ? 0 : item.amount
    builder.put DataType::SHORT, id+1
    if amount > 254
      builder.put DataType::BYTE, 255
      builder.put DataType::INT, amount
    else
      builder.put DataType::BYTE, amount
    end
  end
  builder.to_game_packet
end