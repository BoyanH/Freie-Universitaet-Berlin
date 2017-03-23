{-
	Funktionale Programmierung

	Übungsblatt 3 (Abgabe: Mo., den 9.11. um 10:10 Uhr)
	Author: Boyan Hristov Matrikelnummer: 4980301
	Tutor: Zachrau, Alexander
	Tutorium: Dienstag; 12:00 - 14:00
-}


{-
----------------------------------------------------------------------------------
	Exercise 1
----------------------------------------------------------------------------------
-}

throwBaseError = error "Please enter a base of at least 2 and less than 10!"

fromDecTo :: Int -> Int -> [Int]
fromDecTo num base 
					| base < 2 || base > 10 = throwBaseError
					| num < base 			= [num] --bottom of recursion, instead of fromDecTo 0 base ++ [num] => [] ++ [num]
											--add the rest at the end and call the function with the whole number devision
					| otherwise				= fromDecTo (num `div` base) base ++ [num `mod` base]


toDecFrom :: Int -> [Int] -> Int
toDecFrom baseN numList
					| baseN < 2 || baseN > 10	= throwBaseError
					| otherwise 				= toDecFromBase baseN  (reverse numList)
						where
							toDecFromBase base [] 		= 0 --if there are no more digits, add zero (break recursion)
														
														--otherwise, multiply the base by the function with the rest of
														--the numbers and add x; This way the multiplications stack and
														-- for every next digit we have base^n+1
							toDecFromBase base (x:xs) 	= base * (toDecFromBase base xs) + x

toDecFromN :: Int -> [Int] -> Int
toDecFromN baseN numList
					| baseN < 2 || baseN > 10	= throwBaseError
					| otherwise 				= toDecFromBase baseN  numList
						where
							toDecFromBase base [] 		= 0
													{-	same principle as previous one, but this time we don't reverse the
														list, so we need to use the length of it, to determine the power of the
														base. I use the length of the remaining, as for the lengt of
														all elements of the list, the first digit is equal to digit*base^n-1 -}
							toDecFromBase base (x:xs) 	= base^(length xs) * x + toDecFromBase base xs
{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}

{-
	I reverse all bits and then reverse the array, so I can work on it from right to left
	Then I can change 1 bits to 0, untill I come to a 0 bit, which I change to 1 and finish the addBit function
	Then I reverse it again to display the result properly
-}

twoComplement :: [Int] -> [Int]
twoComplement bitList = reverse (addBit (reverse (reverseBits bitList)))
						where
							reverseBits []		= []
							reverseBits (x:xs) 	= [revBit x] ++ reverseBits xs
							revBit 	bit 		= bit - 2*bit + 1 --the not function from the previous homework
							addBit [] = [] -- if another bit must be added, it should be in overflow anyways, so don't
																									-- add anything	
							addBit (x:xs)
										| x == 0 = [1] ++ xs --adding a bit to ...xxx0 = ...xxx1
										| x == 1  = [0] ++ addBit xs --adding a bit to ...xxx01 = ...xxx10

{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}

{-
	I decided I will remove every non-bracket character from the text first and
	then use the given function. I could also just skip the non-bracket symbols
	in the balanced function, but then I would need to use the bigger pattern
	matching, that inlcudes every bracket, otherwise cases like 
	bal stapel ('a':xs) will match the pattern  bal (s:stapel) (x:xs)
	and give wrong results.
-}
trimText :: [Char] -> [Char]
trimText [] = [] --if there are no more elements to trim, end the recursive function
trimText (x:xs) 
				--if element is a bracket, add it to the result and trim the rest
				| elem x ['{', '}', '(', ')', '[', ']'] = [x] ++ (trimText xs)
				--otherwise, skip it and trim the rest
				| otherwise 							= trimText xs

balanced:: [Char] -> Bool
balanced text = bal [] (trimText text)
				where
					bal:: [Char] -> [Char] -> Bool
					bal [] [] = True
					bal stapel ('(':xs) = bal (')':stapel) xs
					bal stapel ('[':xs) = bal (']':stapel) xs
					bal stapel ('{':xs) = bal ('}':stapel) xs
					bal (s:stapel) (x:xs) 	| s==x = bal stapel xs
					bal _ _ = False

