class CombatListener

  def battle_executed(source, victim)
    # needs to be overridden
  end

  def death_executed(source, victim)
    # needs to be overridden
  end

  def custom_death
    false
    # needs to be overridden
  end

end