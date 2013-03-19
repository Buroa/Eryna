require 'java'
java_import 'org.apollo.game.event.impl.FirstPlayerOptionEvent'
java_import 'org.apollo.game.event.impl.SecondPlayerOptionEvent'
java_import 'org.apollo.game.event.impl.ThirdPlayerOptionEvent'
java_import 'org.apollo.game.event.impl.FourthPlayerOptionEvent'
java_import 'org.apollo.game.event.impl.PlayerReportEvent'
java_import 'org.apollo.game.event.impl.PlayerIdleEvent'

on :decode, 317, 73 do |packet|
  reader = GamePacketReader.new packet
  id = reader.get_unsigned DataType::SHORT, DataOrder::LITTLE
  FirstPlayerOptionEvent.new id
end

on :decode, 317, 139 do |packet|
  reader = GamePacketReader.new packet
  id = reader.get_signed DataType::SHORT, DataOrder::LITTLE
  SecondPlayerOptionEvent.new id
end

on :decode, 317, 39 do |packet|
  reader = GamePacketReader.new packet
  id = reader.get_signed DataType::SHORT, DataOrder::LITTLE
  ThirdPlayerOptionEvent.new id
end

on :decode, 317, 153 do |packet|
  reader = GamePacketReader.new packet
  id = reader.get_signed DataType::SHORT, DataOrder::LITTLE
  FourthPlayerOptionEvent.new id
end

on :decode, 317, 218 do |packet|
  reader = GamePacketReader.new packet
  player = reader.get_signed DataType::LONG, DataTransformation::QUADRUPLE
  rule = reader.get_unsigned DataType::BYTE
  mute = reader.get_unsigned DataType::BYTE
  PlayerReportEvent.new player, rule, (mute == 1 ? true : false)
end

on :decode, 317, 202 do |packet|
  PlayerIdleEvent.new
end