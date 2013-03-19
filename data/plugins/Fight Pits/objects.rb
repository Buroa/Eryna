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

  end

  def equals(other)
    return (get_class == other.get_class and other.position == @position and other.object == @object)
  end

end