{-
	Funktionale Programmierung

	Übungsblatt 5 (Abgabe: Mi., den 02.12. um 10:10 Uhr)
	Author: Boyan Hristov, Luis Herrmann
	Tutor: Zachrau, Alexander
	Tutorium: Dienstag; 12:00 - 14:00
-}

{-
----------------------------------------------------------------------------------
	Globally required functions
----------------------------------------------------------------------------------
-}

qReverse xs = loop xs [] --a simple quick reverse function
	where
	loop [] ys = ys
	loop (x:xs) ys = loop xs (x:ys)
	
int2float :: Int -> Float --for quicker use, call fromIntegral on x and casts it to Float
int2float x = fromIntegral x :: Float

int2double :: Int -> Double
int2double x = fromIntegral x :: Double

{-
----------------------------------------------------------------------------------
	Exercise 1
----------------------------------------------------------------------------------
-}

abflachen :: [[a]] -> [a]
abflachen list = join list []
	where
	join [] retList = qReverse(retList)
	join (x:xs) retList = join xs (foldl (flip (:)) retList x)
	
abflachen_ :: [[a]] -> [a]
abflachen_ list = join (qReverse list) []
	where
	join [] retList = retList
	join (x:xs) retList = join xs (foldr (:) retList x)

{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}

toDecFrom :: [Int] -> Int -> Int
toDecFrom xs base
				| base <= 1 || base >= 10 = error "The base must be between 1 and 9!"
				| xs == [] = error "The list must contain at least one element!"
				| otherwise = foldl addDigit 0 xs
								where
									addDigit x y 
												| (y >= base) || (y < 0) = error "Number is not of specified base"
												| otherwise = (x*base) + y

{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}

data Color = RGB (Int, Int, Int) | CMYK (Float, Float, Float, Float)
								deriving (Eq, Ord, Show)

getRGBW :: Color -> Float --for cleaner code, return w of rgb2cmyk function (maximum of all values divided by 255)
getRGBW (RGB (r,g,b)) = maximum (map (/255) (map int2float [r,g,b]))

isRGBValid :: (Int, Int, Int) -> Bool --checks if each value in rgb is in [0,255]
isRGBValid (r,g,b) = isValid r && isValid g && isValid b --r is in [0,255] AND g is in ...
					where
						isValid x = x >= 0 && x <= 255

--using the formula from a previous homework, refactored with current knowledge and using algebraic data types
rgb2cmyk :: Color -> Color
rgb2cmyk (RGB (0, 0, 0)) = CMYK (0, 0, 0, 1) --default case
							--if rgb is not valid, throw an error
rgb2cmyk (RGB (r, g, b))| not (isRGBValid (r,g,b)) = error "RGB color is not valid! Values not in [0, 255]!"
						| otherwise = CMYK (c, m, y, k) --return all calculated values of CMYK as tuple
							where
							 	w = getRGBW (RGB (r,g,b))
							 	c = rgb2cmyk_aid r
							 	m = rgb2cmyk_aid g
							  	y = rgb2cmyk_aid b
							 	k = 1 - w

								rgb2cmyk_aid :: Int -> Float
								rgb2cmyk_aid x = (w - ((int2float x) / 255)) / w
{-
----------------------------------------------------------------------------------
	Exercise 4
----------------------------------------------------------------------------------
-}

--define the algebraic data type as a touple of two integers, a + b√2 as in task
data RootNum = RootNum (Int, Int) 

--defining RootNum as instance of Num, only defining the operators which were required in the task 
--using the functions below
instance Num RootNum where
	x-y = rnSub x y
	x+y = rnAdd x y
	x*y = rnMult x y

--defining RootNum as instance of Show
instance Show RootNum where
	--displaying a tuple as a + b√2
	show (RootNum (a,b))
		| a == 0 = (show b) ++ "√2"
		| b == 0 = (show a)
		| b < 0 = (show a) ++ (show b) ++ "√2" --for aesthetic reasons, avoids display of type a+-b√2
		| otherwise = (show a) ++ "+" ++ (show b) ++ "√2"

--sum two RootNum elements
rnAdd :: RootNum -> RootNum -> RootNum
rnAdd (RootNum (a1, b1)) (RootNum (a2, b2)) = RootNum (resultA, resultB)
												where
													resultA = a1 + a2 --add the separate number parts
													resultB = b1 + b2 --add b+b' in b√2 + b'√2 = √2(b+b') = (b+b')√2

