require 'java'
java_import 'org.apollo.game.event.impl.DamageEvent'
java_import 'org.apollo.game.event.impl.PlayerSynchronizationEvent'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Appearance'
java_import 'org.apollo.game.model.Direction'
java_import 'org.apollo.game.model.EquipmentConstants'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.Inventory'
java_import 'org.apollo.game.model.Item'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.def.EquipmentDefinition'
java_import 'org.apollo.game.model.inter.melee.MeleeConstants'
java_import 'org.apollo.game.sync.block.AnimationBlock'
java_import 'org.apollo.game.sync.block.AppearanceBlock'
java_import 'org.apollo.game.sync.block.ChatBlock'
java_import 'org.apollo.game.sync.block.ForceChatBlock'
java_import 'org.apollo.game.sync.block.ForceMovementBlock'
java_import 'org.apollo.game.sync.block.GraphicBlock'
java_import 'org.apollo.game.sync.block.HitUpdateBlock'
java_import 'org.apollo.game.sync.block.InteractingEntityBlock'
java_import 'org.apollo.game.sync.block.SecondHitUpdateBlock'
java_import 'org.apollo.game.sync.block.SynchronizationBlockSet'
java_import 'org.apollo.game.sync.block.TurnToPositionBlock'
java_import 'org.apollo.game.sync.seg.AddCharacterSegment'
java_import 'org.apollo.game.sync.seg.MovementSegment'
java_import 'org.apollo.game.sync.seg.SegmentType'
java_import 'org.apollo.game.sync.seg.SynchronizationSegment'
java_import 'org.apollo.game.sync.seg.TeleportSegment'
java_import 'org.apollo.net.codec.game.DataOrder'
java_import 'org.apollo.net.codec.game.DataTransformation'
java_import 'org.apollo.net.codec.game.DataType'
java_import 'org.apollo.net.codec.game.GamePacket'
java_import 'org.apollo.net.codec.game.GamePacketBuilder'
java_import 'org.apollo.net.meta.PacketType'
java_import 'org.apollo.net.release.EventEncoder'

