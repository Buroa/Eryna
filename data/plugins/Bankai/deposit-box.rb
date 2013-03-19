require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.inter.bank.DepositBoxUtils'

DEPOSIT_BOX_ID = 9398
DEPOSIT_BOX_SIZE = 1

class DepositBoxAction < DistancedAction
  attr_reader :position

  def initialize(character, position)
    super 0, true, character, position, DEPOSIT_BOX_SIZE
    @position = position
  end

  def executeAction
    character.turn_to position
    DepositBoxUtils.open_box character
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end
end

on :event, :object_action do |ctx, player, event|
  if event.id == DEPOSIT_BOX_ID and event.option == 1
    player.startAction DepositBoxAction.new(player, event.position)
  end
end