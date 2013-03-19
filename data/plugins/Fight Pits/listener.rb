# Holds the listener for usefull event handling.

require 'java'
java_import 'org.apollo.game.event.impl.ConfigEvent'

class FightPitsMinigameListener < MinigameListener
  attr_reader :minigame

  def initialize(minigame)
    @minigame = minigame
  end

  def character_added(team, character)

  end

  def character_removed(team, character)

  end

  def character_disconnected(player)

  end

end

class FightPitsCombatListener < CombatListener
  attr_reader :minigame

  def initialize(minigame)
    @minigame = minigame
  end

  def death_executed(source, victim)

  end
end