require 'java'
java_import 'org.apollo.game.action.DistancedAction'

class AgilityAction < DistancedAction

  attr_reader :position, :object

  def initialize(player, position, object)
    super 0, true, player, position, 1
    @position = position
    @object = object
  end

  def executeAction
    object.execute character
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @object == other.object)
  end
end

on :event, :object_action do |ctx, player, event|
  object = AGILITYOBJECTS[event.id]
  if object != nil
    player.start_action AgilityAction.new(player, event.position, object)
  end
end