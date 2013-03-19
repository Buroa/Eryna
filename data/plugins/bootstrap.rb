# A script to 'bootstrap' all of the other plugins, wrapping Apollo's verbose
# Java-style API in a Ruby-style API.
#
# Written by Graham.

# ********************************** WARNING **********************************
# * If you do not really understand what this is for, do not edit it without  *
# * creating a backup! Many plugins rely on the behaviour of this script, and *
# * will break if you mess it up.                                             *
# *                                                                           *
# * This is actually part of the core server and in an ideal world shouldn't  *
# * be changed.                                                               *
# *****************************************************************************

require 'java'
java_import 'org.apollo.game.event.handler.EventHandler'
java_import 'org.apollo.net.release.EventEncoder'
java_import 'org.apollo.net.release.EventDecoder'
java_import 'org.apollo.io.player.PlayerListener'
java_import 'org.apollo.game.command.PrivilegedCommandListener'
java_import 'org.apollo.game.model.Player'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.scheduling.ScheduledTask'
java_import 'org.apollo.game.model.inter.store.Shop'
java_import 'org.apollo.world.WorldListener'
java_import 'java.util.logging.Logger'
java_import 'java.util.logging.FileHandler'

# Alias for the privilege levels
RIGHTS_STANDARD = Player::PrivilegeLevel::STANDARD
RIGHTS_MEMBER   = Player::PrivilegeLevel::MEMBER
RIGHTS_MOD      = Player::PrivilegeLevel::MODERATOR
RIGHTS_ADMIN    = Player::PrivilegeLevel::ADMINISTRATOR
RIGHTS_DEV      = Player::PrivilegeLevel::DEVELOPER
RIGHTS_OWNER    = Player::PrivilegeLevel::OWNER

# Alias for the shops
STORE_NORMAL    = Shop::ShopType::NORMAL
STORE_REDUNDANT = Shop::ShopType::REDUNDANT
STORE_MIXED     = Shop::ShopType::MIXED
STORE_CLOSED    = Shop::ShopType::CLOSED

# Set this to true if you want to print out information
$debug   = FALSE

# Extends the (Ruby) String class with a method to convert a lower case,
# underscore delimited string to camel-case.
class String
  def camelize
    gsub(/(?:^|_)(.)/) { $1.upcase }
  end
end

# A CommandListener which executes a Proc object with two arguments: the player
# and the command.
class ProcCommandListener < PrivilegedCommandListener
  def initialize(rights, block)
    super rights
    @block = block
  end

  def executePrivileged(player, command)
    @block.call player, command
  end
end

# An EventHandler which executes a Proc object with three arguments: the chain
# context, the player and the event.
class ProcEventHandler < EventHandler
  def initialize(block)
    super() # required (with brackets!), see http://jira.codehaus.org/browse/JRUBY-67
    @block = block
  end

  def handle(ctx, player, method)
    @block.call ctx, player, method
  end
end

# An EventDecoder which executes a Proc object with one argument: the packet
class ProcEventDecoder < EventDecoder
  def initialize(block)
    super()
    @block = block
  end

  def decode(packet)
    if $debug then puts "dec #{packet}" end
    return @block.call packet
  end
end

# An EventEncoder which executes a Proc object with one argument: the event
class ProcEventEncoder < EventEncoder
  def initialize(block)
    super()
    @block = block
  end

  def encode(event)
    if $debug then puts "enc #{event}" end
    return @block.call event
  end
end

# An PlayerListener which executes a Proc object with one argument: the event
class ProcLoginPlayerListener < PlayerListener
  def initialize(block)
    super()
    @block = block
  end

  def login(player)
    @block.call player
  end

  def logout(player)
  end
end

# An PlayerListener which executes a Proc object with one argument: the event
class ProcLogoutPlayerListener < PlayerListener
  def initialize(block)
    super()
    @block = block
  end

  def login(player)
  end

  def logout(player)
    @block.call player
  end
end

# An WorldListener which executes a Proc object with one argument: the request
class ProcWorldListener
  java_implements 'org.apollo.world.WorldListener'

  def initialize(block)
    super()
    @block = block
  end

  def execute(request)
    return @block.call request
  end
end

# A ScheduledTask which executes a Proc object with one argument (itself).
class ProcScheduledTask < ScheduledTask
  def initialize(delay, immediate, block)
    super delay, immediate
    @block = block
  end

  def execute
    @block.call self
  end
end

