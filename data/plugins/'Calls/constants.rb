require 'java'
java_import 'org.apollo.net.codec.world.WorldResponse'
java_import 'com.google.gson.Gson'

GSON = Gson.new

class WorldConstants

  def self.UNKNOWN_ACCOUNT
    WorldResponse.new "no data sent", false
  end

  def self.PLAYER_OFFLINE
    WorldResponse.new "account is offline", false
  end

end