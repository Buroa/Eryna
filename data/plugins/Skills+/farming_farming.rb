require 'java'
java_import 'org.apollo.game.model.skill.farming.Farming'

on :event, :object_action do |ctx, player, event|
  pos = event.position
  if event.option == 1
    result = false
    if player.settings.is_farming
      result = player.settings.farming_set.compost.handle_object_click event.get_id, pos.x, pos.y
    end
    if not result
      if Farming.harvest player, pos.x, pos.y
        ctx.break_handler_chain
      end
    else
      ctx.break_handler_chain
    end
  elsif event.get_option == 2
    if Farming.inspect_object player, pos.x, pos.y
      ctx.break_handler_chain
    end
  end
end

on :event, :item_used_on_object do |ctx, player, event|
  pos = event.position
  result = Farming.prepare_crop player, event.id, event.object, pos.x, pos.y
  if result
    ctx.break_handler_chain
  end
end

on :event, :item_on_item do |ctx, player, event|
  primary = event.id
  secondary = event.target_id
  primary_slot = event.get_slot
  secondary_slot = event.target_slot

  if player.settings.is_farming
    result = player.settings.farming_set.seedling.water_seedling primary, secondary, primary_slot, secondary_slot
    if result
      ctx.break_handler_chain
    else
      result = player.settings.farming_set.seedling.place_seed_in_pot primary, secondary, primary_slot, secondary_slot
      if result
        ctx.break_handler_chain
      end
    end
  end
end
