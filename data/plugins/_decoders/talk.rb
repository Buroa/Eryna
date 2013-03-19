require 'java'
java_import 'org.apollo.game.event.impl.ChatEvent'
java_import 'org.apollo.util.TextUtil'

on :decode, 317, 4 do |packet|
  reader = GamePacketReader.new packet
  effects = reader.get_unsigned DataType::BYTE, DataTransformation::SUBTRACT
  color = reader.get_unsigned DataType::BYTE, DataTransformation::SUBTRACT
  length = packet.length-2
  original = Java::byte[length].new
  reader.get_bytes_reverse DataTransformation::ADD, original
  uncompressed = TextUtil.uncompress original, length
  uncompressed = TextUtil.filter_invalid_characters uncompressed
  uncompressed = TextUtil.capitalize uncompressed
  recompressed = Java::byte[length].new
  TextUtil.compress uncompressed, recompressed
  ChatEvent.new uncompressed, recompressed, color, effects
end