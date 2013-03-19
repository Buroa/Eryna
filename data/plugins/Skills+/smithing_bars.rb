BARS = []
  
class Bar
	attr_reader :bar_id, :base_xp, :item_group, :item_ids, :bar_amounts, :level_requirements, :configs
	
	def initialize(bar_id, base_xp, item_group, item_ids, bar_amounts, level_requirements, configs)
	  @bar_id = bar_id
    @base_xp = base_xp
    @item_group = item_group
    @item_ids = item_ids
    @bar_amounts = bar_amounts
    @level_requirements = level_requirements
    @configs = configs
	end
end

def append_items(bar)
  BARS.push(bar)
end

#Bronze
append_items Bar.new(2349, 12.5, 1119, [1205, 1277, 1321, 1291, 1307],
  [1, 1, 2, 2, 3],
  [1, 4, 5, 6, 14],
  [1, 5, 6, 7, 15]) #Col One
append_items Bar.new(2349, 12.5, 1120, [1351, 1422, 1337, 1375],
  [1, 1, 3, 2],
  [1, 2, 9, 10],
  [2, 3, 10, 11]) #Col Two
append_items Bar.new(2349, 12.5, 1121, [1103, 1075, 1087, 1117],
  [3, 3, 3, 5],
  [11, 16, 16, 18],
  [12, 17, 17, 19]) #Col Three
append_items Bar.new(2349, 12.5, 1122, [1139, 1155, 1173, 1189, 4819],
  [1, 2, 2, 3, 1],
  [3, 7, 8, 12, 4],
  [4, 8, 9, 13, 5]) #Col Four
append_items Bar.new(2349, 12.5, 1123, [819, 39, 864, 1794, -1],
  [1, 1, 1, 1, 1],
  [4, 5, 7, 4, -1],
  [1, 6, 8, 7, 7]) #Col Five
append_items Bar.new(2349, 12.5, 8430, [3095],
  [2],
  [8],
  [14]) #Claws
append_items Bar.new(2349, 12.5, 11460, [-1],
  [1],
  [-1],
  [1]) #Latern

#Iron
append_items Bar.new(2351, 25, 1119, [1203, 1279, 1323, 1293, 1309],
  [1, 1, 2, 2, 3],
  [15, 19, 20, 21, 29],
  [1, 5, 6, 7, 15]) #Col One
append_items Bar.new(2351, 25, 1120, [1349, 1420, 1335, 1363],
  [1, 1, 3, 2],
  [15, 17, 24, 25],
  [2, 3, 10, 11]) #Col Two
append_items Bar.new(2351, 25, 1121, [1101, 1067, 1081, 1115],
  [3, 3, 3, 5],
  [26, 31, 31, 33],
  [12, 17, 17, 19]) #Col Three
append_items Bar.new(2351, 25, 1122, [1137, 1153, 1175, 1191, 4820],
  [1, 2, 2, 3, 1],
  [18, 22, 23, 27, 19],
  [4, 8, 9, 13, 5]) #Col Four
append_items Bar.new(2351, 25, 1123, [820, 40, 863, -1, -1],
  [1, 1, 1, 1, 1],
  [19, 20, 23, -1, -1],
  [1, 6, 8, 7, 7]) #Col Five
append_items Bar.new(2351, 25, 8430, [3096],
  [2],
  [28],
  [14]) #Claws
append_items Bar.new(2351, 25, 11460, [4540],
  [1],
  [26],
  [1]) #Latern

#Steel
append_items Bar.new(2353, 37.5, 1119, [1207, 1281, 1325, 1295, 1311],
  [1, 1, 2, 2, 3],
  [30, 34, 35, 36, 44],
  [1, 5, 6, 7, 15]) #Col One
append_items Bar.new(2353, 37.5, 1120, [1353, 1424, 1339, 1365],
  [1, 1, 3, 2],
  [31, 32, 39, 40],
  [2, 3, 10, 11]) #Col Two
append_items Bar.new(2353, 37.5, 1121, [1105, 1069, 1083, 1119],
  [3, 3, 3, 5],
  [41, 46, 46, 48],
  [12, 17, 17, 19]) #Col Three
append_items Bar.new(2353, 37.5, 1122, [1141, 1157, 1177, 1193, 1539],
  [1, 2, 2, 3, 1],
  [33, 37, 41, 42, 34],
  [4, 8, 9, 13, 5]) #Col Four
append_items Bar.new(2353, 37.5, 1123, [821, 41, 865, -1, 2370],
  [1, 1, 1, 1, 1],
  [34, 35, 37, -1, 36],
  [1, 6, 8, 7, 7]) #Col Five
