require 'java'
java_import 'org.apollo.util.DefaultUtil'

class ChatCommand
  attr_reader :sender, :channel, :command

  def initialize(sender, channel, message)
    @sender = sender
    @channel = channel
    @command = DefaultUtil.create_command message
  end
end