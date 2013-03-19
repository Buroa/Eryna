require 'java'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.Animation'

AGILITYOBJECTS = {}

# Types
balance = CrossinAgilityExecution.new Animation.new(762)
pipes = CrossinAgilityExecution.new Animation.new(844)
branchs = DefaultAgilityExecution.new Animation.new(828)
net = DefaultAgilityExecution.new Animation.new(828)

# Courses
gnome = AgilityCourse.new 6, 40

class AgilityObject

  attr_reader :level, :exp, :position, :destination, :execution, :course, :click

  def initialize(level, exp, destination, execution, course, click)
    @level = level
    @exp = exp
    @destination = destination
    @execution = execution
    @course = course
    @click = click
  end

  def execute(player)
    @execution.execute self, player
  end
end

def append_agility_object(id, agility_object)
  AGILITYOBJECTS[id] = agility_object
end

append_agility_object 2295, AgilityObject.new(1, 8, Position.new(2474, 3429, 0), balance, gnome, 1)
append_agility_object 2285, AgilityObject.new(1, 8, Position.new(2473, 3425, 1), net,     gnome, 2)
append_agility_object 2313, AgilityObject.new(1, 5, Position.new(2473, 3420, 2), branchs, gnome, 3)
append_agility_object 2312, AgilityObject.new(1, 7, Position.new(2483, 3420, 2), balance, gnome, 4)
append_agility_object 2314, AgilityObject.new(1, 5, Position.new(2486, 3419, 0), branchs, gnome, 5)
append_agility_object 2286, AgilityObject.new(1, 8, Position.new(2486, 3427, 0), net,     gnome, 6)
append_agility_object 4058, AgilityObject.new(1, 7, Position.new(2487, 3437, 0), pipes,   gnome, 7)
append_agility_object  154, AgilityObject.new(1, 7, Position.new(2484, 3437, 0), pipes,   gnome, 7)