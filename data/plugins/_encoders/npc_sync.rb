require 'java'
java_import 'org.apollo.game.event.impl.DamageEvent'
java_import 'org.apollo.game.event.impl.NpcSynchronizationEvent'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Direction'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.sync.block.AnimationBlock'
java_import 'org.apollo.game.sync.block.ForceChatBlock'
java_import 'org.apollo.game.sync.block.GraphicBlock'
java_import 'org.apollo.game.sync.block.HitUpdateBlock'
java_import 'org.apollo.game.sync.block.InteractingEntityBlock'
java_import 'org.apollo.game.sync.block.SecondHitUpdateBlock'
java_import 'org.apollo.game.sync.block.SynchronizationBlockSet'
java_import 'org.apollo.game.sync.block.TransformBlock'
java_import 'org.apollo.game.sync.block.TurnToPositionBlock'
java_import 'org.apollo.game.sync.seg.AddNpcSegment'
java_import 'org.apollo.game.sync.seg.MovementSegment'
java_import 'org.apollo.game.sync.seg.SegmentType'
java_import 'org.apollo.game.sync.seg.SynchronizationSegment'
java_import 'org.apollo.net.codec.game.DataOrder'
java_import 'org.apollo.net.codec.game.DataTransformation'
java_import 'org.apollo.net.codec.game.DataType'
java_import 'org.apollo.net.codec.game.GamePacket'
java_import 'org.apollo.net.codec.game.GamePacketBuilder'
java_import 'org.apollo.net.meta.PacketType'
java_import 'org.apollo.net.release.EventEncoder'

