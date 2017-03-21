{-
	Funktionale Programmierung

	Übungsblatt 6 (Abgabe: Mi., den 25.11. um 10:10 Uhr)
	Author: Boyan Hristov, Luis Herrmann
	Tutor: Zachrau, Alexander
	Tutorium: Dienstag; 12:00 - 14:00
-}

{-
----------------------------------------------------------------------------------
	Globally required functions
----------------------------------------------------------------------------------
-}

qReverse xs = loop xs []
	where
	loop [] ys = ys
	loop (x:xs) ys = loop xs (x:ys)
	
int2float :: Int -> Float
int2float x = fromIntegral x :: Float

int2double :: Int -> Double
int2double x = fromIntegral x :: Double

{-
----------------------------------------------------------------------------------
	Exercise 1
----------------------------------------------------------------------------------
-}

{-abflachen :: [[a]] -> [a]
abflachen list = foldr (++) [] list

abflachen_ :: [[a]] -> [a]
abflachen_ list = foldl (++) [] list-}

abflachen :: [[a]] -> [a]
abflachen list = loop list []
	where
	loop [] retList = qReverse(retList)
	loop (x:xs) retList = loop xs (foldl (flip (:)) retList x)
	
abflachen_ :: [[a]] -> [a]
abflachen_ list = loop (qReverse list) []
	where
	loop [] retList = retList
	loop (x:xs) retList = loop xs (foldr (:) retList x)

--TODO: Check improvements

{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}

toDecFrom :: Int -> [Int] -> Int
toDecFrom base x
	| x == [] = error "List must contain at least one element"
	| (base <= 0) || (base >= 10) = error "No base in {1,..,9}"
	| otherwise = foldl f 0 x
		where
		f :: Int -> Int -> Int
		f x y 
			| (x > base) || (x < 1) = error "Number is not of specified base"
			| otherwise = base*x+y
			
{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}

data Color = RGB (Int, Int, Int) | CMYK (Float, Float, Float, Float)
	deriving (Eq, Ord, Show)

w :: Color -> Float
w (RGB (r,g,b)) = maximum (map (/255) (map int2float [r,g,b]))
	
rgb2cmyk :: Color -> Color
rgb2cmyk (RGB (r, g, b))
	| not((r <= 255) && (g <= 255) && (b <= 255) && (r >= 0) && (g >= 0) && (b >= 0)) = error "Values not in [0,255]"
	| (r, g, b) == (0,0,0) = CMYK (0,0,0,1)
	| otherwise = CMYK (c, m, y, k)
		where
		rgb2cmyk_aid :: Int -> Color -> Float
		rgb2cmyk_aid x (RGB color) = ((w (RGB color)) - ((int2float x) / 255)) / (w (RGB color))
		c = rgb2cmyk_aid r (RGB (r,g,b))
		m = rgb2cmyk_aid g (RGB (r,g,b))
		y = rgb2cmyk_aid b (RGB (r,g,b))
		k = 1-w (RGB (r, g, b))
		
{-
----------------------------------------------------------------------------------
	Exercise 4
----------------------------------------------------------------------------------
-}

data RootNum = RootNum (Int, Int)

instance Show RootNum
	where
	show (RootNum (a, b)) = (show a) ++ "+" ++ (show b) ++ "√2"

instance Num RootNum
	where
		(+) (RootNum (a, b)) (RootNum (c,d)) = add (RootNum (a, b)) (RootNum (c,d))

add :: RootNum -> RootNum -> RootNum
add (RootNum (x1,y1)) (RootNum (x2,y2)) = (RootNum (x1+x2, y1+y2))
sub :: RootNum -> RootNum -> RootNum
sub (RootNum (x1,y1)) (RootNum (x2,y2)) = (RootNum (x1-x2, y1-y2))
mult :: RootNum -> RootNum -> RootNum
mult (RootNum (x1,y1)) (RootNum (x2,y2)) = (RootNum (x1*x2+2*y1*y2,x1*y2+y1*x2))

getValue :: RootNum -> Double
getValue (RootNum (x,y)) = (int2double x) + sqrt(2) * (int2double y)

{-
----------------------------------------------------------------------------------
	Exercise 5
----------------------------------------------------------------------------------
-}

majority :: (Eq a) => [a] -> Maybe a
majority list = loop list
	where
	--loop :: (Eq a) => [a] -> [a] -> Int
	loop [] = Nothing
	loop (x:xs)
		| count >= (length(list) `div` 2) + 1 = Just x
		| otherwise = loop xs
			where
			(count, remList) = allOccurences x (x:xs)
		
allOccurences :: (Eq a) => a -> [a] -> (Int, [a])
allOccurences elmt list = loop list 0 []
	where 
	loop [] count remList = (count, remList)
	loop (x:xs) count remList
		| x == elmt = loop xs (count+1) remList
		| otherwise = loop xs count (x:remList)

{-
----------------------------------------------------------------------------------
	Exercise 6
----------------------------------------------------------------------------------
-}

-----Algebraic data type definition and useful function definitions:

data Nat = Zero | S Nat
	deriving Show

nadd :: Nat -> Nat -> Nat
nadd a Zero = a
nadd a (S b) = nadd (S a) b

nsucc :: Nat -> Nat
nsucc a = (S a)

lt :: Nat -> Nat -> Bool
lt Zero (S _) = True
lt (S a) (S b) = lt a b
lt _ _ = False
 
equal :: Nat -> Nat -> Bool
equal Zero Zero = True
equal (S a) (S b) = equal a b
equal _ _ = False

nat2int :: Nat -> Int
nat2int Zero = 0
nat2int (S a) = (nat2int a) + 1

int2nat :: Int -> Nat
int2nat a
	| a < 0 = error "Not a natural number!"
	| otherwise = loop Zero a
		where
		loop nat 0 = nat
		loop nat int = loop (S nat) (int-1)
		
-----Functions for exercise:

natUngerade :: Nat -> Bool
natUngerade a = loop a Zero
	where
	loop Zero Zero = False
	loop (S a) b
		| (S a) `equal` b = False
		| a `equal` b = True
		| b `lt` (S a) = loop a (S b)
	
cutSub :: Nat -> Nat -> Nat -- a - b => 0, wenn b>=a
cutSub a b = loop a b
	where
	loop a Zero = a
	loop (S a) (S b) = loop a b
	loop Zero b = Zero

natMax :: Nat -> Nat -> Nat
natMax a b
	| (a `lt` b) = b
	| otherwise = a
	
natFib :: Nat -> Nat
natFib Zero = Zero
natFib (S Zero) = (S Zero)
natFib a = loop Zero (S Zero) a
	where
	loop a b Zero = a
	loop a b (S c) = loop b (a `nadd` b) c
	
{-
----------------------------------------------------------------------------------
	Test functions
----------------------------------------------------------------------------------
-}















