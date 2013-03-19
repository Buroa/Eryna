require 'java'
java_import 'org.apollo.game.model.Skill'

class AgilityExecution
  attr_reader :animation

  def initialize(animation)
    @animation = animation
  end

  def execute(obj, player)
    skills = player.skill_set
    level = skills.skill(Skill::AGILITY).maximum_level # TODO: is using max level correct?

    # check if we are high enough level
    if level < obj.level
      character.send_message "You need a Agility level of at least #{@level} to use this."
      return false
    end

    # increase click
    obj.course.raise player.name, obj.click

    # all good
    player.skill_set.add_experience Skill::AGILITY, obj.exp

    # completion check
    if obj.course.complete player.name
      player.skill_set.add_experience Skill::AGILITY, obj.course.bonus
    end
    return true
  end
end

class DefaultAgilityExecution < AgilityExecution
  def initialize(animation)
    super(animation)
  end

  def execute(obj, player)
    if super obj, player
      player.play_animation animation
      schedule 1 do |task|
        player.teleport obj.destination
        task.stop
      end
    end
  end
end

class CrossinAgilityExecution < AgilityExecution
  def initialize(animation)
    super(animation)
  end

  def execute(obj, player)
    if super obj, player
      player.appearance.set_walk_animation animation.id
      player.appearance.set_run_animation animation.id
      player.set_appearance player.appearance
      player.walking_queue.walk_to obj.destination
      player.walking_queue.stop 6
      schedule 6 do |task|
        player.appearance.set_walk_animation -1
        player.appearance.set_run_animation -1
        player.set_appearance player.appearance
        player.walking_queue.stop 6
        task.stop
      end
    end
  end
end