on :encode, 317, :player_synchronization do |event|
    builder = GamePacketBuilder.new 81, PacketType::VARIABLE_SHORT
    builder.switch_to_bit_access 
    blockBuilder = GamePacketBuilder.new
    putMovementUpdate event.segment, event, builder
    putBlocks event.segment, blockBuilder
    builder.put_bits 8, event.local_players
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
      builder.put_bits 11, 2047
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
    builder.put_bits 11, seg.getIndex
    builder.put_bits 1, (updateRequired ? 1 : 0)
    builder.put_bits 1, 1
    builder.put_bits 5, other.y - player.y
    builder.put_bits 5, other.x - player.x
  end

  def putAnimationBlock(block, blockBuilder) 
    animation = block.animation 
    blockBuilder.put DataType::SHORT, DataOrder::LITTLE, animation.id
    blockBuilder.put DataType::BYTE, DataTransformation::NEGATE, animation.delay
  end


  ## Still need to convert this block.
  def putAppearanceBlock(block, blockBuilder)
    appearance = block.appearance 
    playerProperties = GamePacketBuilder.new
    playerProperties.put DataType::BYTE, appearance.gender.to_i
    playerProperties.put DataType::BYTE, appearance.skill
    equipment = block.equipment 
    style = appearance.style 
    item = nil
    chest = nil
    helm = nil
    weapon = nil
    weapon = (equipment.get(EquipmentConstants::WEAPON) != nil ? equipment.get(EquipmentConstants::WEAPON) : Item.new(0))
    if appearance.npc_id != -1 
      playerProperties.put DataType::SHORT, -1
      playerProperties.put DataType::SHORT, appearance.npc_id
    else
      for slot in 0..3
        if (item = equipment.get(slot)) != nil
          playerProperties.put DataType::SHORT, 0x200 + item.id
        else
          playerProperties.put DataType::BYTE, 0
        end
      end
      if (chest = equipment.get(EquipmentConstants::CHEST)) != nil
        playerProperties.put DataType::SHORT, (0x200 + chest.id)
      else
        playerProperties.put DataType::SHORT, (0x100 + style[2])
      end
      if (item = equipment.get(EquipmentConstants::SHIELD)) != nil
        playerProperties.put DataType::SHORT, (0x200 + item.id)
      else
        playerProperties.put DataType::BYTE, 0
      end
      if chest != nil 
        defi = EquipmentDefinition.forId chest.id
        if defi != nil && !defi.full_body
          playerProperties.put DataType::SHORT, (0x100 + style[3])
        else
          playerProperties.put DataType::BYTE, 0
        end
      else
        playerProperties.put DataType::SHORT, (0x100 + style[3])
      end
      if (item = equipment.get(EquipmentConstants::LEGS)) != nil
        playerProperties.put DataType::SHORT, (0x200 + item.id)
      else
        playerProperties.put DataType::SHORT, (0x100 + style[5])
      end
      if (helm = equipment.get(EquipmentConstants::HAT)) != nil 
        defi = EquipmentDefinition.forId helm.id
        if defi != nil && !defi.full_hat && !defi.full_mask
          playerProperties.put DataType::SHORT, (0x100 + style[0])
        else
          playerProperties.put DataType::BYTE, 0
        end
      else
        playerProperties.put DataType::SHORT, (0x100 + style[0])
      end
      if (item = equipment.get(EquipmentConstants::HANDS)) != nil
        playerProperties.put DataType::SHORT, (0x200 + item.id)
      else
        playerProperties.put DataType::SHORT, (0x100 + style[4])
      end
      if (item = equipment.get(EquipmentConstants::FEET)) != nil
        playerProperties.put DataType::SHORT, (0x200 + item.id)
      else
        playerProperties.put DataType::SHORT, (0x100 + style[6])
      end
      defi = nil
      if helm != nil
        defi = EquipmentDefinition.forId helm.id
      end
      if defi != nil && defi.full_hat || defi.full_mask || appearance.female
        playerProperties.put DataType::BYTE, 0
      else
        playerProperties.put DataType::SHORT, (0x100 + style[1])
      end
    end
    appearance.colors.each do |color|
      playerProperties.put DataType::BYTE, color
    end
    weaponId = weapon.id 
    playerProperties.put DataType::SHORT, MeleeConstants.getStandAnimation(appearance, weaponId) # stand
    playerProperties.put DataType::SHORT, 0x337 # stand turn
    playerProperties.put DataType::SHORT, MeleeConstants.getWalkAnimation(appearance, weaponId) # walk
    playerProperties.put DataType::SHORT, 0x334 # turn 180
    playerProperties.put DataType::SHORT, 0x335 # turn 90 cw
    playerProperties.put DataType::SHORT, 0x336 # turn 90 ccw
    playerProperties.put DataType::SHORT, MeleeConstants.getRunAnimation(appearance, weaponId) # run
    playerProperties.put DataType::LONG, DataTransformation::QUADRUPLE, block.name
    playerProperties.put DataType::BYTE, block.combat_level
    playerProperties.put DataType::SHORT, block.skill_level
    blockBuilder.put DataType::BYTE, DataTransformation::NEGATE, playerProperties.length
    blockBuilder.putRawBuilder playerProperties
  end

  def putBlocks(segment, blockBuilder)
    blockSet = segment.getBlockSet 
    if blockSet.size > 0 
      mask = 0;
      if blockSet.contains ForceMovementBlock.class
        mask |= 0x400
      end
      if blockSet.contains GraphicBlock.class
        mask |= 0x100
      end
      if blockSet.contains AnimationBlock.class
        mask |= 8
      end
      if blockSet.contains ForceChatBlock.class
        mask |= 4;
      end
      if blockSet.contains ChatBlock.class
        mask |= 0x80
      end
      if blockSet.contains InteractingEntityBlock.class
        mask |= 1
      end
      if blockSet.contains AppearanceBlock.class
        mask |= 0x10
      end
      if blockSet.contains TurnToPositionBlock.class
        mask |= 2
      end
      if blockSet.contains HitUpdateBlock.class
        mask |= 0x20
      end
      if blockSet.contains SecondHitUpdateBlock.class
        mask |= 0x200
      end
      if mask >= 0x100 
        mask |= 0x40
        blockBuilder.put DataType::SHORT, DataOrder::LITTLE, mask
      else
        blockBuilder.put DataType::BYTE, mask
      end
      if blockSet.contains ForceMovementBlock.class
        putForceMovementBlock blockSet.get(ForceMovementBlock.class), blockBuilder
      end
      if blockSet.contains GraphicBlock.class
        putGraphicBlock blockSet.get(GraphicBlock.class), blockBuilder
      end
      if blockSet.contains AnimationBlock.class
        putAnimationBlock blockSet.get(AnimationBlock.class), blockBuilder
      end
      if blockSet.contains ForceChatBlock.class
        putForceChatBlock blockSet.get(ForceChatBlock.class), blockBuilder
      end
      if blockSet.contains ChatBlock.class
        putChatBlock blockSet.get(ChatBlock.class), blockBuilder
      end
      if blockSet.contains InteractingEntityBlock.class
        putInteractingEntityBlock blockSet.get(InteractingEntityBlock.class), blockBuilder
      end
      if blockSet.contains AppearanceBlock.class
        putAppearanceBlock blockSet.get(AppearanceBlock.class), blockBuilder
      end
      if blockSet.contains TurnToPositionBlock.class
        putTurnToPositionBlock blockSet.get(TurnToPositionBlock.class), blockBuilder
      end
      if blockSet.contains HitUpdateBlock.class
        putHitUpdateBlock blockSet.get(HitUpdateBlock.class), blockBuilder
      end
      if blockSet.contains SecondHitUpdateBlock.class
        putSecondHitUpdateBlock blockSet.get(SecondHitUpdateBlock.class), blockBuilder
      end
    end
  end

  def putChatBlock(block, blockBuilder)
    bytes = block.compressed_message 
    blockBuilder.put DataType::SHORT, DataOrder::LITTLE, (block.text_color << 8 | block.text_effects)
    blockBuilder.put DataType::BYTE, block.privilege_level.to_i
    blockBuilder.put DataType::BYTE, DataTransformation::NEGATE, bytes.length
    blockBuilder.put_bytes_reverse bytes
  end

  def putForceChatBlock(block, blockBuilder)
    blockBuilder.put_string block.text
  end

  def putForceMovementBlock(block, blockBuilder)
    current = block.current_position
    reachable = block.position
    blockBuilder.put DataType::BYTE, DataTransformation::SUBTRACT, current.local_x
    blockBuilder.put DataType::BYTE, DataTransformation::SUBTRACT, current.local_y
    blockBuilder.put DataType::BYTE, DataTransformation::SUBTRACT, reachable.local_x(current)
    blockBuilder.put DataType::BYTE, DataTransformation::SUBTRACT, reachable.local_y(current)
    blockBuilder.put DataType::SHORT, DataOrder::LITTLE, DataTransformation::ADD, block.first_speed
    blockBuilder.put DataType::SHORT, DataTransformation::ADD, block.second_speed
    blockBuilder.put DataType::BYTE, DataTransformation::SUBTRACT, block.direction
  end

  def putGraphicBlock(block, blockBuilder)
    graphic = block.graphic 
    blockBuilder.put DataType::SHORT, DataOrder::LITTLE, graphic.id
    blockBuilder.put DataType::INT, (graphic.height << 16 & 0xFFFF0000 | graphic.delay & 0x0000FFFF)
  end

  def putHitUpdateBlock(block, blockBuilder)
    damage = block.damage 
    blockBuilder.put DataType::BYTE, damage.damage_done
    blockBuilder.put DataType::BYTE, DataTransformation::ADD, damage.hit_type
    blockBuilder.put DataType::BYTE, DataTransformation::NEGATE, (damage.hp - damage.damage_done)
    blockBuilder.put DataType::BYTE, damage.max_hp
  end

  def putInteractingEntityBlock(block, blockBuilder)
    blockBuilder.put DataType::SHORT, DataOrder::LITTLE, block.id
  end

  def putMovementUpdate(seg, event, builder)
    update = seg.block_set.size > 0
    if seg.type == SegmentType::TELEPORT
      pos = seg.destination
      builder.put_bits 1, 1
      builder.put_bits 2, 3
      builder.put_bits 2, pos.height
      builder.put_bit event.has_region_changed
      builder.put_bit update
      builder.put_bits 7, pos.local_y(event.last_known_region)
      builder.put_bits 7, pos.local_x(event.last_known_region)
    elsif seg.type == SegmentType::RUN
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
      builder.put_bits update
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
    blockBuilder.put DataType::BYTE, damage.damage_done
    blockBuilder.put DataType::BYTE, DataTransformation::SUBTRACT, damage.hit_type
    blockBuilder.put DataType::BYTE, DataTransformation::NEGATE, (damage.hp - damage.damage_done)
    blockBuilder.put DataType::BYTE, damage.max_hp
  end

  def putTurnToPositionBlock(block, blockBuilder)
    pos = block.position 
    blockBuilder.put DataType::SHORT, DataOrder::LITTLE, DataTransformation::ADD, (pos.x * 2 + 1)
    blockBuilder.put DataType::SHORT, DataOrder::LITTLE, (pos.y * 2 + 1)
  end