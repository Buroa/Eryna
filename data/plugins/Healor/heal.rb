require 'java'
java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.Animation'

EAT_ANIMATION = Animation.new(0x33D)

class Eat < Action

 attr_reader :player, :item

  def initialize(player, item)
    super 0, true, player
    @item = item
  end
  
  def execute
    player = character
    player.inventory.remove @item.item, 1
    item_def = ItemDefinition.for_id @item.item
    name = item_def.name.sub(/ item$/, "").downcase
    player.play_animation EAT_ANIMATION
    player.send_message "You eat the #{name}."
    heal
    stop
  end

  def heal
    character.add_health @item.points
  end

  def equals(other)
    return (get_class == other.get_class)
  end

end

on :event, :item_option do |ctx, player, event|
  if event.option == 1
    food = FOODS[event.get_id]
    if food != nil
      player.start_action Eat.new(player, food)
      ctx.break_handler_chain
    end
  end
end