require 'java'
java_import 'org.apollo.game.event.impl.MagicOnPlayerEvent'
java_import 'org.apollo.game.event.impl.MagicOnObjectEvent'
java_import 'org.apollo.game.event.impl.MagicOnGroundEvent'
java_import 'org.apollo.game.event.impl.MagicOnNpcEvent'

on :decode, 317, 249 do |packet|
  reader = GamePacketReader.new packet
  index = reader.get_signed DataType::SHORT, DataTransformation::ADD
  spell = reader.get_signed DataType::SHORT, DataOrder::LITTLE
  MagicOnPlayerEvent.new index, spell
end

on :decode, 317, 35 do |packet|
  reader = GamePacketReader.new packet
  x = reader.get_signed DataType::SHORT, DataOrder::LITTLE
  id = reader.get_signed DataType::SHORT, DataOrder::BIG, DataTransformation::ADD
  y = reader.get_signed DataType::SHORT, DataOrder::BIG, DataTransformation::ADD
  object = reader.get_signed DataType::SHORT, DataOrder::LITTLE
  MagicOnObjectEvent.new x, y, id, object
end

on :decode, 317, 181 do |packet|
  reader = GamePacketReader.new packet
  y = reader.get_signed DataType::SHORT, DataOrder::LITTLE
  item = reader.get_signed DataType::SHORT
  x = reader.get_signed DataType::SHORT, DataOrder::LITTLE
  magic = reader.get_signed DataType::SHORT, DataTransformation::ADD
  MagicOnGroundEvent.new x, y,magic, item
end

on :decode, 317, 131 do |packet|
  reader = GamePacketReader.new packet
  index = reader.get_signed DataType::SHORT, DataOrder::LITTLE, DataTransformation::ADD
  spell = reader.get_signed DataType::SHORT, DataTransformation::ADD
  MagicOnNpcEvent.new index, spell
end