require 'java'
java_import 'org.apollo.game.event.impl.EnteredAmountEvent'
java_import 'org.apollo.game.event.impl.ClosedInterfaceEvent'
java_import 'org.apollo.game.event.impl.ButtonEvent'
java_import 'org.apollo.game.event.impl.DialogueContinueEvent'

on :decode, 317, 208 do |packet|
  reader = GamePacketReader.new packet
  amount = reader.get_unsigned DataType::INT
  EnteredAmountEvent.new amount
end

on :decode, 317, 130 do |packet|
  ClosedInterfaceEvent.new
end

on :decode, 317, 185 do |packet|
  reader = GamePacketReader.new packet
  interface = reader.get_unsigned DataType::SHORT
  ButtonEvent.new interface
end

on :decode, 317, 40 do |packet|
  reader = GamePacketReader.new packet
  interface = reader.get_unsigned DataType::SHORT
  DialogueContinueEvent.new interface
end