require 'java'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.action.Action'

class GemAction < Action

  attr_reader :gem

  def initialize(player, gem)
    super 0, true, player
    @gem = gem
  end

  def execute
    skills = character.skill_set
    level = skills.get_skill(Skill::CRAFTING).maximum_level
    
    # verifys the crafting requirements
    if gem.level > level
      character.send_message "You don't have the correct Crafting level to cut this!"
      character.send_message "You need a Crafting level of at least #{gem.level} to cut this."
      stop
      return
    end

    # start crafting fella's
    character.play_animation Animation.new(gem.animation)
    character.inventory.remove gem.uncut
    if gem.crackable and rand(20) == 1
      character.inventory.add 1633, 1
      character.send_message "You accidentaly crush the gem!"
    else
      character.inventory.add gem.cut
      skills.add_experience Skill::CRAFTING, gem.exp
    end

    stop
  end

  def equals(other)
    return (get_class == other.get_class and @gem == other.gem)
  end

end

class AmuletAction < Action

  attr_reader :amulet

  def initialize(player, amulet)
    super 0, true, player
    @amulet = amulet
  end

  def execute
    skills = character.skill_set
    level = skills.get_skill(Skill::CRAFTING).maximum_level
    
    # verifys the crafting requirements
    if amulet.level > level
      character.send_message "You don't have the correct Crafting level to string this!"
      character.send_message "You need a Crafting level of at least #{amulet.level} to string this."
      stop
      return
    end

    # start crafting fella's
    character.inventory.remove amulet.amulet
    character.inventory.remove 1759, 1
    character.inventory.add amulet.item
    character.send_message "You attach the wool to the amulet."
    skills.add_experience Skill::CRAFTING, amulet.exp

    stop
  end

  def equals(other)
    return (get_class == other.get_class and @amulet == other.amulet)
  end

end

class StaveAction < Action

  attr_reader :stave

  def initialize(player, stave)
    super 0, true, player
    @stave = stave
  end

  def execute
    skills = character.skill_set
    level = skills.get_skill(Skill::CRAFTING).maximum_level
    
    # verifys the crafting requirements
    if stave.level > level
      character.send_message "You don't have the correct Crafting level to create this!"
      character.send_message "You need a Crafting level of at least #{stave.level} to create this."
      stop
      return
    end

    # start crafting fella's
    character.inventory.remove stave.orb, 1
    character.inventory.remove 1391, 1
    character.inventory.add stave.item
    character.send_message "You put the staff together."
    skills.add_experience Skill::CRAFTING, stave.exp

    stop
  end

  def equals(other)
    return (get_class == other.get_class and @stave == other.stave)
  end

end

on :event, :item_on_item do |ctx, player, event|
  primary = event.id
  secondary = event.target_id

  if primary == 1755
    gem = C_GEMS[secondary]
    if gem != nil
      player.start_action GemAction.new(player, gem)
    end
  end
end

on :event, :item_on_item do |ctx, player, event|
  primary = event.id
  secondary = event.target_id

  if primary == 1759
    amulet = AMULETS[secondary]
    if amulet != nil
      player.start_action AmuletAction.new(player, amulet)
    end
  end
end

on :event, :item_on_item do |ctx, player, event|
  primary = event.id
  secondary = event.target_id

  stave = STAVES[primary]
  if stave != nil
    if secondary == 1391
      player.start_action StaveAction.new(player, stave)
      ctx.break_handler_chain
    end
  end
end