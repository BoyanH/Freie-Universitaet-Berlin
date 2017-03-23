{-
	Funktionale Programmierung

	Übungsblatt 5 (Abgabe: Mi., den 02.12. um 10:10 Uhr)
	Author: Boyan Hristov, Luis Herrmann
	Tutor: Zachrau, Alexander
	Tutorium: Dienstag; 12:00 - 14:00
-}

{-
----------------------------------------------------------------------------------
	Imports
----------------------------------------------------------------------------------
-}

import Queue

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

	--This module is in a separate file. In this file only tests will be made

{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}

ones :: [Int]
ones = iterate (+0) 1 --each successor is formed by adding zero, basically repeats 1

naturals :: [Int]
naturals = iterate (+1) 1 --starts from 1, adding 1 to each successor

evens :: [Int]
evens = iterate (+2) 2 --starts from 2, adding 2 to each successor

twoPlusOne :: [Int]
twoPlusOne = iterate ((+1).(*2)) 0 --starts from zero, forming each successor by multiplying the previous element by 2 and adding 1

{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}

unfold :: (b -> Bool) -> (b -> a) -> (b -> b) -> b -> [a]
{--
	p: stop recursion if this condition is met
	f: apply this function on the next iteration
	g: function to change the element to be passed to the next recursion
	x: element, the result of passing it to f will be added to the array
--}
unfold p f g x 
			| p x = []
			| otherwise = f x : unfold p f g (g x) 


unfoldMap :: (Eq a) => (a -> a) -> [a] -> [a]
--end when the list is empty, execute f on first element, add it to the result list and continue with the rest
unfoldMap f = unfold (== []) (f.head) (tail) 

unfoldIterate :: (a -> a) -> a -> [a]
--never end (iterate is endless), use the same function to get the new element to be passed to the next recursion
-- and to get the element to be added to the result list, start with the given argument
unfoldIterate f = (\(elmnt) -> elmnt:(unfold returnFalse f f elmnt))
					where 
						returnFalse _ = False

--if and argument can be used in the declaration
dec2binPrototype :: Int -> [Int]
dec2binPrototype num 
		| num < 0 	= error "Cannot calculate binary value from negative numbers!"
		| num == 0 	= [0]
--stop when num is zero, each time passing div num 2 to the next recursion and adding mod num 2 to the result
--at the end the result list needs to be reversed
		| otherwise = reverse (unfold (== 0) (`mod` 2) (`div` 2) num)

--in order to be defined as in the task, without taking argument but declaring the function as equal to another
dec2bin :: Int -> [Int]															
--first check for error, if such must be thrown, if not pass the argument to the actual core function
--convert decimal to binary as studied before
--check if the result list is empty (happens by input of 0), return [0] in this case
--reverse the result list, as it is now in reversed order
dec2bin = reverse.checkZeroCase.(unfold (== 0) (`mod` 2) (`div` 2)).throwErrorIfNeeded
			where
				throwErrorIfNeeded num
									| num < 0 	= error "Cannot calculate binary value from negative numbers!"
									| otherwise = num
				checkZeroCase [] = [0]
				checkZeroCase list = list



{-
----------------------------------------------------------------------------------
	Test functions
----------------------------------------------------------------------------------
-}

-----Exercise 1
aufgabe1 1 = putStrLn("-Aufgabe 1-\n\n\
	\\n Test: enqueue makeQueue 1 => \"1: 1\" \n \t Result: \n" ++ show(enqueue makeQueue 1) ++ "\
 \\n Test: enqueue (dequeue (enqueue (enqueue (enqueue makeQueue 1) 2) 3)) 4 => \"1: 2; 2: 3; 3: 4 \" \n \t Result: \n" ++ show (enqueue (fst (dequeue (enqueue (enqueue (enqueue makeQueue 1) 2) 3))) 4) ++ "\
 \\n Test: dequeue (fst (dequeue (enqueue makeQueue 1) )) => \"Cannot dequeue an empty queue\" \t Result: " ++ show(dequeue (fst (dequeue (enqueue makeQueue 1) ))))

