# Actions called upon a object interaction

require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.event.impl.ConfigEvent'

class FightPitsDistancedAction < DistancedAction

 attr_reader :minigame, :position, :object

  def initialize(character, minigame, position, object)
    super 0, true, character, position, 3
    @minigame = minigame
    @position = position
    @object = object
  end

  def executeAction
    team = minigame.get_team character
    if object == ENTER_GAME_LOBBY
      if team == -1 # no team
        minigame.add PITS_LOBBY_TEAM, character
        character.teleport PITS_LOBBY_ENTER_POSITION
        character.send SetInterfaceTextEvent.new(2805, "Current Champion: #{minigame.champion}")
        character.send ConfigEvent.new(560, REMOVE_FOES_REMAINING)
        character.interface_set.open_walkable PITS_INTERFACE_ID
      elsif team == PITS_LOBBY_TEAM
        minigame.remove team, character
        character.teleport PITS_LOBBY_LEAVE_POSITION
        character.interface_set.open_walkable -1 # Clears the interface
      end
    elsif object == LEAVE_GAME_START
      if team == PITS_GAME_TEAM
        minigame.remove team, character
        minigame.add PITS_LOBBY_TEAM, character
        character.teleport PITS_GAME_LEAVE_POSITION
      end
    end
    stop
  end

  def equals(other)
    return (get_class == other.get_class and other.position == @position and other.object == @object)
  end

end