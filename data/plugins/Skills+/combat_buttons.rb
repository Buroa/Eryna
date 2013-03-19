COMBAT_STYLES = {}

def append_combat_style(button, style)
  COMBAT_STYLES[button] = style
end

on :event, :button do |ctx, player, event|
  style = COMBAT_STYLES[event.interface_id]
  if style != nil
      get_combat_set(player).set_style style
  end
end

on :button, 150 do |player|
  get_combat_set(player).set_retaliate true
end

on :button, 151 do |player|
  get_combat_set(player).set_retaliate false
end

append_combat_style 12298, 1
append_combat_style 5860, 1
append_combat_style 1772, 1
append_combat_style 2429, 1

append_combat_style 12297, 2
append_combat_style 5862, 2
append_combat_style 1771, 2
append_combat_style 2432, 2

append_combat_style 12296, 3
append_combat_style 5861, 3
append_combat_style 1770, 3
append_combat_style 2430, 3

append_combat_style 2431, 4