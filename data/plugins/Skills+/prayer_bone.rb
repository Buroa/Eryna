BONES = {}

class Bone

  attr_reader :item, :exp

  def initialize(item, exp)
    @item = item
    @exp = exp
  end

end

def append_bone(bone)
  BONES[bone.item] = bone
end

append_bone Bone.new(526, 5)
append_bone Bone.new(2530, 5)
append_bone Bone.new(528, 5)
append_bone Bone.new(530, 14)
append_bone Bone.new(2859, 14)
append_bone Bone.new(3179, 14)
append_bone Bone.new(3180, 14)
append_bone Bone.new(3181, 14)
append_bone Bone.new(3182, 14)
append_bone Bone.new(3183, 14)
append_bone Bone.new(3185, 14)
append_bone Bone.new(3186, 14)
append_bone Bone.new(3187, 14)
append_bone Bone.new(3125, 15)
append_bone Bone.new(4812, 23)
append_bone Bone.new(3127, 25)
append_bone Bone.new(3123, 25)
append_bone Bone.new(532, 45)
append_bone Bone.new(534, 30)
append_bone Bone.new(6812, 50)
append_bone Bone.new(536, 72)
append_bone Bone.new(4830, 84)
append_bone Bone.new(4932, 96)
append_bone Bone.new(4834, 140)
append_bone Bone.new(6729, 125)