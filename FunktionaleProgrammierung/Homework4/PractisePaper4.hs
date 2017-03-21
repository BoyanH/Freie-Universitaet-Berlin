{-
	Funktionale Programmierung

	Übungsblatt 3 (Abgabe: Mo., den 16.11. um 10:10 Uhr)
	Author: Boyan Hristov Matrikelnummer: 4980301
	Tutor: Zachrau, Alexander
	Tutorium: Dienstag; 12:00 - 14:00
-}


{-
----------------------------------------------------------------------------------
	Exercise 1
----------------------------------------------------------------------------------

	[(n,m) | n<-[1..3], m<-[3,2..0], n/=m] =>

	[(1,3) | 1/=3] => True, will be added to list
	[(1,2) | 1/=2] => True, will be added to list
	[(1,1) | 1/=1] => 1=1, False => Won't be added to list
	[(1,0) | 1/=0] => True, will be added to list
	[(2,3) | 2/=3] => True, will be added to list
	...
	[(3,0) | 3/=0] => True, will be added to list

	=> [(1,3), (1,2), (1,0), (2,3), (2,1), (2,0), (3,2), (3,1), (3,0)]
-}

{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}

trueDivisor :: Int -> [Int]
--as the num is not a true divisor of itself, the last one can be div num 2
--therefore a generate the list only until that number for shorter execution time
trueDivisor num = [divisor | divisor <- [1..(div num 2)], mod num divisor == 0]

{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}

allPairMults :: [Int] -> [Int]
--generate all possible pairs like in the presentation, then multiply pair members
allPairMults inputList = [a*b | a <- inputList, b <- inputList]

{-
----------------------------------------------------------------------------------
	Exercise 4
----------------------------------------------------------------------------------
-}

friends :: Int -> Int -> Bool
friends a b = sum (trueDivisor a) == b && sum (trueDivisor b) == a

{-
	get all possible combinations, where a<b<n and check if there friends
	conditions are checked one after the other, so it is iportant, that a<b
	stands before friends a b for better performance. Also, a needs to be
	from 1 to n-1 only, as it needs to be < b; b needs to be > a => b<-[a+1..n]
-}

allFriends :: Int -> [(Int, Int)]
allFriends n = [(a, b) | a <- [1..n-1], b <- [a+1..n], friends a b]

{-
----------------------------------------------------------------------------------
	Exercise 5
----------------------------------------------------------------------------------
-}

--use the primeNumbers generator from the presentation
primeNumbers :: Integer -> [Integer]
primeNumbers n = sieve [2..n]
				where
					sieve [] = []
					sieve (x:xs) = x: sieve([elem | elem <-xs, (mod elem x) /= 0])

--using the primeNumbers list until n, check for each pair if a<b and a+b = num
	--if both conditons are met, the pair is a goldbachPair and we add it to the list
goldbachPairs :: Integer -> [(Integer, Integer)]
goldbachPairs num = [(a, b) | a <- (primeNumbers num), b <- (primeNumbers num), a < b, a + b == num]

{-
----------------------------------------------------------------------------------
	Exercise 6
----------------------------------------------------------------------------------
-}

--1st argument is a function which returns a bool
myAny :: (a->Bool) -> [a] -> Bool
myAny operator [] = False --bottom of recursion, non-existing element cannot meet a condition
						--check if x meets the condition OR recursively any of the other elements in the list
myAny operator (x:xs) = (operator x) || (myAny operator xs)

{-
----------------------------------------------------------------------------------
	Exercise 7
----------------------------------------------------------------------------------
-}

--as we are going to compare the element with elemnts of list, we need the type to be comparable (Eq)
allPositionsOf :: (Eq a) => a -> [a] -> [Int]
allPositionsOf elem list = indexOf elem list 0
							where
								indexOf elem [] idx = []
								indexOf elem (x:xs) count --if the element is the searched one, add index to list
															--and continue
														| elem == x 	= count: (indexOf elem xs (count+1))
														| otherwise		= indexOf elem xs (count+1)
															--otherwise, continue without searching
														--in both cases, increment the index before checking next elem

allPositionsOfLG :: (Eq a) => a -> [a] -> [Int]
--init a list of all indexes, then to cheack if list[idx] == elem drop a-1 indexes (=a elements) 
--and check the head element of the rest is our searched one
allPositionsOfLG elem list = [a | a <- [1..(length list)-1], head (drop (a) list) == elem]

{-
----------------------------------------------------------------------------------
	Tests
----------------------------------------------------------------------------------
-}

test2 :: [Int]
test2 = trueDivisor 250

result2 :: [Int]
result2 = [1, 2, 5, 10, 25, 50, 125]


test3 :: [Int]
test3 = allPairMults [2, 3, 1]

result3 :: [Int]
result3 = [4, 6, 2, 6, 9, 3, 2, 3, 1]


test4 :: [(Int, Int)]
test4 = allFriends 300

result4 :: [(Int, Int)]
result4 = [(220,284)]


test5 :: [(Integer, Integer)]
test5 = goldbachPairs 80

result5 :: [(Integer, Integer)]
result5 = [(7,73), (13,67), (19,61), (37,43)]


test6 :: Bool
test6 = myAny (/=7) [2, 1, 7, 8, 3, 0]

result6 :: Bool
result6 = True


test7_a :: [Int]
test7_a = allPositionsOf 'n' "Funktionale Programmierung"

result7_a :: [Int]
result7_a = [2, 7, 24]

test7_b :: [Int]
test7_b = allPositionsOfLG 'n' "Funktionale Programmierung"

testAll :: IO()
testAll = do
			putStr ("Exercise 2:  trueDivisor 250 == " ++ show (result2) ++ ": ")
			putStr (show (test2 == result2) ++ "\n")

			putStr ("Exercise 3:  allPairMults [2, 3, 1] == " ++ show (result3) ++ ": ")
			putStr (show (test3 == result3) ++ "\n")

			putStr ("Exercise 4:  allFriends 300 == " ++ show (result4) ++ ": ")
			putStr (show (test4 == result4) ++ "\n")

			putStr ("Exercise 5:  goldbachPairs 80 == " ++ show (result5) ++ ": ")
			putStr (show (test5 == result5) ++ "\n")

			putStr ("Exercise 6:  myAny (/=7) [2, 1, 7, 8, 3, 0] == " ++ show (result6) ++ ": ")
			putStr (show (test6 == result6) ++ "\n")

			putStr ("Exercise 7: \n    a) allPositionsOf 'n' \"Funktionale Programmierung\" == " ++ show (result7_a) ++ ": ")
			putStr (show (test7_a == result7_a) ++ "\n")

			putStr "    b) allPositionsOfLG 'n' \"Funktionale Programmierung\" == allPositionsOf 'n' \"Funktionale Programmierung\": "
			putStr (show (test7_a == test7_b) ++ "\n")