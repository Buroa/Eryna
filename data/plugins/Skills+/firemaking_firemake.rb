require 'java'
java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.obj.DynamicGameObject'
java_import 'org.apollo.game.model.GroundItem'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.def.ObjectDefinition'
java_import 'org.apollo.game.scheduling.ScheduledTask'
java_import 'org.apollo.game.model.Item'

FIREMAKING_ANIMATION = Animation.new(733)

class FiremakingAction < Action
  attr_reader :item, :started, :ground_log

  def initialize(player, item)
    super 3, true, player
    @player = player
    @item = item
    @started = false
    @ground_log = GroundItem.new(player.name, Item.new(item.log), player.position)
  end

  def execute
    # get the levels
    skills = character.skill_set
    level = skills.get_skill(Skill::FIREMAKING).maximum_level
    spot = FIRES[character.position]

    # verify fire position
    if spot != nil
      if spot
        character.send_message "You cannot light a fire here!"
        stop
        return
      end
    end

    # verifys the firemaking requirements
    if item.level > level
      character.send_message "You don't have the correct Firemaking level to light this log!"
      character.send_message "You need a Firemaking level of at least #{item.level} to light this log."
      stop
      return
    end

    # start some player stuff
    if not started
      character.play_animation FIREMAKING_ANIMATION
      character.send_message "You attempt to light a fire..."
      character.inventory.remove item.log
      ground_log.set_listener DisablePickupGroundItem.new
      World.world.register ground_log
      @started = true
    else
      if rand(2) == 1
        # game object
        object = DynamicGameObject.new 2732, character.position, 10, 1
        World.world.unregister ground_log
        World.world.register object
        World.world.schedule ExpireFire.new(object, character.name)
        append_fire character.position

        #notify
        character.send_message "You light a fire."
        skills.add_experience Skill::FIREMAKING, item.xp

        # stop everything
        stop
      end
    end

  end

  def stop
    character.stop_animation
    super
  end

  def equals(other)
    return (get_class == other.get_class and @item == other.item)
  end

end

class ExpireFire < ScheduledTask

  attr_reader :object, :name

  def initialize(object, name)
    super 100, false
    @object = object
    @name = name
  end

  def execute
    FIRES[object.position] = false
    World.world.unregister object
    World.world.register GroundItem.new(name, Item.new(592), object.position)
    stop
  end

end

class DisablePickupGroundItem
  java_implements 'org.apollo.game.model.inter.GroundItemListener'

  def itemClicked(player, item)
    false
  end
end

# Finds the tinderbox
on :event, :item_on_item do |ctx, player, event|
  primary = event.id
  secondary = event.target_id

  if primary == 590
    item = F_LOGS[secondary]
    if item != nil
      player.start_action FiremakingAction.new(player, item)
      ctx.break_handler_chain
    end
  end
end