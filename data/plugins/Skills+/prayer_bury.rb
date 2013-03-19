require 'java'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.action.Action'

BURY_ANIMATION = Animation.new(827)

class BuryingAction < Action

  attr_reader :bone
  
  def initialize(player, bone)
    super 0, true, player
    @bone = bone
  end

  def execute
    character.inventory.remove bone.item, 1
    character.skill_set.add_experience Skill::PRAYER, bone.exp
    character.send_message "You bury the bones."
    character.play_animation BURY_ANIMATION
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @bone == other.bone)
  end

end

on :event, :item_option do |ctx, player, event|
  if event.option == 1
    bone = BONES[event.id]
    if bone != nil
      player.start_action BuryingAction.new(player, bone)
    end
  end
end