aufgabe1 2 = putStrLn("-Aufgabe 1-\n\n\
\\n Test: (enqueue (enqueue (enqueue makeQueue 1) 2) 3) > (enqueue (enqueue makeQueue 1) 2) => True \t Result: " ++ show((enqueue (enqueue (enqueue makeQueue 1) 2) 3) > (enqueue (enqueue makeQueue 1) 2)) ++ "\
\\n Test: (enqueue (enqueue (enqueue makeQueue 1) 2) 3) >= (enqueue (enqueue makeQueue 1) 2) => True \t Result: " ++ show((enqueue (enqueue (enqueue makeQueue 1) 2) 3) > (enqueue (enqueue makeQueue 1) 2)) ++ "\
\\n Test: (enqueue (enqueue (enqueue makeQueue 1) 2) 3) < (enqueue (enqueue makeQueue 1) 2) => False \t Result: " ++ show((enqueue (enqueue (enqueue makeQueue 1) 2) 3) < (enqueue (enqueue makeQueue 1) 2)) ++ "\
\\n Test: (enqueue (enqueue (enqueue makeQueue 1) 2) 3) <= (enqueue (enqueue makeQueue 1) 2) => False \t Result: " ++ show((enqueue (enqueue (enqueue makeQueue 1) 2) 3) <= (enqueue (enqueue makeQueue 1) 2)) ++ "\
\\n Test: (enqueue (enqueue (enqueue makeQueue 1) 2) 3) == (enqueue (enqueue makeQueue 1) 2) => False \t Result: " ++ show((enqueue (enqueue (enqueue makeQueue 1) 2) 3) == (enqueue (enqueue makeQueue 1) 2)) ++ "\
\\n Test: (enqueue (enqueue (enqueue makeQueue 1) 2) 3) == (enqueue (enqueue (enqueue makeQueue 1) 2) 4) => True \t Result: " ++ show((enqueue (enqueue (enqueue makeQueue 1) 2) 3) == (enqueue (enqueue (enqueue makeQueue 1) 2) 4)) ++ "\
\\n Test: (enqueue (enqueue (enqueue makeQueue 1) 2) 3) === (enqueue (enqueue (enqueue makeQueue 1) 2) 4) => False \t Result: " ++ show((enqueue (enqueue (enqueue makeQueue 1) 2) 3) === (enqueue (enqueue (enqueue makeQueue 1) 2) 4)) ++ "\
\\n Test: (enqueue (enqueue (enqueue makeQueue 1) 2) 3) === (enqueue (enqueue (enqueue makeQueue 1) 2) 3) => True \t Result: " ++ show((enqueue (enqueue (enqueue makeQueue 1) 2) 3) === (enqueue (enqueue (enqueue makeQueue 1) 2) 3)) ++ "\
\\n Test: (enqueue (enqueue makeQueue 1) 2) < (enqueue (enqueue (enqueue makeQueue 1) 2) 3) => True \t Result: " ++ show((enqueue (enqueue makeQueue 1) 2) < (enqueue (enqueue (enqueue makeQueue 1) 2) 3)))

aufgabe1 3 = putStrLn("-Aufgabe 1-\n\n\
\\n Test: isEmpty (fst (dequeue (enqueue makeQueue 1))) => True \t Result: " ++ show(isEmpty (fst (dequeue (enqueue makeQueue 1)))) ++ "\
\\n Test: isEmpty (enqueue makeQueue 1) => False \t \t \t Result: " ++ show(isEmpty (enqueue makeQueue 1)))

-----Exercise 2
aufgabe2 _ = putStrLn("-Aufgabe 1-\n\n\
\\n Test: take 10 ones => [1,1,1,1,1,1,1,1,1,1] \t Result: " ++ show(take 10 ones) ++ "\
\\n Test: take 10 naturals => [1,2,3,4,5,6,7,8,9,10] \t Result: " ++ show(take 10 naturals) ++ "\
\\n Test: take 10 evens => [2,4,6,8,10,12,14,16,18,20] \t Result: " ++ show(take 10 evens) ++ "\
\\n Test: take 10 twoPlusOne => [0,1,3,7,15,31,63,127,255,511] \t \t \t Result: " ++ show(take 10 twoPlusOne))

-----Exercise 3
aufgabe3 _ = putStrLn("-Aufgabe 1-\n\n\
\\n Test: unfoldMap (*2) [1..10] => [2,4,6,8,10,12,14,16,18,20] \t \t \t Result: " ++ show(unfoldMap (*2) [1..10]) ++ "\
\\n Test: take 10 (unfoldIterate (+1) 1) => [1,2,3,4,5,6,7,8,9,10] \t \t Result: " ++ show(take 10 (unfoldIterate (+1) 1)) ++ "\
\\n Test: dec2bin 0 => [0] \t \t \t \t \t \t \t Result: " ++ show(dec2bin 0) ++ "\
\\n Test: dec2bin (-3) => \"Cannot calculate binary value from negative numbers!\" \t Result: " ++ show(dec2bin (-3)) ++ "\
\\n Test: dec2bin 17 => [1,0,0,0,1] \t \t \t Result: " ++ show(dec2bin 17))




