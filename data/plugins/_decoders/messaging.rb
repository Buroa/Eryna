require 'java'
java_import 'org.apollo.util.TextUtil'
java_import 'org.apollo.game.model.Frenemy'
java_import 'org.apollo.game.event.impl.FrenemyEvent'
java_import 'org.apollo.game.event.impl.PrivateChatEvent'

on :decode, 317, 188, 215, 133, 74 do |packet|
  reader = GamePacketReader.new packet
  player = reader.get_signed DataType::LONG, DataTransformation::QUADRUPLE
  frenemy = Frenemy.new player, (packet.opcode == 188 or packet.opcode == 215) ? true : false
  FrenemyEvent.new frenemy, (packet.opcode == 215 or packet.opcode == 74) ? true : false
end

on :decode, 317, 126 do |packet|
  reader = GamePacketReader.new packet
  player = reader.get_signed DataType::LONG, DataTransformation::QUADRUPLE
  length = packet.length - 8
  original = Java::byte[length].new
  reader.get_bytes original
  uncompressed = TextUtil.uncompress original, length
  uncompressed = TextUtil.filter_invalid_characters uncompressed
  uncompressed = TextUtil.capitalize uncompressed
  recompressed = Java::byte[length].new
  TextUtil::compress uncompressed, recompressed
  PrivateChatEvent.new player, recompressed
end