require 'java'
java_import 'org.apollo.game.event.impl.ConfigEvent'

COMBAT_SETS = {}

class CombatSet
  attr_reader :attacking, :embattled, :defunct, :style, :retaliate, :respawn, :timer, :special_amount, :using_special, :listeners

  def initialize
    @attacking = false
    @embattled = false
    @defunct = false
    @style = 1
    @retaliate = true
    @respawn = true
    @timer = 2
    @special_amount = 100
    @using_special = false
    @listeners = []
  end

  def set_attacking(attacking)
    @attacking = attacking
  end

  def set_embattled(embattled)
    @embattled = embattled
  end

  def set_defunct(defunct)
    @defunct = defunct
  end

  def set_timer(timer)
    @timer = timer
  end

  def set_style(style)
    @style = style
  end

  def set_retaliate(retaliate)
    @retaliate = retaliate
  end

  def set_respawn(respawn)
    @respawn = respawn
  end

  def set_special_amount(special_amount)
    @special_amount = special_amount
  end

  def set_using_special(using_special)
    @using_special = using_special
  end

  def add_listener(listener)
    if not @listeners.include? listener
      @listeners.push listener
    end
  end

  def remove_listener(listener)
    if @listeners.include? listener
      @listeners.delete listener
    end
  end

end

def get_combat_set(character)
  combat_set = COMBAT_SETS[character]
  if combat_set == nil
    combat_set = CombatSet.new
    COMBAT_SETS[character] = combat_set
  end
  return combat_set
end

on :logout do |player|
  COMBAT_SETS[player] = nil
end