# Schedules a ScheduledTask. Can be used in two ways: passing an existing
# ScheduledTask object or passing a block along with one or two parameters: the
# delay (in pulses) and, optionally, the immediate flag.
#
# If the immediate flag is not given, it defaults to false.
#
# The ScheduledTask object is passed to the block so that methods such as
# setDelay and stop can be called. execute MUST NOT be called - if it is, the
# behaviour is undefined (and most likely it'll be bad).
def schedule(*args, &block)
  if block_given?
    if args.length == 1 or args.length == 2
      delay = args[0]
      immediate = args.length == 2 ? args[1] : false
      World.world.schedule ProcScheduledTask.new(delay, immediate, block)
    else
      raise "invalid combination of arguments"
    end
  elsif args.length == 1
    World.world.schedule args[0]
  else
    raise "invalid combination of arguments"
  end
end

# Defines some sort of action to take upon an event. The following 'kinds' of
# event are currently valid:
#
#   * :command
#   * :event
#   * :button
#   * :http
#   * :encode
#   * :decode
#   * :login
#   * :logout
#
# A command takes one or two arguments (the command name and optionally the
# minimum rights level to use it). The minimum rights level defaults to
# STANDARD. The block should have two arguments: player and command.
#
# An event takes no arguments. The block should have three arguments: the chain
# context, the player and the event object.
#
# A button takes one argument (the id). The block should have one argument: the
# player who clicked the button.
#
# A encode takes two arguments: the release version and the packet id,
# The block should have one argument: the packet.
#
def on(kind, *args, &block)
  case kind
    when :command then on_command(args, block)
    when :event   then on_event(args, block)
    when :button  then on_button(args, block)
    when :encode  then on_encode(args, block)
    when :decode  then on_decode(args, block)
    when :login   then on_login(args, block)
    when :logout  then on_logout(args, block)
    when :world   then on_world(args, block)
    else raise "unknown event type"
  end
end

# Defines an action to be taken upon a button press.
def on_button(args, proc)
  if args.length != 1
    raise "button must have one argument"
  end

  id = args[0].to_i

  on :event, :button do |ctx, player, event|
    if event.interface_id == id
      proc.call player
    end
  end
end

# Defines an action to be taken upon an event.
# The event can either be a symbol with the lower case, underscored class name
# or the class itself.
def on_event(args, proc)
  if args.length != 1
    raise "event must have one argument"
  end

  evt = args[0]
  if evt.is_a? Symbol
    class_name = evt.to_s.camelize.concat "Event"
    evt = Java::JavaClass.for_name("org.apollo.game.event.impl.".concat class_name)
  end

  $ctx.add_last_event_handler evt, ProcEventHandler.new(proc)
end

# Defines an action to be taken upon a command.
def on_command(args, proc)
  if args.length != 1 and args.length != 2
    raise "command event must have one or two arguments"
  end

  rights = RIGHTS_STANDARD
  if args.length == 2
    rights = args[1]
  end

  $ctx.add_command_listener args[0].to_s, ProcCommandListener.new(rights, proc)
end

# Defines an action to be taken upon an event decode.
def on_decode(args, proc)
  if args.length < 2
    raise "decode must have two or more arguments"
  end

  decoder = ProcEventDecoder.new proc
  release = args[0].to_i
  for i in 1..args.length
    if i != args.length
      opcode = args[i].to_i
      $ctx.add_event_decoder release, opcode, decoder
    end
  end
end

# Defines an action to be taken upon an event encode.
# The event can either be a symbol with the lower case, underscored class name
# or the class itself.
def on_encode(args, proc)
  if args.length != 2
    raise "encode must have two argument"
  end

  release = args[0].to_i
  evt = args[1]
  if evt.is_a? Symbol
    class_name = evt.to_s.camelize.concat "Event"
    evt = Java::JavaClass.for_name("org.apollo.game.event.impl.".concat class_name)
  end

  $ctx.add_event_encoder release, evt, ProcEventEncoder.new(proc)
end

# Defines an action to be taken upon a logout.
def on_logout(args, proc)
    $ctx.add_player_listener ProcLogoutPlayerListener.new(proc)
end

# Defines an action to be taken upon a login.
def on_login(args, proc)
    $ctx.add_player_listener ProcLoginPlayerListener.new(proc)
end

# Defines an action to be taken upon an world.
def on_world(args, proc)
  if args.length != 2
    raise "world must have two arguments"
  end

  world_module = args[0]
  world_cmd = args[1]

  $ctx.add_world_listener world_module, world_cmd, ProcWorldListener.new(proc)
end

ProcEventHandler.__persistent__ = true

# set logging
# handler = FileHandler.new "./data/logs/#{Time.new}.log", 10000, 1
# Logger.get_logger("").addHandler handler