{-
----------------------------------------------------------------------------------
	Exercise 4
----------------------------------------------------------------------------------
-}

deleteReps :: [Int] -> [Int]
deleteReps intList = quickTrim [] intList
						where
							--if there are no more elements, break the recursive function
							quickTrim stack [] = []
							quickTrim stack (x:xs)
													--if the element exists in the stack, miss it and call the function
													--with the remaining list
													| elem x stack	=  quickTrim stack xs
													--otherwise, add the new element and call the function
													--with the remaining list
													| otherwise 	= [x] ++ (quickTrim (x:stack) xs)

{-
----------------------------------------------------------------------------------
	Exercise 5
----------------------------------------------------------------------------------
-}

chars2words :: [Char] -> [[Char]] 
--words are added to the beginning of the words list, so the list needs to be reversed to get all words in the correct order
chars2words sentance = reverse (separateWords sentance [[]]) --the empty list of lists is for list of words, which will be added
						where
							--if the sentance is finished, return the words
							separateWords [] words = words
							separateWords (x:xs) (y:ys)
														--if a new word begins, add an empty word to the words list
														| elem x [',', '.', '!', '?', ' '] 	= separateWords xs ([]:(y:ys))
														--otherwise, add the new letter to the end of the current word
														| otherwise 						= separateWords xs ((y++[x]):ys)

{-
----------------------------------------------------------------------------------
	Exercise 6
----------------------------------------------------------------------------------
-}

random :: Int -> Int
random seed = (25173*seed + 13849) `mod` 65536

withoutReps :: Int -> Int
-- call the recursive function with the first random number, as it is unique for sure
withoutReps seed = getNoRepCount [random seed]
					where
						getNoRepCount prevList 
												--if the new random number already exists in the list, then we have a repetition
												--in this case, abort the function and return the number of all previous elements
												| elem newRand prevList = length prevList
												--otherwise, add the new random number to the list of previous numbers
													--and continue the recursive function
												| otherwise 			= getNoRepCount (newRand:prevList)
													where
														--always get the new random number with the previous one as seed
														newRand = random (head prevList)

--withoutReps 7654322 => 65536 			it works slow, as it get 65536 new randoms and saves them to a list, but it works

{-
----------------------------------------------------------------------------------
	Exercise 7
----------------------------------------------------------------------------------
-}

--convert a number between 0 and 15 in hexadecimal char
toHexChar :: Int -> Char
toHexChar n	
		--if it is a decimal digit, take 'n'
		| n < 10 && n >= 0 	= head (show n)
		| n == 10			= 'A'
		| n == 11			= 'B'
		| n == 12 			= 'C'
		| n == 13 			= 'D'
		| n == 14 			= 'E'
		| n == 15			= 'F'
		| otherwise 		= error "Not a hex digit!"

--input is a [Char], therefore I need to convert chars to digits
--as the input is in octadecimal, I do this only for 0 <=digit<8
charToOctDigit :: Char -> Int
charToOctDigit dig 
				| dig == '0' 	= 0
				| dig == '1' 	= 1
				| dig == '2' 	= 2
				| dig == '3' 	= 3
				| dig == '4' 	= 4
				| dig == '5' 	= 5
				| dig == '6' 	= 6
				| dig == '7' 	= 7
				| otherwise		= error "Not all of the digits are in octadecimal!"

