require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.inter.bank.BankUtils'

class BankAction < DistancedAction
  attr_reader :position

  def initialize(character, position)
    super 0, true, character, position, 1
    @position = position
  end

  def executeAction
    character.turn_to @position
    BankUtils.open_bank character
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end
end

on :event, :object_action do |ctx, player, event|
  if event.option == 2 and (event.id == 11758 or event.id == 2213)
    player.startAction BankAction.new(player, event.position)
    ctx.break_handler_chain
  end
end

on :event, :npc_option do |ctx, player, event|
  if event.option == 3
    interactions = event.npc.definition.interactions
    if interactions != nil and interactions[event.option-1] != nil and interactions[event.option-1].downcase == "bank"
      player.start_action BankAction.new(player, event.npc.position)
      ctx.break_handler_chain
    end
  end
end

on :button, 23007 do |player, command|
  BankUtils.deposit_inventory player
end

# TODO: when we have NPCs/dialogue, also respond to clicking on them