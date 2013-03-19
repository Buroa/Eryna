BALLOONS = [115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130]
REGISTERED_PARTYROOM_OBJECTS = {}
BALLOONS_INVENTORY = nil

on :event, :object_action do |ctx, player, event|
  if event.id == 2416
    # we are a lever for the party room
    PARTY_ROOM.drop_activated
    drop_balloons
  end
end

def drop_balloons
 game_object_position = Position.new(2737, 3469).transform rand(6), rand(7), 0
 game_object = GameObject.new BALLOONS.sample, game_object_position, 10, 1
 REGISTERED_PARTYROOM_OBJECTS[game_object_position] = game_object
 World.world.register game_object
end