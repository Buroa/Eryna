require 'java'
java_import 'com.google.gson.JsonObject'

on :world, :account, :getPrivilegeLevel do |request|
  account_data = request.data.get "account"
  if account_data != nil
    account = World.world.get_player account_data
    if account != nil
      WorldResponse.new GSON.to_json(player.privilege_level)
    else
      WorldConstants.PLAYER_OFFLINE
    end
  else
    WorldConstants.UNKNOWN_ACCOUNT
  end
end

on :world, :account, :getPosition do |request|
  account_data = request.data.get "account"
  if account_data != nil
    account = World.world.get_player account_data
    if account != nil
      WorldResponse.new GSON.to_json(player.position)
    else
      WorldConstants.PLAYER_OFFLINE
    end
  else
    WorldConstants.UNKNOWN_ACCOUNT
  end
end