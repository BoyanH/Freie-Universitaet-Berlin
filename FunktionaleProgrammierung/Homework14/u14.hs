import UTM

{-
	Funktionale Programmierung

	Übungsblatt 14 (Abgabe: Mi., den 02.12. um 10:10 Uhr)
	Author: Boyan Hristov, Luis Herrmann
	Tutor: Zachrau, Alexander
	Tutorium: Dienstag; 12:00 - 14:00
-}

-- aufgabe1Tabelle = [ ("revertBits", '0', "revertBits", '1', R),
-- 					("revertBits", '1', "revertBits", '0', R),
-- 					("revertBits", ' ', "addOne", ' ', L),

-- 					("addOne", '1', "addOne", '0', L),
-- 					("addOne", '0', "returnResult", '1', L),
-- 					("addOne", ' ', "end", ' ', R),

-- 					("returnResult", '1', "returnResult", '1', L),
-- 					("returnResult", '0', "returnResult", '0', L),
-- 					("returnResult", ' ', "end", ' ', R),
-- 				  ]

aufgabe1Tabelle = [ (1, '0', 1, '1', R),
					(1, '1', 1, '0', R),
					(1, ' ', 2, ' ', L),

					(2, '1', 2, '0', L),
					(2, '0', 3, '1', L),
					(2, ' ', 0, ' ', R),

					(3, '1', 3, '1', L),
					(3, '0', 3, '0', L),
					(3, ' ', 0, ' ', R)
				  ]



aufgabe1 = start (1,"_ ",'0',"0101000 _") aufgabe1Tabelle


-- aufgabe2Tabelle = [ ("read", 'B', "read", 'B', R),
-- 					("read", 'A', "read", 'A', R),
-- 					("read", '<', "even1", '<', R),
-- 					("read", '0', "save0", 'A', R),
-- 					("read", '1', "save1", 'B', R),

-- 					("save0", '<', "compare0", '<', R),
-- 					("save0", '0', "save0", '0', R),
-- 					("save0", '1', "save0", '1', R),
-- 					("save1", '<', "compare1", '<', R),
-- 					("save1", '0', "save1", '0', R),
-- 					("save1", '1', "save1", '1', R),

-- 					("compare0", '0', "loop", 'A', L),
-- 					("compare0", '1', "bBigger1", 'B', R),
-- 					("compare1", '1', "loop", 'B', L),
-- 					("compare1", '0', "aBigger1", 'A', R),

-- 					("loop", 'A', "loop", 'A', L),
-- 					("loop", 'B', "loop", 'B', L),
-- 					("loop", '0', "loop", '0', L),
-- 					("loop", '1', "loop", '1', L),
-- 					("loop", '<', "loop", '<', L),
-- 					("loop", ' ', "read", ' ', R),
--
--					("even1", 'A', "even1", 'A', R),
-- 					("even1", 'B', "even1", 'B', R),
-- 					("even1", '1', "even1", '1', R),
-- 					("even1", '0', "even1", '0', R),
-- 					("even1", ' ', "even2", '=', R),
-- 					("even2", ' ', "even3", '0', R),
-- 					("even3", ' ', "returnInput", '0', L),

-- 					("aBigger1", '1', "aBigger1", '1', R),
-- 					("aBigger1", '0', "aBigger1", '0', R),
-- 					("aBigger1", ' ', "aBigger2", '=', R),
-- 					("aBigger2", ' ', "aBigger3", '1', R),
-- 					("aBigger3", ' ', "returnInput", '0', L),

-- 					("bBigger1", '1', "bBigger1", '1', R),
-- 					("bBigger1", '0', "bBigger1", '0', R),
-- 					("bBigger1", ' ', "bBigger2", '=', R),
-- 					("bBigger2", ' ', "bBigger3", '0', R),
-- 					("bBigger3", ' ', "returnInput", '1', L),

-- 					("returnInput", '=', "returnInput", '=', L),
-- 					("returnInput", '<', "returnInput", '<', L),
-- 					("returnInput", 'A', "returnInput", 'A', L),
-- 					("returnInput", 'B', "returnInput", 'B', L),
-- 					("returnInput", '0', "returnInput", '0', L),
-- 					("returnInput", '1', "returnInput", '1', L),					

-- 					("returnInput", ' ', "end", ' ', R)
-- 				  ]