append_items Bar.new(2353, 37.5, 8430, [3097],
  [2],
  [43],
  [14]) #Claws
append_items Bar.new(2353, 37.5, 11460, [4544],
  [1],
  [49],
  [1]) #Latern

#Mithril
append_items Bar.new(2359, 50, 1119, [1209, 1285, 1329, 1299, 1315],
  [1, 1, 2, 2, 3],
  [50, 54, 55, 56, 64],
  [1, 5, 6, 7, 15]) #Col One
append_items Bar.new(2359, 50, 1120, [1355, 1428, 1343, 1369],
  [1, 1, 3, 2],
  [51, 52, 59, 60],
  [2, 3, 10, 11]) #Col Two
append_items Bar.new(2359, 50, 1121, [1109, 1071, 1085, 1121],
  [3, 3, 3, 5],
  [61, 66, 66, 68],
  [12, 17, 17, 19]) #Col Three
append_items Bar.new(2359, 50, 1122, [1143, 1159, 1181, 1197, 4822],
  [1, 2, 2, 3, 1],
  [53, 57, 58, 62, 54],
  [4, 8, 9, 13, 5]) #Col Four
append_items Bar.new(2359, 50, 1123, [822, 42, 866, -1, -1],
  [1, 1, 1, 1, 1],
  [54, 55, 56, -1, -1],
  [1, 6, 8, 7, 7]) #Col Five
append_items Bar.new(2359, 50, 8430, [3099],
  [2],
  [63],
  [14]) #Claws
append_items Bar.new(2359, 50, 11460, [-1],
  [1],
  [-1],
  [1]) #Latern

#Adamant
append_items Bar.new(2361, 62.5, 1119, [1211, 1287, 1331, 1301, 1317],
  [1, 1, 2, 2, 3],
  [70, 74, 75, 76, 83],
  [1, 5, 6, 7, 15]) #Col One
append_items Bar.new(2361, 62.5, 1120, [1357, 1430, 1345, 1371],
  [1, 1, 3, 2],
  [71, 72, 79, 80],
  [2, 3, 10, 11]) #Col Two
append_items Bar.new(2361, 62.5, 1121, [1111, 1073, 1091, 1123],
  [3, 3, 3, 5],
  [81, 86, 86, 88],
  [12, 17, 17, 19]) #Col Three
append_items Bar.new(2361, 62.5, 1122, [1145, 1161, 1183, 1199, 4823],
  [1, 2, 2, 3, 1],
  [73, 77, 78, 82, 74],
  [4, 8, 9, 13, 5]) #Col Four
append_items Bar.new(2361, 62.5, 1123, [823, 43, 867, -1, -1],
  [1, 1, 1, 1, 1],
  [74, 75, 77, -1, -1],
  [1, 6, 8, 7, 7]) #Col Five
append_items Bar.new(2361, 62.5, 8430, [3100],
  [2],
  [83],
  [14]) #Claws
append_items Bar.new(2361, 62.5, 11460, [-1],
  [1],
  [-1],
  [1]) #Latern

#Rune
append_items Bar.new(2363, 75, 1119, [1213, 1289, 1333, 1303, 1319],
  [1, 1, 2, 2, 3],
  [85, 89, 90, 91, 99],
  [1, 5, 6, 7, 15]) #Col One
append_items Bar.new(2363, 75, 1120, [1359, 1432, 1347, 1373],
  [1, 1, 3, 2],
  [86, 87, 94, 95],
  [2, 3, 10, 11]) #Col Two
append_items Bar.new(2363, 75, 1121, [1113, 1079, 1093, 1127],
  [3, 3, 3, 5],
  [96, 99, 99, 99],
  [12, 17, 17, 19]) #Col Three
append_items Bar.new(2363, 75, 1122, [1147, 1163, 1185, 1201, 4824],
  [1, 2, 2, 3, 1],
  [75, 92, 93, 97, 89],
  [4, 8, 9, 13, 5]) #Col Four
append_items Bar.new(2363, 75, 1123, [824, 44, 868, -1, -1],
  [1, 1, 1, 1, 1],
  [89, 90, 92, -1, -1],
  [1, 6, 8, 7, 7]) #Col Five
append_items Bar.new(2363, 75, 8430, [3101],
  [2],
  [150],
  [14]) #Claws
append_items Bar.new(2363, 75, 11460, [-1],
  [1],
  [-1],
  [1]) #Latern