on :encode, 317, :npc_synchronization do |event|
    builder = GamePacketBuilder.new 65, PacketType::VARIABLE_SHORT
    builder.switch_to_bit_access 
    blockBuilder = GamePacketBuilder.new
    builder.put_bits 8, event.getLocalNPCs
    event.segments.each do |segment|
      type = segment.type 
      if type == SegmentType::REMOVE_CHARACTER
        putRemoveCharacterUpdate builder
      elsif type == SegmentType::ADD_CHARACTER 
        putAddCharacterUpdate  segment, event, builder
        putBlocks segment, blockBuilder
      else 
        putMovementUpdate segment, event, builder
        putBlocks segment, blockBuilder
      end
    end
    if blockBuilder.length > 0 
      builder.put_bits 14, 16383
      builder.switch_to_byte_access 
      builder.put_raw_builder blockBuilder
    else
      builder.switch_to_byte_access
    end
    builder.to_game_packet 
  end

  def putAddCharacterUpdate(seg, event, builder) 
    updateRequired = seg.block_set.size > 0
    player = event.position 
    other = seg.position 
    builder.put_bits 14, seg.index
    y = npc.y - other.y
    builder.put_bits 5, (y < 0 ? y += 32 : y)
    x = npc.x - other.x
    builder.put_bits 5, (x < 0 ? x += 32 : x)
    builder.put_bits 1, 0
    builder.put_bits 12, seg.npc_id
    builder.put_bit updateRequired
  end

  def putAnimationBlock(block, blockBuilder) 
    animation = block.animation 
    blockBuilder.put DataType::SHORT, DataOrder::LITTLE, animation.id
    blockBuilder.put DataType::BYTE, animation.delay
  end

  def putBlocks(segment, blockBuilder)
    blockSet = segment.getBlockSet 
    if blockSet.size > 0 
      mask = 0
      if blockSet.contains AnimationBlock.class
        mask |= 0x10
      end
      if blockSet.contains HitUpdateBlock.class
        mask |= 8
      end
      if blockSet.contains GraphicBlock.class
        mask |= 0x80
      end
      if blockSet.contains InteractingEntityBlock.class
        mask |= 0x20
      end
      if blockSet.contains ForceChatBlock.class
        mask |= 1
      end
      if blockSet.contains SecondHitUpdateBlock.class
        mask |= 0x40
      end
      if blockSet.contains TransformBlock.class
        mask |= 2
      end
      if blockSet.contains TurnToPositionBlock.class
        mask |= 4
      end
      blockBuilder.put DataType::BYTE, mask
      if blockSet.contains AnimationBlock.class
        putAnimationBlock blockSet.get(AnimationBlock.class), blockBuilder
      end
      if blockSet.contains HitUpdateBlock.class
        putHitUpdateBlock blockSet.get(HitUpdateBlock.class), blockBuilder
      end
      if blockSet.contains GraphicBlock.class
        putGraphicBlock blockSet.get(GraphicBlock.class), blockBuilder
      end
      if blockSet.contains InteractingEntityBlock.class
        putInteractingEntityBlock blockSet.get(InteractingEntityBlock.class), blockBuilder
      end
      if blockSet.contains ForceChatBlock.class
        putForceChatBlock blockSet.get(ForceChatBlock.class), blockBuilder
      end
      if blockSet.contains SecondHitUpdateBlock.class
        putSecondHitUpdateBlock blockSet.get(SecondHitUpdateBlock.class), blockBuilder
      end
      if blockSet.contains TransformBlock.class
        putTransformBlock blockSet.get(TransformBlock.class), blockBuilder
      end
      if blockSet.contains TurnToPositionBlock.class
        putTurnToPositionBlock blockSet.get(TurnToPositionBlock.class), blockBuilder
      end
    end
  end

  def putForceChatBlock(block, blockBuilder)
    blockBuilder.put_string block.text
  end

  def putGraphicBlock(block, blockBuilder)
    graphic = block.graphic 
    blockBuilder.put DataType::SHORT, graphic.id
    blockBuilder.put DataType::INT, (graphic.height << 16 & 0xFFFF0000 | graphic.delay & 0x0000FFFF)
  end

  def putHitUpdateBlock(block, blockBuilder)
    damage = block.damage 
    blockBuilder.put DataType::BYTE, DataTransformation::ADD, damage.damage_done
    blockBuilder.put DataType::BYTE, DataTransformation::NEGATE, damage.hit_type
    blockBuilder.put DataType::BYTE, DataTransformation::ADD, (damage.hp - damage.damage_done)
    blockBuilder.put DataType::BYTE, damage.max_hp
  end

  def putMovementUpdate(seg, event, builder)
    update = seg.block_set.size > 0
    if seg.type == SegmentType::RUN
      directions = seg.directions
      builder.put_bits 1, 1
      builder.put_bits 2, 2
      builder.put_bits 3, directions[0].to_i
      builder.put_bits 3, directions[1].to_i
      builder.put_bit update
    elsif seg.type == SegmentType::WALK
      directions = seg.directions
      builder.put_bits 1, 1
      builder.put_bits 2, 1
      builder.put_bits 3, directions[0].to_i
      builder.put_bit update
    elsif update
      builder.put_bits 1, 1
      builder.put_bits 2, 0
    else
      builder.put_bits 1, 0
    end
  end

  def putRemoveCharacterUpdate(builder)
    builder.put_bits 1, 1
    builder.put_bits 2, 3
  end

  def putSecondHitUpdateBlock(block, blockBuilder)
    damage = block.damage 
    blockBuilder.put DataType::BYTE, DataTransformation::NEGATE, damage.damage_done
    blockBuilder.put DataType::BYTE, DataTransformation::SUBTRACT, damage.hit_type
    blockBuilder.put DataType::BYTE, DataTransformation::SUBTRACT, (damage.hp - damage.damage_done)
    blockBuilder.put DataType::BYTE, DataTransformation::NEGATE, damage.max_hp
  end

  def putTurnToPositionBlock(block, blockBuilder)
    pos = block.position 
    blockBuilder.put DataType::SHORT, DataOrder::LITTLE, (pos.x * 2 + 1)
    blockBuilder.put DataType::SHORT, DataOrder::LITTLE, (pos.y * 2 + 1)
  end

  def putTransformBlock(block, blockBuilder)
    blockBuilder.put DataType::SHORT, DataTransformation::ADD, block.id
  end

  def putInteractingEntityBlock(block, blockBuilder)
    blockBuilder.put DataType::SHORT, block.id
  end