aufgabe2Tabelle = [ (1, 'B', 1, 'B', R),
					(1, 'A', 1, 'A', R),
					(1, '<', 7, '<', R),
					(1, '0', 2, 'A', R),
					(1, '1', 3, 'B', R),

					(2, '<', 4, '<', R),
					(2, '0', 2, '0', R),
					(2, '1', 2, '1', R),
					(3, '<', 5, '<', R),
					(3, '0', 3, '0', R),
					(3, '1', 3, '1', R),

					(4, 'A', 4, 'A', R),
					(4, 'B', 4, 'B', R),
					(4, '0', 6, 'A', L),
					(4, '1', 13, 'B', R),
					(5, 'A', 5, 'A', R),
					(5, 'B', 5, 'B', R),
					(5, '1', 6, 'B', L),
					(5, '0', 10, 'A', R),

					(6, '<', 6, '<', L),
					(6, '0', 6, '0', L),
					(6, '1', 6, '1', L),
					(6, 'A', 6, 'A', L),
					(6, 'B', 6, 'B', L),
					(6, ' ', 1, ' ', R),

					(7, '0', 7, '0', R),
					(7, '1', 7, '1', R),
					(7, 'A', 7, 'A', R),
					(7, 'B', 7, 'B', R),
					(7, ' ', 8, '=', R),
					(8, ' ', 9, '0', R),
					(9, ' ', 16, '0', L),

					(10, '0', 10, '0', R),
					(10, '1', 10, '1', R),
					(10, ' ', 11, '=', R),
					(11, ' ', 12, '1', R),
					(12, ' ', 16, '0', L),

					(13, '0', 13, '0', R),
					(13, '1', 13, '1', R),
					(13, ' ', 14, '=', R),
					(14, ' ', 15, '0', R),
					(15, ' ', 16, '1', L),

					(16, '=', 16, '=', L),
					(16, '<', 16, '<', L),
					(16, 'A', 16, 'A', L),
					(16, 'B', 16, 'B', L),
					(16, '0', 16, '0', L),
					(16, '1', 16, '1', L),					

					(16, ' ', 0, ' ', R)
				  ]



aufgabe2 = start (1,"_ ",'0',"1101000<01100111       _") aufgabe2Tabelle


-- aufgabe3Tabelle = [ ("start", '0', "goToPlus", '0', R),
-- 					("start", '1', "goToPlus", '1', R),
-- 					("start", 'A', "clearA", ' ', R),
-- 					("start", 'B', "clearA", ' ', R),

-- 					("goToPlus", '0', "goToPlus", '0', R),
-- 					("goToPlus", '1', "goToPlus", '1', R),
-- 					("goToPlus", 'A', "goToPlus", 'A', R),
-- 					("goToPlus", 'B', "goToPlus", 'B', R),
-- 					("goToPlus", '+', "read", '+', L),
-- 					("goToPlus", 'P', "read", 'P', L),

-- 					("read", 'A', "read", 'A', L),
-- 					("read", 'B', "read", 'B', L),
-- 					("read", '0', "save0", 'A', R),
-- 					("read", '1', "save1", 'B', R),

-- 					("save0", 'A', "save0", 'A', R),
-- 					("save0", 'B', "save0", 'B', R),
-- 					("save0", '+', "goToEnd0", '+', R),
-- 					("save0", 'P', "goToEnd0", 'P', R),

-- 					("save1", 'A', "save1", 'A', R),
-- 					("save1", 'B', "save1", 'B', R),
-- 					("save1", '+', "goToEnd1", '+', R),
-- 					("save1", 'P', "goToEnd1", 'P', R),

-- 					("goToEnd0", '0', "goToEnd0", '0', R),
-- 					("goToEnd0", '1', "goToEnd0", '1', R),
-- 					("goToEnd0", 'A', "add0", 'A', L),
-- 					("goToEnd0", 'B', "add0", 'B', L),
-- 					("goToEnd0", ' ', "add0", ' ', L),

-- 					("goToEnd1", '0', "goToEnd1", '0', R),
-- 					("goToEnd1", '1', "goToEnd1", '1', R),
-- 					("goToEnd1", 'A', "add1", 'A', L),
-- 					("goToEnd1", 'B', "add1", 'B', L),
-- 					("goToEnd1", ' ', "add1", ' ', L),

-- 					("add0", '0', "goToStart", 'A', L),
-- 					("add0", '1', "goToStart", 'B', L),
-- 					("add1", '0', "goToStart", 'B', L),
-- 					("add1", '1', "keepAdding", 'A', L),

-- 					("keepAdding", '1', "keepAdding",  '0', L),
-- 					("keepAdding", '+', "goToStart",  'P', L),
-- 					("keepAdding", '0', "goToStart",  '1', L),

-- 					("goToStart", '0', "goToStart", '0', L),
-- 					("goToStart", '1', "goToStart", '1', L),
-- 					("goToStart", 'A', "goToStart", 'A', L),
-- 					("goToStart", 'B', "goToStart", 'B', L),
-- 					("goToStart", '+', "goToStart", '+', L),
-- 					("goToStart", 'P', "goToStart", 'P', L),
-- 					("goToStart", ' ', "start", ' ', R),

-- 					("clearA", 'B', "clearA", ' ', R),
-- 					("clearA", 'A', "clearA", ' ', R),
-- 					("clearA", '+', "converResult", ' ', R),
-- 					("clearA", 'P', "converResult", '1', R),

-- 					("converResult", 'A', "converResult", '0', R),
-- 					("converResult", 'B', "converResult", '1', R),
-- 					("converResult", ' ', "returnResult", ' ', L),

-- 					("returnResult", '0', "returnResult", '0', L),
-- 					("returnResult", '1', "returnResult", '1', L),
-- 					("returnResult", ' ', "end", ' ', R),


-- 				  ]

