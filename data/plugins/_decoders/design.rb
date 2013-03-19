require 'java'
java_import 'org.apollo.game.model.Gender'
java_import 'org.apollo.game.model.Appearance'
java_import 'org.apollo.game.event.impl.CharacterDesignEvent'

on :decode, 317, 101 do |packet|
  reader = GamePacketReader.new packet
  value = reader.get_unsigned DataType::BYTE
  style = Java::int[7].new
  for i in 0..style.length-1
    style[i] = reader.get_unsigned DataType::BYTE
  end
  color = Java::int[5].new
  for i in 0..color.length-1
    color[i] = reader.get_unsigned DataType::BYTE
  end
  gender = value == Gender::MALE.to_integer ? Gender::MALE : Gender::FEMALE
  CharacterDesignEvent.new Appearance.new(gender, style, color)
end