--subtract two RootNum elements
rnSub :: RootNum -> RootNum -> RootNum
rnSub (RootNum (a1, b1)) (RootNum (a2, b2)) = RootNum (resultA, resultB)
												where
													resultA = a1 - a2 --as above, subtract the non-√2-multiplied parts
													resultB = b1 - b2 --as above b√2 - b'√2 = √2(b-b') = (b-b')√2

--multiply two RootNum elements
rnMult :: RootNum -> RootNum -> RootNum
rnMult (RootNum (a1, b1)) (RootNum (a2, b2)) = RootNum (resultA, resultB)
												where 
--(a + b√2)*(a' + b'√2) = a*a' + a*b'√2 + b√2*a' + b√2*b'√2 = (a*a' + b*b'*2) + √2(a*b' + a'*b) = (a1*a2 + b1*b2*2) + (a1*b2 + a2*b1)√2
													resultA = (a1*a2) + (b1*b2*2) 
													resultB = (a1*b2) + (a2*b1)

getValue :: RootNum -> Double
--convert a and b to double, in order to be able to return a double. Simply a+b√2 => a+ (b* (sqrt 2))
getValue (RootNum (a, b)) = (int2double a) + ( (int2double b) * (sqrt 2) )


{-
----------------------------------------------------------------------------------
	Exercise 5
----------------------------------------------------------------------------------
-}

majority :: (Eq a) => [a] -> Maybe a
majority list = getMajority list 0
	where
	--getMajority :: (Eq a) => [a] -> [a] -> Int
	getMajority [] brkcount = Nothing --bottom of recursion. If no majority exists, return Nothing
	getMajority (x:xs) brkcount
			--check if the number of occurences in the last counted element is enough for a majority
					| brkcount >= (listLen `div` 2) + 1 = Nothing --If no majority group has been found after first (n/2)+1 elements, there can be none.
					| count >= (listLen `div` 2) + 1 = Just x --in this case, return Just and the element, as specified in the task
					| otherwise = getMajority remList (brkcount+count) --otherwise, keep counting each element from the list of elements, which have not been counted yet
						where
						(count, remList) = allOccurences x (x:xs) --returns a tuple with the occurences of element x and all elements /= x

	listLen = length list --define a function to return the length of the list, so it doesn't need to be calculated on every getMajority recursion
		
allOccurences :: (Eq a) => a -> [a] -> (Int, [a])
allOccurences elmt list = countReps list 0 [] --call the helper function
	where 
	countReps [] count remList = (count, remList) --if we counted all elements in the list, break recursion and return
	countReps (x:xs) count remList															-- the occurences and not-counted elements in a tuple
								| x == elmt = countReps xs (count+1) remList --if the crnt element is equal to the counted, it is an occurence, count it
								| otherwise = countReps xs count (x:remList) --otherwise, push it to the list with elements, that have not yet been counted


{-
----------------------------------------------------------------------------------
	Exercise 6
----------------------------------------------------------------------------------
-}

--define the algebraic data type for natural number as zero or successor of a natural number
data Nat = Zero | S Nat
			deriving (Show, Eq) --use default Show and Eq instances

--defining Nat as instance of Ord, only defining the operators, which were required in the task 
--using the functions below
instance Ord Nat where
	a < b 	= lt a b
	a <= b 	= (a == b) || a < b
	a > b 	= not (a <= b)
	a >= b 	= not (a < b)

--same as above for Num
instance Num Nat where
	a - b = cutSub a b
	a + b = addNat a b

nsucc :: Nat -> Nat
nsucc a = (S a) -- return the successor of a natural number

lt :: Nat -> Nat -> Bool
lt Zero (S _) = True --0 is smaller than any other natural number
lt (S a) (S b) = lt a b --if both numbers are successors of a natural number (both not 0), then decrement both
lt _ _ = False --lt (S _) Zero = False, because there is no natural number < 0

equal :: Nat -> Nat -> Bool
equal Zero Zero = True -- 0 = 0
equal (S a) (S b) = equal a b --if both are >0, decrement both until a Zero is reached. If both are 0, then True, else False
equal _ _ = False --Zero is not equal to any other natural number

nat2int :: Nat -> Int
nat2int Zero = 0 --bottom of recursion, by Zero add nothing, break recursion
nat2int (S a) = (nat2int a) + 1 --otherwise, decrement the natural number and increment the integer untill Zero is reached

int2nat :: Int -> Nat
int2nat a
	| a < 0 	= error "Not a natural number!" --natural numbers are only positive. If input is not, throw an error
	| otherwise = toNat Zero a --otherwise call the helper function
		where
		toNat nat 0 = nat --bottom of recursion, return the natural number
		toNat nat int = toNat (S nat) (int-1) --increment the natural nuber and decrement the integer, untill integer is 0

natUngerade :: Nat -> Bool
natUngerade Zero = False --if at the bottom of the recursion 0 is left after n subtractions of 2, then number `mod` 2 = 0 => even => False
natUngerade (S (Zero)) = True --otherwise, at the bottom of the recursion 1 is left => number `mod` 2 = 1 => True
natUngerade (S (S n)) = natUngerade n --if the natural number is >= (S (S n)) >= 2, then decrement it by 2 

cutSub :: Nat -> Nat -> Nat --if b >= a then a - b = 0 
cutSub a Zero = a --if subtraction has finished, return a // (a-0 = a)
cutSub Zero b = Zero --Zero is the smallest natural number => a <= b  => as per task definition, return Zero
cutSub (S a) (S b) = cutSub a b --decrement both natural numbers // (x-y = (x-1) - (y-1))

addNat :: Nat -> Nat -> Nat
addNat a Zero = a -- a + 0 = 0, bottom of recursion, return a
addNat a (S b) = addNat (S a) b -- a + b = (a+1) + (b-1)

natMax :: Nat -> Nat -> Nat
natMax a b --using the lt function above
		| a < b 	= b --return b if b>a
		| otherwise = a --return a if a>=b

natFib :: Nat -> Nat
natFib Zero = Zero --fib(0) = 0 per definition
natFib (S Zero) = (S Zero) --fib(1) = 1 per definition //1 is the successor of 0 => S Zero
natFib a = quickFib Zero (S Zero) a --for all other fib(x) call the helper function with last argument the input or the number of cycles required
	where
	quickFib a b Zero = a --if there are no more cycles left, return a, which is the n-th fibonacci number for input n
	quickFib a b (S c) = quickFib b (a + b) c -- as we studied in class. temp = a; a=b; b = temp + b; cycles--
	
{-
----------------------------------------------------------------------------------
	Test functions
----------------------------------------------------------------------------------
-}

-----Exercise 1

aufgabe1 _ = putStrLn("-Aufgabe 1-\n\n\
	\\n Test: abflachen [\"abc\", \"bcd\", \"abc\"] => \"abcbcdabc\"\t Result: " ++ show(abflachen ["abc", "bcd", "abc"]) ++ "\
	\\n Test: abflachen [[1,2,3], [3,4,1], [1,2,3]] => [1,2,3,3,4,1,1,2,3]\t Result: " ++ show(abflachen [[1,2,3], [3,4,1], [1,2,3]]) ++ "\
	\\n Test: abflachen [[1,2,3]] => [1,2,3]\t Result: " ++ show(abflachen [[1,2,3]]) ++ "\
	\\n Test: abflachen_ [\"abc\", \"bcd\", \"abc\"] => \"abcbcdabc\"\t Result: " ++ show(abflachen ["abc", "bcd", "abc"]) ++ "\
	\\n Test: abflachen_ [[1,2,3], [3,4,1], [1,2,3]] => [1,2,3,3,4,1,1,2,3]\t Result: " ++ show(abflachen [[1,2,3], [3,4,1], [1,2,3]]) ++ "\
	\\n Test: abflachen_ [[1,2,3]] => [1,2,3]\t Result: " ++ show(abflachen [[1,2,3]]))
	
	{-\\t Test: abflachen [] => []\t Result: " ++ show(abflachen []) ++ "\
	\\t Test: abflachen [[],[]] => []\t Result: " ++ show(abflachen [[],[]]) ++ "\-}
	
-----Exercise 2
	
aufgabe2 1 = putStrLn("\
 \ Test: toDecFrom [1,3,2] 4 => 30\t Result: " ++ show(toDecFrom [1,3,2] 4) ++ "\
 \\n Test: toDecFrom [1,1,1,1,0] 2 => 30\t Result: " ++ show(toDecFrom [1,1,1,1,0] 2))
aufgabe2 2 = putStrLn("Test: toDecFrom [1,3,2] (-2) => ERROR\t Result: " ++ show(toDecFrom [1,3,2] (-2)))
aufgabe2 3 = putStrLn("Test: toDecFrom [1,3,2] 10 => ERROR\t Result: " ++ show(toDecFrom [1,3,2] 10))
aufgabe2 4 = putStrLn("Test: toDecFrom [1,3,2] 2 => ERROR\t Result: " ++ show(toDecFrom [1,3,2] 2))
aufgabe2 5 = putStrLn("Test: toDecFrom [] 2 => ERROR\t Result: " ++ show(toDecFrom [] 2))
aufgabe2 _ = putStrLn("-Aufgabe 1c-\n Tests 1 through 5. USE: aufgabe2 [testid]")

-----Exercise 3

aufgabe3 1 = putStrLn("Test: rgb2cmyk (RGB (210,21,34)) => CMYK (0.0,0.90000004,0.83809525,0.17647058)\t Result: " ++ show(rgb2cmyk (RGB (210,21,34))))
aufgabe3 2 = putStrLn("Test: rgb2cmyk (RGB (270,21,34)) => ERROR\t Result: " ++ show(rgb2cmyk (RGB (270,21,34))))
aufgabe3 _ = putStrLn("-Aufgabe 1c-\n Tests 1 through 2. USE: aufgabe3 [testid]")

-----Exercise 4

aufgabe4 _ = putStrLn("-Aufgabe 4-\n\n\
	\\n Test: (RootNum (1,2)) + (RootNum (2,3)) => 3+5√2\t Result: " ++ show((RootNum (1,2)) + (RootNum (2,3))) ++ "\
	\\n Test: (RootNum (0,2)) + (RootNum (1,0)) => 1+2√2\t Result: " ++ show((RootNum (0,2)) + (RootNum (1,0))) ++ "\
	\\n Test: (RootNum (2,2)) - (RootNum (1,3)) => 1-1√2\t Result: " ++ show((RootNum (2,2)) - (RootNum (1,3))) ++ "\
	\\n Test: (RootNum (0,-1)) - (RootNum (1,0)) => -1-1√2\t Result: " ++ show((RootNum (0,-1)) - (RootNum (1,0))) ++ "\
	\\n Test: (RootNum (0,1)) * (RootNum (1,0)) => 1√2\t Result: " ++ show((RootNum (0,1)) * (RootNum (1,0))) ++ "\
	\\n Test: (RootNum (3,2)) * (RootNum (2,1))) => 10+7√2\t Result: " ++ show((RootNum (3,2)) * (RootNum (2,1))) ++ "\
	\\n Test: getValue (RootNum (0,1)) => 1.4142135623730951\t Result: " ++ show(getValue((RootNum (0,1)))) ++ "\
	\\n Test: getValue ((RootNum (3,2)) * (RootNum (2,1))) => 19.899494936611667\t Result: " ++ show(getValue((RootNum (3,2)) * (RootNum (2,1)))))
	
aufgabe5 _ = putStrLn("-Aufgabe 5-\n\n\
	\\n Test: majority [1] => Just 1\t Result: " ++ show(majority [1]) ++ "\
	\\n Test: majority [1,1,2,2] => Nothing\t Result:" ++ show(majority [1,1,2,2]) ++ "\
	\\n Test: majority [1,1,2,2,2] => Just 2\t Result:" ++ show(majority [1,1,2,2,2]))
	
aufgabe6 _ = putStrLn("-Aufgabe 6-\n\n\
	\\n Test: natUngerade Zero => False\t Result: " ++ show(natUngerade Zero) ++ "\
	\\n Test: natUngerade (S Zero) => True\t Result: " ++ show(natUngerade (S Zero)) ++ "\
	\\n Test: natUngerade (int2nat 7) => True\t Result: " ++ show(natUngerade (int2nat 7)) ++ "\
	\\n Test: cutSub Zero Zero => Zero\t Result: " ++ show(cutSub Zero Zero) ++ "\
	\\n Test: cutSub (S Zero) Zero => (S Zero)\t Result: " ++ show(cutSub (S Zero) Zero) ++ "\
	\\n Test: cutSub Zero (S Zero) => Zero\t Result: " ++ show(cutSub Zero (S Zero)) ++ "\
	\\n Test: nat2int(cutSub (int2nat 7) (int2nat 3)) => 4\t Result: " ++ show(nat2int(cutSub (int2nat 7) (int2nat 3))) ++ "\
	\\n Test: cutSub (int2nat 3) (int2nat 7) => Zero\t Result: " ++ show(cutSub (int2nat 3) (int2nat 7)) ++ "\
	\\n Test: nat2int(natMax (int2nat 3) (int2nat 7)) => 7\t Result: " ++ show(nat2int(natMax (int2nat 3) (int2nat 7))) ++ "\
	\\n Test: nat2int(natMax (int2nat 3) (int2nat 3)) => 3\t Result: " ++ show(nat2int(natMax (int2nat 3) (int2nat 3))) ++ "\
	\\n Test: natMax (S (S Zero)) Zero => (S (S Zero))\t Result: " ++ show(natMax (S (S Zero)) Zero) ++ "\
	\\n Test: natFib Zero => Zero\t Result: " ++ show(natFib Zero) ++ "\
	\\n Test: natFib (S Zero) => (S Zero)\t Result: " ++ show(natFib (S Zero)) ++ "\
	\\n Test: natFib (S (S (S Zero))) => (S (S Zero))\t Result: " ++ show(natFib (S (S (S Zero)))) ++ "\
	\\n Test: nat2int(natFib(int2nat 9)) => 34\t Result: " ++ show(nat2int(natFib(int2nat 9))))
