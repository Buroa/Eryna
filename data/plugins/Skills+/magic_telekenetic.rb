require 'java'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.event.impl.GraphicEvent'

GROUND_SPELLS = {}

TELEK_ANIM = Animation.new(711)
TELEK_GFX = Graphic.new(142, 0, 100)
TELEK_GFX_GROUND = Graphic.new(144, 0, 100)

class GroundSpell < Spell
  attr_reader :animation, :graphic, :experience
  
  def initialize(level, elements, animation, graphic, experience)
    super level, elements, experience
	
	@animation = animation
	@graphic = graphic
  end
end

class TelekeneticAction < SpellAction
  attr_reader :position, :item, :region

  def initialize(player, spell, position, item)
    super player, spell
    @position = position
    @item = item
    @region = player.get_region
  end
  
  def execute_action
    player = character
    reward = World.getWorld.getRegionManager.getGroundItem player, @position, @item
    graphicEvent = GraphicEvent.new(TELEK_GFX_GROUND, @position)
    if reward != nil
          if @pulses == 0
	    player.play_animation @spell.animation
	    player.play_graphic @spell.graphic
	  
	    inventory = player.inventory
            World.getWorld.unregister reward
            @region.add_event graphicEvent
	    inventory.add reward.get_item
	  
	    player.skill_set.add_experience MAGIC_ID, @spell.experience
	  elsif @pulses == 3
            @region.remove_event graphicEvent
	    player.stop_animation
	    player.stop_graphic
	    stop
	  end
    else
      stop
    end
  end
end

def append_ground(spell, level, elements, animation, graphic, experience)
  ground = GroundSpell.new(level, elements, animation, graphic, experience)
  GROUND_SPELLS[spell] = ground
end

append_ground 1168, 33, { AIR => 1, LAW => 1 }, TELEK_ANIM, TELEK_GFX, 31 # Telekenetic