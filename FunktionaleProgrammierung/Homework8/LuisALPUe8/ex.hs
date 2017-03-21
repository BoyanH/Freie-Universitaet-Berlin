import Queue

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

{-
----------------------------------------------------------------------------------
	Exercise 1
----------------------------------------------------------------------------------
-}

	
{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}

ones :: a -> [Int]
ones x = iterate (0+) 1

naturals :: a -> [Int]
naturals x = iterate (1+) 1

evens :: a -> [Int]
evens x = iterate (2+) 2

twoPlusOne :: a -> [Int]
twoPlusOne x = iterate ((1+).(2*)) 0

{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}

unfold p f g x 
	| p x = []
	| otherwise = f x : unfold p f g (g x)
	
map_ :: Eq a => (a -> a) -> [a] -> [a]
map_ f x = unfold (==[]) (f.head) (tail) x

iterate_ :: Eq a => (a -> a) -> a -> [a]
iterate_ f x = unfold (contra) f f x
	where
	contra :: a -> Bool
	contra x = False

dec2bin_ :: Int -> [Int]
dec2bin_ x = qReverse (unfold (==0) (`mod` 2) (`div` 2) x)

