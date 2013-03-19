require 'java'
java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.event.impl.DisplayTabInterfaceEvent'
java_import 'org.apollo.game.model.EquipmentConstants'
java_import 'org.apollo.game.model.Skill'

MAGIC_ID = Skill::MAGIC
DISPLAY_SPELLBOOK = DisplayTabInterfaceEvent.new 6
DEFAULT_SPELLBOOK = 1511
ANCIENT_SPELLBOOK = 12855

class Spell
  attr_reader :level, :elements, :experience
  
  def initialize(level, elements, experience)
    @level = level
	@elements = elements
	@experience = experience
  end
end

class SpellAction < Action
  attr_reader :spell, :pulses
  
  def initialize(character, spell)
    super 0, true, character
	
	@spell = spell
	@pulses = 0
  end
  
  def execute
  # Comment out this if-statement to disable level & element checking
    if character.is_controlling
      if @pulses == 0
        if not (check_skill and process_elements)
          stop
  	      return
        end
      end
    end
	execute_action
	@pulses += 1
  end
  
  def execute_action
    # Override to perform actions.
	stop
  end
  
  def check_skill
    required = @spell.level
    if required > character.skill_set.skill(MAGIC_ID).maximum_level
	  character.send_message "You need a Magic level of at least " + required.to_s + " to cast this spell."
	  return false
	end
	
	return true
  end
  
  def process_elements
	elements = @spell.elements
	
	elements.each do |element, amount|
	  if !(element.check_remove character, amount, false)
	    character.send_message "You do not have enough " + element.name + "s to cast this spell."
		return false
	  end
	end
	
	elements.each do |element, amount|
	  element.check_remove character, amount, true
	end
	
	return true
  end

  def equals(other)
    return (get_class == other.get_class and @spell == other.spell)
  end
end

class MagicDistancedAction < CombatDistancedAction
  attr_reader :spell, :spell_action

  def initialize(source, victim, spell)
    super source, victim, 6, 8
    @spell = spell
    @spell_action = SpellAction.new source, spell
  end

  def executeCombat
    # checks the players skill and elements
    if not spell_action.check_skill or not spell_action.process_elements
      stop
      return
    end

    # initiate some constants every time
    region = character.region
    projectile = create_magic_projectile character, victim, spell

    # calculate max damage and randomize it
    max_damage = get_magic_max_hit character, victim
    random_damage = rand (1+max_damage).to_i

    if victim.health > 0
      character.skill_set.add_experience MAGIC_ID, spell.experience
      character.play_animation spell.animation
      if spell.graphics[0].id != 65535
        character.play_graphic spell.graphics[0]
      end

      if spell.projectile != 0
        World.world.region_manager.get_chunk_by_position(character.position).add projectile, 1
      end

      # damage the victim
      schedule 2 do |task|
        if rand(5) == 1
          victim.play_graphic Graphic.new(339, 0, 100)
        else
          if spell.graphics.size > 1 and damage_victim(random_damage)
            character.skill_set.add_experience MAGIC_ID, (spell.experience * random_damage)
            victim.play_graphic spell.graphics[1]
            create_magic_special character, victim, spell, random_damage
          end
        end
        task.stop
      end
    end

    # auto casting
    # should happen automatically
  end
end

class ItemSpellAction < SpellAction
  attr_reader :slot, :item

  def initialize(character, spell, slot, item)
    super character, spell
	
	@slot = slot
	@item = item
  end
  
  # We override SpellAction#execute to implement illegal item check (e.g. coins for alchemy)
  def execute
    if @pulses == 0
	  if illegal_item?
	    character.send_message "You cannot use that spell on this item!"
	    stop
		return
	  end
	  
	  id = @item.id
	  
	  # TODO: There has to be a better way to do this.
	  @spell.elements.each do |element, amount|
	    catch :valid do
	      element.runes.each do |rune|
		    if id == rune
		      if element.check_remove character, amount + 1, false
				throw :valid
			  else
			    character.send_message "You do not have enough " + element.name + "s to cast this spell."
				stop
				return
			  end
			end
		  end
		end
	  end

	end
	
	super
  end

  def illegal_item?
    # We override this method if necessary.
	return false
  end
end

on :event, :magic_on_item do |ctx, player, event|
  if $debug
    player.send_message "Spell: " + event.spell_id.to_s
  end
end

# MagicOnItemEvent handling
on :event, :magic_on_item do |ctx, player, event|
  spell = event.spell_id
  
  alch = ALCHEMY_SPELLS[spell]
  if alch != nil
    slot = event.slot
	item = player.inventory.get slot
	player.start_action AlchemyAction.new(player, alch, slot, item)
	ctx.break_handler_chain
	return
  end
  
  ench = ENCHANT_SPELLS[event.id]
  if ench != nil and ench.button == spell
    slot = event.slot
	item = player.inventory.get slot
	player.start_action EnchantAction.new(player, ench, slot, item, ENCHANT_ITEMS[item.id])
	ctx.break_handler_chain
	return
  end
end

on :event, :magic_on_ground do |ctx, player, event|
  player.send_message "Spell: " + event.magic_id.to_s
end

# MagicOnGroundEvent handling
on :event, :magic_on_ground do |ctx, player, event|
  spell = event.magic_id
  
  ground = GROUND_SPELLS[spell]
  if ground != nil
    item = event.get_item_id
    position = event.get_position
	player.start_action TelekeneticAction.new(player, ground, position, item)
	ctx.break_handler_chain
	return
  end
end

on :event, :magic do |ctx, player, event|
  if $debug
    player.send_message "Spell: " + event.get_spell_id.to_s
  end
end

# Magic handling
on :event, :magic do |ctx, player, event|
  spell = COMBAT_SPELLS[event.spell_id]

  if spell != nil
    player.walking_queue.clear # this is a must
    player.start_action MagicDistancedAction.new(player, event.victim, spell)
    ctx.break_handler_chain
  end
end

on :event, :magic_on_object do |ctx, player, event|
  if $debug
    player.send_message "Spell: " + event.magic_id.to_s
  end
end

# ButtonEvent handling
on :event, :button do |ctx, player, event|
  button = event.interface_id

  tele = TELEPORT_SPELLS[button]
  if tele != nil
    player.start_action TeleportingAction.new(player, tele)
	ctx.break_handler_chain
	return
  end
  
  conv = CONVERT_SPELLS[button]
  if conv != nil
    slots = bone_slots player
    if slots.length != 0
      player.start_action ConvertingAction.new(player, conv, slots)
    else
      player.send_message "You do not have any usable bones in your inventory!"
    end
	ctx.break_handler_chain
	return
  end
end
