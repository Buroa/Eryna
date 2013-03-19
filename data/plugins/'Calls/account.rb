require 'java'
java_import 'org.apollo.net.codec.world.WorldResponse'
java_import 'com.google.gson.Gson'
java_import 'com.google.gson.JsonObject'

UNKNOWN_ACCOUNT = WorldResponse.new "no data sent", false
GSON = Gson.new

on :world, :account, :getPrivilegeLevel do |request|
  account = request.data.get "account"
  if account != nil
    player = loadPlayer(account).player
    WorldResponse.new GSON.to_json(player.privilege_level)
  else
    UNKNOWN_ACCOUNT
  end
end

on :world, :account, :getPosition do |request|
  account = request.data.get "account"
  if account != nil
    player = loadPlayer(account).player
    WorldResponse.new GSON.to_json(player.position)
  else
    UNKNOWN_ACCOUNT
  end
end