require 'java'
java_import 'org.apollo.game.event.impl.SetInterfaceComponentEvent'

SPECIAL_BARS = [12323, 7574, 7599, 7549, 8493, 7499]
SPECIAL_BUTTONS = [12311]
SPECIALS = {}

class CombatSpecial
  attr_reader :drain, :base, :block

  def initialize(drain, base, block)
    @drain = drain
    @base = base
    @block = block
  end

  def execute(source, victim)
    special_amount = get_combat_set(source).special_amount
    if special_amount >= drain
      special_schedule source
      get_combat_set(source).set_special_amount special_amount-drain
      @block.call source, victim
    else
      SPECIALS[0].execute source, victim
    end

    special_amount = get_combat_set(source).special_amount
    if get_combat_set(source).using_special
      source.send ConfigEvent.new(301, 0)
      source.send ConfigEvent.new(300, special_amount * 10)
      get_combat_set(source).set_using_special false
    end
  end
end

def append_special(weapon, clazz)
  SPECIALS[weapon] = clazz
end

append_special 1305, CombatSpecial.new(25, 1.2, proc do |source, victim|
  source.play_animation Animation.new(1058)
  source.play_graphic Graphic.new(248, 0, 100)
  victim.play_graphic Graphic.new(254, 0, 100)
end)

append_special 1303, CombatSpecial.new(30, 1.2, proc do |source, victim|
  source.play_animation Animation.new(1203)
  source.play_graphic Graphic.new(285, 0, 100)
end)

append_special 4151, CombatSpecial.new(50, 1.2, proc do |source, victim|
  source.play_animation Animation.new(1658)
  victim.play_graphic Graphic.new(341, 0, 100)
end)

append_special 0, CombatSpecial.new(0, 0, proc do |source, victim|
  source_animation = get_combat_hit_animation source
  source.play_animation source_animation

  if victim.is_controlling
    victim_has_shield = victim.equipment.get(EquipmentConstants::SHIELD) != nil
    victim.play_animation victim_has_shield ? SHIELD_BLOCK_ANIMATION : FOREARM_BLOCK_ANIMATION
  else
    npc_block_anim = NPC_BLOCK_ANIMATIONS[victim.id]
    victim.play_animation npc_block_anim != nil ? npc_block_anim : FOREARM_BLOCK_ANIMATION
  end
end)

# sends the special bars
# todo correct way?
on :login do |player|
  SPECIAL_BARS.each do |special|
    player.send SetInterfaceComponentEvent.new(special, true)
  end

  # max the special
  player.send ConfigEvent.new(300, 1000)
end

# actives the special
on :event, :button do |ctx, player, event|
  SPECIAL_BUTTONS.each do |button|
    if button == event.interface_id
      if not get_combat_set(player).using_special
        if get_combat_set(player).special_amount != 0
          player.send ConfigEvent.new(301, 1)
          get_combat_set(player).set_using_special true
        end
      else
        player.send ConfigEvent.new(301, 0)
        get_combat_set(player).set_using_special false
      end
      ctx.break_handler_chain
    end
  end
end