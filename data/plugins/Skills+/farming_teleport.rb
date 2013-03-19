require 'java'
java_import 'org.apollo.game.model.Npc'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.inter.dialog.DialogueListener'
java_import 'org.apollo.game.model.skill.farming.Farming'

# Define all of the farmers
FARMERS = {
  1 => Npc.new(1701, Position.new(2817, 3468)), # Catherby
  2 => Npc.new(1701, Position.new(3050, 3303)), # Falador
  3 => Npc.new(1701, Position.new(3599, 3522)), # Phasmatys
  4 => Npc.new(1701, Position.new(2661, 3374)), # Ardougne
}

# Register the defined farmers
FARMERS.each do |index, npc|
  World.world.register npc
  npc.set_random_walking true
end

# Have we clicked one of the farmers
on :event, :npc_option do |ctx, player, event|
  if event.option == 2
    if event.npc.id == 1701
      player.interface_set.send_option FarmingNpcDialogueListener.new(index_of(event.npc.first_position.x)), "Catherby", "Falador", "Phasmatys", "More Options"
    end
  end
end

# Function to help find the index of a farmer
def index_of(x)
  case x
  when 2817
    return 1
  when 3050
    return 2
  when 3599
    return 3
  when 2661
    return 4
  end
end

# Helps determine what we have clicked
class FarmingNpcDialogueListener
  java_implements 'org.apollo.game.model.inter.dialog.DialogueListener'
  attr_reader :index, :page

  def initialize(index)
    @index = index
    @page = 0
  end

  def interfaceClosed(player, manually)
  end

  def continued(player)
  end

  def buttonClicked(player, button)
    if @page == 0
      case button
      when 2482
        player.teleport Position.new(2817, 3468)
        player.interface_set.close
        return
      when 2483
        player.teleport Position.new(3050, 3303)
        player.interface_set.close
        return
      when 2484
        player.teleport Position.new(3599, 3522)
        player.interface_set.close
        return
      when 2485
        @page = 1
        player.interface_set.close
        player.interface_set.send_option self, "Ardougne", "Watch my patches for me", "Back"
        return
      end
    elsif @page == 1
      case button
      when 2471
        player.teleport Position.new(2661, 3374)
        player.interface_set.close
        return
      when 2472
        # watch my patches for me
        if player.inventory.contains 995, 10000
          Farming.watch player, 0, @index
          player.inventory.remove 995, 10000
        else
          player.send_message "You need to pay me atleast 10k to watch your patches!"
        end
        player.interface_set.close
        return
      when 2473
        @page = 0
        player.interface_set.close
        player.interface_set.send_option self, "Catherby", "Falador", "Phasmatys", "More Options"
        return
      end
    end
  end
end