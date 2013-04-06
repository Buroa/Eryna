# Holds the listener for usefull event handling.

require 'java'
java_import 'org.apollo.game.event.impl.ConfigEvent'

class FightPitsMinigameListener < MinigameListener
  attr_reader :minigame

  def initialize(minigame)
    @minigame = minigame
  end

  def character_added(team, character)
    if team == PITS_LOBBY_TEAM
      size = minigame.get_characters(PITS_LOBBY_TEAM).size
      if size > PITS_MIN_NEEDED and minigame.cooldown <= 0
        minigame.set_attribute PITS_START_GAME, true
      end
    end
  end

  def character_removed(team, character)
    if team == PITS_GAME_TEAM
      minigame.set_attribute PITS_PLAYERS_COUNT_UPDATE, true
    end
    character.settings.set_attackable false
  end

  def character_disconnected(character)
    team = minigame.get_team character
    if team != -1
      minigame.remove team, character
      character.teleport PITS_LOBBY_LEAVE_POSITION
      minigame.set_attribute PITS_PLAYERS_COUNT_UPDATE, true
    end
  end

end

class FightPitsCombatListener < CombatListener
  attr_reader :minigame

  def initialize(minigame)
    @minigame = minigame
  end

  def death_executed(source, victim)
    team = minigame.get_team source
    minigame.remove team, source
    minigame.add PITS_LOBBY_TEAM, source
    source.teleport PITS_GAME_LEAVE_POSITION
    get_combat_set(source).remove_listener self
    source.stop_animation
  end

  def custom_death
    true
  end
end