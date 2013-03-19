require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.event.impl.OpenInterfaceEvent'
java_import 'org.apollo.game.event.impl.CloseInterfaceEvent'
java_import 'org.apollo.game.event.impl.ConfigEvent'
java_import 'org.apollo.game.event.impl.UpdateItemsEvent'
java_import 'org.apollo.game.model.Item'

ANVIL_ANIMATION = Animation.new(898)

class AnvilAction < DistancedAction
  attr_reader :interface_id, :item_id, :slot, :count, :current_count
  
  def initialize(player, position, interface_id, item_id, slot, count)
    super 3, true, player, position, 1
    @position = position
    @interface_id = interface_id
    @item_id = item_id
    @slot = slot
    @count = count
    @current_count = 0
  end

  def executeAction
    skill = character.skill_set
    
    if @current_count == @count
      stop
      return
    end
    
    BARS.each do |bar_info|
      if @interface_id == bar_info.item_group && @item_id == bar_info.item_ids[@slot]
        if not (skill.get_skill(Skill::SMITHING).maximum_level >= bar_info.level_requirements[@slot])
          character.send_message "You need a smithing level of #{bar_info.level_requirements[@slot]} to make this item."
          stop
          return
        end
        
        if not (character.inventory.get_item_count(bar_info.bar_id) >= bar_info.bar_amounts[@slot])
          character.send_message "You do not have enough bars to make this item."
          stop
          return
        end
        character.send CloseInterfaceEvent.new()
        character.play_animation ANVIL_ANIMATION
        character.inventory.remove bar_info.bar_id, bar_info.bar_amounts[@slot]
        item_def = ItemDefinition.for_id bar_info.item_ids[@slot]
        if item_def.name.include?("arrow") || item_def.name.include?("nails")
          character.inventory.add bar_info.item_ids[@slot], 15
        elsif item_def.name.include?("dart")
          character.inventory.add bar_info.item_ids[@slot], 10
        elsif item_def.name.include?("knife")
          character.inventory.add bar_info.item_ids[@slot], 5
        else
          character.inventory.add bar_info.item_ids[@slot], 1
        end
        skill.add_experience Skill::SMITHING, (bar_info.base_xp * bar_info.bar_amounts[@slot])
        @current_count+=1
      end
    end
  end
end

on :command, :smith do |player, command|
  args = command.arguments
  id = args[0].to_i
  reset_smith(id, player)
end

on :event, :item_action do |ctx, player, event|
  if event.option == 1
    player.start_action AnvilAction.new(player, player.position, event.interfaceId, event.id, event.slot, 1)
  end
  if event.option == 2
    player.start_action AnvilAction.new(player, player.position, event.interfaceId, event.id, event.slot, 5)
  end
  if event.option == 3
    player.start_action AnvilAction.new(player, player.position, event.interfaceId, event.id, event.slot, 10)
  end
end

on :event, :item_used_on_object do |ctx, player, event|
  if event.object == 2783
    if player.inventory.contains 2347
      reset_smith(event.id, player)
    else
      player.send_message "You need a hammer to make anything."
    end
  end
end

def reset_smith(bar_id, player)
  smith_items = []
  BARS.each do |bar_info|
    if bar_info.bar_id == bar_id
      bar_info.item_ids.each do |item_id|
        smith_items.push(Item.new(item_id, 1))
      end
      player.send ConfigEvent.new(210, player.inventory.get_item_count(bar_id))
      player.send UpdateItemsEvent.new(bar_info.item_group, smith_items)
      smith_items.clear
    end
  end
  player.send OpenInterfaceEvent.new(994)
end