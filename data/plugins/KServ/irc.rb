# Import ssl support
require 'java'
java_import 'org.jibble.pircbot.TrustingSSLSocketFactory'

$pirc = nil
$dispatcher = nil

# Removes all instances and loads a new one
def _irc_new_instance
  $pirc = IrcListener.new
  $dispatcher = ChatDispatcher.new $pirc
end

# Register commands
def _irc_register_commands
  $dispatcher.register ".players", PlayersCommandListener.new
  $dispatcher.register ".time", TimeCommandListener.new
  $dispatcher.register ".part", PartCommandListener.new
  $dispatcher.register ".stats", StatsCommandListener.new
  $dispatcher.register ".uptime", UptimeCommandListener.new
  $dispatcher.register ".cmb", CombatCommandListener.new
  $dispatcher.register ".skill", SkillCommandListener.new
  $dispatcher.register ".yell", YellCommandListener.new
  $dispatcher.register ".price", PriceCommandListener.new
  $dispatcher.register ".help", HelpCommandListener.new
  $dispatcher.register ".news", NewsCommandListener.new
  $dispatcher.register ".voice", VoiceCommandListener.new
  $dispatcher.register ".hop", HalfOpCommandListener.new
  $dispatcher.register ".op", OpCommandListener.new
  $dispatcher.register ".cmd", CmdCommandListener.new
  $dispatcher.register ".auth", AuthCommandListener.new
  $dispatcher.register ".logout", AuthCommandListener.new
  $dispatcher.register ".mass", MassMessageCommandListener.new
  $dispatcher.register ".kick", KickCommandListener.new
  $dispatcher.register ".topic", TopicCommandListener.new
  $dispatcher.register ".invite", InviteCommandListener.new
  $dispatcher.register ".raw", RawCommandListener.new
  $dispatcher.register ".debug", DebugCommandListener.new
  $dispatcher.register ".spy", SpyCommandListener.new
  $dispatcher.register ".ping", PingCommandListener.new
  $dispatcher.register ".rdns", ReverseDnsCommandListener.new
  $dispatcher.register ".eval", EvalCommandListener.new
end

# Set the default settings
def _irc_load_settings
  $pirc.set_message_delay 0
  $pirc.set_name "Eryna"
  $pirc.set_login "Eryna"
  $pirc.set_version "Eryna Services"
end

# Connect to the irc server
def _irc_connect
  $pirc.connect "bipartite.nj.us.SwiftIRC.net", 6697, TrustingSSLSocketFactory.new
  $pirc.identify "UUfp6WcA"
  $pirc.set_mode $pirc.nick, "+BTp"
  $pirc.join_channel "#eryna"
  $pirc.join_channel "#bots"
end

_irc_new_instance
_irc_register_commands
_irc_load_settings

# Have to catch otherwise server wont start
begin
  _irc_connect
rescue Exception=>e
  puts e
end