--In the usage example input and output were strings
okt2hex :: [Char] -> [Char]
--the easiest way to convert a number from octadecimal to hexadecimal is to convert it to binary first
okt2hex charList = bin2hex (oct2bin charList)
					where
						--convert every octadecimal digit to 3 binary digits, as 8 = 2^3
						oct2bin [] = []
						oct2bin (x:xs) = (octDigitToBin (charToOctDigit x) 3) ++ oct2bin xs

						--convert each 4 binary digits to 1 hexadecimal, as 16 = 2^4
						bin2hex [] = []
						bin2hex binList  {-	As we are going to convert each 4 binary digits to 1 hexadecimal, we make sure
						                 	the binary list can be divided into groups of 4. Otherwise, fill with 0 on the left
						                 	as they don't change the number
						                 -}
									| not ((length binList) `mod` 4 == 0) 	= bin2hex (0:binList)
									| otherwise 							= [binQuadToHex (take 4 binList)] ++ bin2hex (drop 4 binList)

						{-
							As 1 octadecimal digit is equal to exactly 3 in binary, we make sure we put 3 binary digits 
							each time, therefore we need that counter. Otherwise, if the bottom of the recursion 
							was digit==0, then in case of octDigitToBin 0 we would get []. And 110 000 is different 
							from 110 for example
						-}
						octDigitToBin digit 0 = []
						octDigitToBin digit counter = (octDigitToBin (digit `div` 2) (counter - 1)) ++ [digit `mod` 2]

						{-
							From XX...XXX(16) => X*16^n + X*16^(n-1) + ... + X*16^3...
							We need to count how many digits we have, so we know by 2 on which power we must multiply 
							the given binary digit

							Then, we are also converting the result to e hex char; For example from 15 to 'F'
						-}
						binQuadToHex binQuad = toHexChar (hexNum binQuad 4)
												where
													hexNum binQ 0 = 0
													hexNum (x:xs) digits = x*(2^(digits-1)) + hexNum xs (digits-1) 


{-
----------------------------------------------------------------------------------
	Exercise 8 - Tests
----------------------------------------------------------------------------------
-}

test1_1 :: [Int]
test1_1 = fromDecTo 30 4

test1_2 :: Int
test1_2 = toDecFrom 4 [1,3,2]

test1_3 :: Int
test1_3 = toDecFromN 4 [1,3,2]

test2 :: [Int]
test2 = twoComplement [0,0,0,1,1,0,1,0]

test3_1 :: Bool
test3_1 = balanced "[(a+b+c)*[x-y]]/{(x+1)**0}"

test3_2 :: Bool
test3_2 = balanced "[(a+b+c)*x-y)/{(x+1)*5}"

test4 :: [Int]
test4 = deleteReps [1,2,1,2,2,1,3,0] 

test5 :: [[Char]]
test5 = chars2words "Word,Another.One?Like!So Good"

test6 :: Int
test6 = withoutReps 54384

test7 :: [Char]
test7 = okt2hex "0770"

testAll :: IO()
testAll = do
			putStr "Exercise 1: \n a) fromDecTo 30 4 == [1,3,2]: "
			putStr (show (test1_1 == [1, 3, 2]) ++ "\n")

			putStr " b) toDecFrom 4 [1,3,2] == 30: "
			putStr (show (test1_2 == 30) ++ "\n")

			putStr " b) toDecFrom 4 [1,3,2] == toDecFromN 4 [1,3,2]: "
			putStr (show (test1_2 == test1_3) ++ "\n")


			putStr "Exercise 2: twoComplement [0,0,0,1,1,0,1,0] == [1,1,1,0,0,1,1,0]: "
			putStr (show (test2 == [1,1,1,0,0,1,1,0]) ++ "\n")


			putStr "Exercise 3: balanced \"[(a+b+c)*[x-y]]/{(x+1)**0}\" == True : "
			putStr (show test3_1 ++ "\n")
			putStr "            balanced \"[(a+b+c)*x-y)/{(x+1)*5}\" == False : "
			putStr (show (test3_2 == False) ++ "\n")

			putStr "Exercise 4: deleteReps [1,2,1,2,2,1,3,0] == [1,2,3,0] : "
			putStr (show (test4 == [1,2,3,0]) ++ "\n")

			putStr "Exercise 5: chars2words \"Word,Another.One?Like!So Good\" == [Word,Another,One,Like,So,Good] : "
			putStr (show (test5 == ["Word","Another","One","Like","So","Good"]) ++ "\n")

			putStr "Exercise 7: okt2hex \"0770\" == \"1F8\": "
			putStr (show (test7 == "1F8") ++ "\n")

			putStr "Exercise 6: withoutReps 2384 (It may take a while, but it works): "
			putStr (show test6 ++ "\n")
			putStr "            The result is always >(o.O)> 65536, which is 2^16. Great function! \n"