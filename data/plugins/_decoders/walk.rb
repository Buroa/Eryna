require 'java'
java_import 'org.apollo.game.event.impl.WalkEvent'

on :decode, 317, 248, 164, 98 do |packet|
  reader = GamePacketReader.new packet
  length = packet.length
  if packet.opcode == 248
    length -= 14
  end
  steps = (length - 5) / 2
  path = {}
  for i in 0..steps
    if i != steps
      path[i] ||= {}
    end
  end
  x = reader.get_unsigned DataType::SHORT, DataOrder::LITTLE, DataTransformation::ADD
  for i in 0..steps
    if i != steps
      path[i][0] = reader.get_signed DataType::BYTE
      path[i][1] = reader.get_signed DataType::BYTE
    end
  end
  y = reader.get_unsigned DataType::SHORT, DataOrder::LITTLE
  run = reader.get_unsigned DataType::BYTE, DataTransformation::NEGATE
  positions = Position[steps+1].new
  positions[0] = Position.new x, y
  for i in 0..steps
    if i != steps
      positions[i+1] = Position.new(path[i][0] + x, path[i][1] + y)
    end
  end
  WalkEvent.new positions, (run == 1 ? true : false)
end