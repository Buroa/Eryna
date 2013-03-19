require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.obj.GameObject'
java_import 'org.apollo.game.model.def.ObjectDefinition'
java_import 'org.apollo.game.scheduling.ScheduledTask'

FLAX_ANIMATION = Animation.new(827)

class FlaxAction < DistancedAction
  
  attr_reader :position, :started, :id

  def initialize(character, position, id)
    super 1, true, character, position, 1
    @position = position
    @started = false
    @id = id
  end

  def executeAction
    if not started
      character.turn_to position
      if character.inventory.add 1779
        character.play_animation FLAX_ANIMATION
        character.send_message "You pick some flax."
        if rand(4) == 1
          World.world.replace_object position, GameObject.new(ObjectDefinition.for_id(3), position, 10, 1)
          World.world.schedule ExpireFlax.new(id, position)
        end
      end
      @started = true
    else
      stop
    end
  end

  def equals(other)
    return (get_class == other.get_class)
  end

end

class ExpireFlax < ScheduledTask

  attr_reader :id, :position

  def initialize(id, position)
    super 6+rand(4), false
    @id = id
    @position = position
  end

  def execute
    World.world.replace_object position, GameObject.new(ObjectDefinition.for_id(id), position, 10, 1)
    stop
  end

end

on :event, :object_action do |ctx, player, event|
  if event.option == 2
    if event.id == 2646
      player.startAction FlaxAction.new(player, event.position, event.id)
      ctx.break_handler_chain
    end
  end
end