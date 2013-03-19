require 'java'
java_import 'org.apollo.game.event.impl.CommandEvent'

on :decode, 317, 103 do |packet|
  reader = GamePacketReader.new packet
  CommandEvent.new reader.string
end