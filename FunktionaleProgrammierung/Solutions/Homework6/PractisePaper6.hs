{-
	Funktionale Programmierung

	Übungsblatt 5 (Abgabe: Mi., den 02.12. um 10:10 Uhr)
	Author: Boyan Hristov, Luis Herrmann
	Tutor: Zachrau, Alexander
	Tutorium: Dienstag; 12:00 - 14:00
-}

{-
----------------------------------------------------------------------------------
	Exercise 1
----------------------------------------------------------------------------------
-}

abflachenR :: [[a]] -> [a]
abflachenR xs = foldr (++) [] xs 

abflachenL :: [[a]] -> [a]
abflachenL (x:xs) = foldl (++) x xs

{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}

toDecFrom :: [Int] -> Int -> Int
toDecFrom xs base -- by the definition of the task. It would work perfectly for unary system (x*1) is x => x1+x2+x3... correct ;)
				| base <= 1 || base >= 10 = error "toDecFrom only defined for 1<base<10"
				| otherwise = foldl addBinDigit 0 xs
								where
									addBinDigit x y = (x*base) + y

{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}

data Color = RGB (Int, Int, Int) | CMYK (Float, Float, Float, Float)
								deriving (Show)

rgb2cmyk :: Color -> Color
rgb2cmyk (RGB (0, 0, 0)) = CMYK (0, 0, 0, 1)
rgb2cmyk (RGB (r, g, b))| not (isRGBValid (r,g,b)) = error "RGB color is not valid!"
						| otherwise = CMYK (c, m, y, k) --return all calculated values of CMYK as tuple
							where
							 	w = maximum [rDiv255, bDiv255, gDiv255] --calculating c, m, y and k using the given formula
							 	c = (w - rDiv255) / w
							 	m = (w - gDiv255) / w
							  	y = (w - bDiv255) / w
							 	k = 1 - w

							 	rDiv255 = (fromIntegral r) / 255
							 	bDiv255 = (fromIntegral g) / 255
							 	gDiv255 = (fromIntegral b) / 255

isRGBValid :: (Int, Int, Int) -> Bool
isRGBValid (r,g,b) = isValid r && isValid g && isValid b
					where
						isValid x = x >= 0 && x <= 255

{-
----------------------------------------------------------------------------------
	Exercise 4
----------------------------------------------------------------------------------
-}

data RootNum = RootNum (Int, Int) 

rootNumAdd :: RootNum -> RootNum -> RootNum
rootNumAdd (RootNum (a1, b1)) (RootNum (a2, b2)) = RootNum (resultA, resultB)
												where
													resultA = a1 + a2
													resultB = b1 + b2

rootNumSub :: RootNum -> RootNum -> RootNum
rootNumSub (RootNum (a1, b1)) (RootNum (a2, b2)) = RootNum (resultA, resultB)
												where
													resultA = a1 - a2
													resultB = b1 - b2

rootNumMult :: RootNum -> RootNum -> RootNum
rootNumMult (RootNum (a1, b1)) (RootNum (a2, b2)) = RootNum (resultA, resultB)
												where 
													resultA = (a1*a2) + (b1*b2*2)
													resultB = (a1*b2) + (b1*a2)

instance Num RootNum where
	x-y = rootNumSub x y
	x+y = rootNumAdd x y
	x*y = rootNumMult x y

instance Show RootNum where
	show (RootNum (a,b)) = (show a) ++ "+" ++ (show b) ++ "√2"

getValue :: RootNum -> Double
getValue (RootNum (a, b)) = (fromIntegral a) + ( (fromIntegral b) * (sqrt 2) )


{-
----------------------------------------------------------------------------------
	Exercise 5
----------------------------------------------------------------------------------
-}

--helper functions

countElem :: (Eq a) => a -> (a, Int) -> [a] -> [a] -> ((a, Int), [a])
countElem elmnt occurances [] notEquals = (occurances, notEquals)
countElem elmnt (elemX, lengthX) (x:xs) notEquals 
											| elmnt == x = countElem elmnt (elemX, lengthX+1) xs notEquals
											| otherwise = countElem elmnt (elemX, lengthX) xs (x:notEquals)

--general approach

--allOccurances :: (Eq a) => [(a, Int)] -> [a] -> [(a, Int)]
--allOccurances accu [] = accu
--allOccurances accu (x:xs) = allOccurances (countX:accu) notXElmnts
--									where
--										(countX, notXElmnts) = countElem x (x,1) xs []

--maxOccurance :: (Eq a) => [a] -> (a, Int)
--maxOccurance list = foldl maxOcc first groupedOcc
--					where
--						(first:groupedOcc) = allOccurances [] list
--						maxOcc (item1, length1) (item2, length2) 
--																| length1 > length2 = (item1, length1)
--																| otherwise = (item2, length2)

--optimized
maxOccurance :: (Eq a) => [a] -> (a, Int) -> (a, Int)
maxOccurance [] prevMax = prevMax
maxOccurance (x:xs) (prevX, prevLength) 
								| crntLength > prevLength = maxOccurance xs (crntX, crntLength)
								| otherwise = maxOccurance xs (prevX, prevLength)
									where
										((crntX, crntLength), notXElmnts) = countElem x (x,1) xs []

--main function

majority :: (Eq a) => [a] -> Maybe a
majority list
		| majLength >= ((div (length list) 2) + 1) = Just maj
		| otherwise = Nothing
		where
			(maj, majLength) = maxOccurance list (head list, 1)


{-
----------------------------------------------------------------------------------
	Exercise 6
----------------------------------------------------------------------------------
-}

data Nat = Zero | Succ Nat
			deriving (Eq, Show)

instance Ord Nat where
					(Succ a) > Zero = True
					Zero > (Succ b) = False
					Zero > Zero = False
					(Succ a) > (Succ b) = (>) a b

instance Num Nat where
	a - b = cutSub a b
	a + b = addNat a b

natUngerade :: Nat -> Bool
natUngerade Zero = False
natUngerade (Succ (Zero)) = True
natUngerade (Succ (Succ n)) = natUngerade n

cutSub :: Nat -> Nat -> Nat
cutSub x y 
		| x > y = helpSub x y
		| otherwise = Zero
		where
			helpSub (Succ a) Zero = (Succ a)
			helpSub (Succ a) (Succ b) = helpSub a b

addNat :: Nat -> Nat -> Nat
addNat a Zero = a
addNat a (Succ b) = addNat (Succ a) b

natMax :: Nat -> Nat -> Nat
natMax a b 
		| b > a = b --return b if b>a
		| otherwise = a --return a if a>=b

natFib :: Nat -> Nat
natFib Zero = Zero
natFib (Succ Zero) = Succ Zero
natFib (Succ (Succ a)) = (natFib (Succ a)) + (natFib a)