aufgabe3Tabelle = [ (1, '0', 2, '0', R),
					(1, '1', 2, '1', R),
					(1, 'A', 11, ' ', R),
					(1, 'B', 11, ' ', R),

					(2, '0', 2, '0', R),
					(2, '1', 2, '1', R),
					(2, 'A', 2, 'A', R),
					(2, 'B', 2, 'B', R),
					(2, '+', 3, '+', L),
					(2, 'P', 3, 'P', L),

					(3, 'A', 3, 'A', L),
					(3, 'B', 3, 'B', L),
					(3, '0', 4, 'A', R),
					(3, '1', 5, 'B', R),

					(4, 'A', 4, 'A', R),
					(4, 'B', 4, 'B', R),
					(4, '+', 6, '+', R),
					(4, 'P', 6, 'P', R),

					(5, 'A', 5, 'A', R),
					(5, 'B', 5, 'B', R),
					(5, '+', 7, '+', R),
					(5, 'P', 7, 'P', R),

					(6, '0', 6, '0', R),
					(6, '1', 6, '1', R),
					(6, 'A', 8, 'A', L),
					(6, 'B', 8, 'B', L),
					(6, ' ', 8, ' ', L),

					(7, '0', 7, '0', R),
					(7, '1', 7, '1', R),
					(7, 'A', 14, 'A', L),
					(7, 'B', 14, 'B', L),
					(7, ' ', 14, ' ', L),

					(8, '0', 10, 'A', L),
					(8, '1', 10, 'B', L),
					(14, '0', 10, 'B', L),
					(14, '1', 9, 'A', L),

					(9, '1', 9,  '0', L),
					(9, '+', 10,  'P', L),
					(9, '0', 10,  '1', L),

					(10, '0', 10, '0', L),
					(10, '1', 10, '1', L),
					(10, 'A', 10, 'A', L),
					(10, 'B', 10, 'B', L),
					(10, '+', 10, '+', L),
					(10, 'P', 10, 'P', L),
					(10, ' ', 1, ' ', R),

					(11, 'B', 11, ' ', R),
					(11, 'A', 11, ' ', R),
					(11, '+', 12, ' ', R),
					(11, 'P', 12, '1', R),

					(12, 'A', 12, '0', R),
					(12, 'B', 12, '1', R),
					(12, ' ', 13, ' ', L),

					(13, '0', 13, '0', L),
					(13, '1', 13, '1', L),
					(13, ' ', 0, ' ', R)


				  ]

aufgabe3 = start (1,"_ ",'1',"011+0111       _") aufgabe3Tabelle