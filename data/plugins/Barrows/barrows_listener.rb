# Handles the death of barrows npcs
class BarrowsListener < CombatListener
  attr_reader :barrows_clazz

  def initialize(barrows_clazz)
    @barrows_clazz = barrows_clazz
  end

  def death_executed(source, victim)
    if source != nil and victim != nil
      if not victim.is_controlling
        barrows_clazz.remove source
        return true
      end
    end
    return false
  end
end