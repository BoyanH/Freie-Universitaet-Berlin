{-
	Funktionale Programmierung

	Übungsblatt 5 (Abgabe: Mi., den 25.11. um 10:10 Uhr)
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
	loop [] ys = ys --return the accumulator
	loop (x:xs) ys = loop xs (x:ys) --append the next element at the beginning
 
{-
----------------------------------------------------------------------------------
	Exercise 1
----------------------------------------------------------------------------------
-}

selectSort :: (Ord a) => (a -> a -> Bool) -> [a] -> [a]
selectSort f [] = []
selectSort f list = loop list []
	where --as the : operator is quicker than ++, we sort the List in the opposite order and then reverse it
	loop [first] ordList = qReverse (first:ordList) --if there is only one element to order, it should be last, as it was never "first"
	loop remList ordList = loop (deleteElem first remList) (first:ordList) --remove the sorted element from the list of to be sorted elements, place crnt First
			where
			first = calculateFirst f remList
	
calculateFirst :: (Ord a) => (a -> a -> Bool) -> [a] -> a
calculateFirst f (x:xs) = loop x xs
	where
	loop crntFirst [] = crntFirst --if there are no more elements, the crntFirst is our first element
	loop crntFirst (x:xs)
		| (f x crntFirst) = loop x xs --if x is (<=) / (>=) than the crntFirst, it is the new crntFirst
		| otherwise = loop crntFirst xs --otherwise, call the function recursive for the rest of the list
  
--First implementation
deleteElem :: (Ord a) => a -> [a] -> [a]
deleteElem elmnt [] = [] --if the element is not found anywhere in the list, abort function
deleteElem elmnt (x:xs) 
	| elmnt == x 	= xs --if the element is the one to be deleted, return the rest of the list
	| otherwise  	= x:(deleteElem elmnt xs) --otherwise, append it at the beginning of the recursive deleting

--Different implementation, deletes multiple elements
deleteElem_ :: (Ord a) => a -> [a] -> [a]
deleteElem_ elem list = [ x | x <- list, x /= elem]

{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}
		
isSorted :: (Ord a) => (a -> a -> Bool) -> [a] -> Bool
isSorted f [] = True
isSorted f list = loop list
	where
	loop (x1:[]) = True --a list with one element is always sorted
	loop (x1:x2:xs)
		| not(f x1 x2) = False --if the condition is not met for any x, x+1, then the list is not sorted
		| otherwise = loop (x2:xs) --if x, x+1 are sorted, continue checking
		
isSorted_ :: (Ord a) => (a -> a -> Bool) -> [a] -> Bool
isSorted_ f xs = and (zipWith f xs (tail xs)) --replaces all (x,x+1) elements in the list with f x x+1
												--then check if f x x+1 is correct for all pairs

{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}

---------------------------------------
-----(a) 

-----Help functions

-----Modfied allSuffixes to work with mergeSort:
allSuffixes :: (Ord a) => [a] -> [[[a]]]
allSuffixes [] = []
allSuffixes string = loop string [[string]]
	where
	--loop :: [a] -> [[[a]]] -> [[[a]]]
	loop (x:[]) suffixes = suffixes --if there are no more tail elements, then all suffixes are already added, break
	loop (x:xs) suffixes = loop xs ([xs]:suffixes) --remove the first element and add the rest to the result
	
prefix :: (Ord a) => [a] -> [a] -> [a]
prefix string1 string2 = loop [] string1 string2
	where
	--loop :: [a] -> [a] -> [a] -> [a]
	loop prefix [] _ = qReverse prefix -- if one of the words\lists has ended, return the result
	loop prefix _ [] = qReverse prefix -- 				--||--
	loop prefix (x:xs) (y:ys)
		| (x == y) = loop (x:prefix) xs ys --if the crnt element/char is equal, add it to the result and continue
		| otherwise = qReverse prefix  		--else, the prefixes are no longer equal from that element on, return result
  
--If multiple prefixes of same length exist, will return first
largestPrefix :: (Ord a) => [[a]] -> (Int, [a])
largestPrefix list = loop (0, []) list
	where 
	loop lPrefix [] = lPrefix
	loop lPrefix [x] = lPrefix --if there are no more pairs to check, return the largest prefix
	loop lPrefix (x:y:xs)
		| fst lPrefix < length currPrefix = loop (length currPrefix, currPrefix) (y:xs) --if the current prefix is larger than lPrefix, set latter to current
		| otherwise = loop lPrefix (y:xs) --otherwise, continue with the same prefix
			where
			currPrefix = prefix x y --take the new equal prefix from another pair


---------------------------------------
-----(b)

-----MergeSort implementation from lecture
merge :: (Ord a) => [a] -> [a] -> [a]
merge [] ys = ys
merge xs [] = xs
merge (x:xs) (y:ys) 
	| x <= y = x: (merge xs (y:ys))
	| otherwise = y: (merge (x:xs) ys)
 
mergeLists :: (Ord a) => [[a]] -> [[a]]
mergeLists [] = []
mergeLists [x] = [x]
mergeLists (x:y:xs) = (merge x y): mergeLists xs

mergeSort :: (Ord a) => [[a]] -> [[a]]
mergeSort [x] = [x]
mergeSort (x:y:xs) = mergeSort (mergeLists (x:y:xs))

-----Main function

maxLengthRepSeq :: (Ord a) => [a] -> (Int, [a])
maxLengthRepSeq [] = (0,[])
maxLengthRepSeq string = largestPrefix(sortedSuffixes) --sort all prefixes, so plausible matches are one after the other, then get largestPrefix
	where
	[sortedSuffixes] = mergeSort(allSuffixes string)

{-
----------------------------------------------------------------------------------
	Exercise 5
----------------------------------------------------------------------------------
-}

-----From previous homework, split sentence into words
splitWords :: [Char] -> [[Char]]
splitWords sentence = loop sentence [[]]
	where
	separators = [',', '.', '!', '?', ' ']
	loop [] words 
		| length (head words) == 0 = (drop 1 words)
		| otherwise				   = words
	loop (x:xs) (y:ys)
		| elem x separators && length y > 0 = loop xs ([]:(y:ys))
		| elem x separators 				= loop xs (y:ys)
		| otherwise							= loop xs ((y++[x]):ys)
	
-----Help function, checks if two words rhyme
rhyme :: [Char] -> [Char] -> Bool
rhyme [] _ = error "Empty list!"
rhyme _ [] = error "Empty list!"
rhyme word1 word2
 -- | minLen == 2 = ((listLast len1 minLen word1) == (listLast len2 minLen word2))
	| minLen < 3 = False --2 letter words cannot rhyme
	| otherwise = ((listLast len1 3 word1) == (listLast len2 3 word2)) --if the last 3 letters are equal, the words rhyme
	where
	minLen = min len1 len2
	len1 = length word1
	len2 = length word2
	listLast :: Int -> Int -> [a] -> [a]
	listLast len last list = [ list!!i | i <- [len - last .. len - 1]] --returns the last "last" elements


-----Main function
classifyRhymeWords :: [Char] -> [[[Char]]]
classifyRhymeWords string = wordsLoop [] (splitWords string) --find groups for all words from splitWords string, where there are no grouped words yet
	where
	wordsLoop :: [[[Char]]] -> [[Char]] -> [[[Char]]]
	wordsLoop rhymeGroups [] = [] --no words => no rhyme groups
	wordsLoop rhymeGroups (x:[]) = [x]:rhymeGroups -- if there is only one word left, it does not belong to any group, add it in single group, return groups
	wordsLoop rhymeGroups (x:xs) = wordsLoop (g:rhymeGroups) ug --otherwise, add the new group to the groups, call recursive with the ungrouped elements
		where
		(g, ug) = loop [x] [] xs
	loop :: [[Char]] -> [[Char]] -> [[Char]] -> ([[Char]], [[Char]])
	loop grouped ungrouped [] = (grouped, ungrouped)
	loop grouped ungrouped (x:xs)
		| (rhyme (head grouped) x) == True = loop (x:grouped) ungrouped xs -- if x rhymes with the other elements in the group, add it
		| otherwise = loop grouped (x:ungrouped) xs --otherwise, add it to the elements that have no groups (used in next recursion of wordsLoop)
 
{-
----------------------------------------------------------------------------------
	Exercise 6
----------------------------------------------------------------------------------
-}
myMin :: (Ord a) => [a] -> a
myMin [] = error "Empty list, no minimal element!"
myMin (x:xs) = foldr min x xs -- = min (head xs) (foldr min x (tail xs)) = ...=the min element 

{-
----------------------------------------------------------------------------------
	Test functions
----------------------------------------------------------------------------------
-}

--NOTE: Cases with empty lists could not be included in test functions, as [] will not be identified as Ord-type argument when compiling test function and thus produce an ERROR.
--However, function will work when called with [] from terminal, as [] will be assigned fitting type during runtime.

-----Exercise 1

aufgabe1 _ = putStrLn("\
	\\t Test: selectSort (<) [2,1,5,0,4,3] => [0,1,2,3,4,5]\t Result: " ++ show (selectSort (<) [2,1,5,0,4,3]) ++ "\
	\\n\t Test: selectSort (>) [2,1,0,4,3] => [5,4,3,2,1,0]\t Result: " ++ show (selectSort (>) [2,1,5,0,4,3]) ++ "\
	\\n\t Test: selectSort (<=) \"astdbkl\" => \"abdklst\"\t Result: " ++ show(selectSort (<=) "astdbkl"))

-----Exercise 2	

aufgabe2 1 = putStrLn("\
	\\t Test: isSorted (<) [1,2,3,5,4,6] => False\t Result: " ++ show (isSorted (<) [1,2,3,5,4,6]) ++ "\
	\\n\t Test: isSorted (<) [1,2,3,4,5,6] => True\t Result: " ++ show (isSorted (<) [1,2,3,4,5,6]) ++ "\
	\\n\t Test: isSorted (>=) \"astdbkl\" => False\t Result: " ++ show(isSorted (>=) "astdbkl"))
aufgabe2 2 = putStrLn("\
	\\t Test: isSorted_ (<) [1,2,3,5,4,6] => False\t Result: " ++ show (isSorted_ (<) [1,2,3,5,4,6]) ++ "\
	\\n\t Test: isSorted_ (<) [1,2,3,4,5,6] => True\t Result: " ++ show (isSorted_ (<) [1,2,3,4,5,6]) ++ "\
	\\n\t Test: isSorted_ (>=) \"astdbkl\" => False\t Result: " ++ show(isSorted_ (>=) "astdbkl"))
aufgabe2 _ = putStrLn("-Aufgabe 2-\n Tests 1 through 2. USE: aufgabe2 [testid]")

-----Exercise 3

aufgabe3 _ = putStrLn("\
	\\t Test: maxLengthRepSeq \"abtdahdtdahko\" => (4,\"tdah\")\t Result: " ++ show (maxLengthRepSeq "abtdahdtdahko") ++ "\
	\\n\t Test: maxLengthRepSeq [1,1,0,1,1,0,0,1,0,1,1,0] => (5,[1,0,1,1,0])\t Result: " ++ show (maxLengthRepSeq [1,1,0,1,1,0,0,1,0,1,1,0]))

-----Exercise 4
	
aufgabe4 1 = putStrLn("\
	\\t Test: allSuffixes \"xyzab\" => [\"b\",\"ab\",\"zab\",\"yzab\",\"xyzab\"]\t Result: " ++ show(allSuffixes "xyzab") ++ "\
	\\n\t Test: prefix \"abcde\" \"abacde\" => \"ab\"\t Result: " ++ show(prefix "abcde" "abacde") ++ "\
	\\n\t Test: largestPrefix [\"a\",\"abca\",\"bca\",\"bcadabca\",\"ca\",\"cdabca\"] => (3,\"bca\")\t Result: " ++ show(largestPrefix ["a","abca","bca","bcadabca","ca","cdabca"]))
aufgabe4 2 = putStrLn("\
	\\t Test: maxLengthRepSeq \"abtdahdtdahko\" => (4,\"tdah\")\t Result: " ++ show (maxLengthRepSeq "abtdahdtdahko") ++ "\
	\\n\t Test: maxLengthRepSeq [1,1,0,1,1,0,0,1,0,1,1,0] => (5,[1,0,1,1,0])\t Result: " ++ show (maxLengthRepSeq [1,1,0,1,1,0,0,1,0,1,1,0]))
aufgabe4 _ = putStrLn("-Aufgabe 4-\n Tests 1 through 2. USE: aufgabe4 [testid]")

-----Exercise 5

aufgabe5 1 = putStrLn("\
	\\t Test: classifyRhymeWords \"Nikolaus baut ein Haus aus Holz und klaut dabei ein Bauhaus.\"\
	\=> [[\"klaut\",\"baut\"],[\"Nikolaus\",\"Bauhaus\",\"Haus\",\"aus\"],[\"ein\",\"ein\"],[\"dabei\"],[\"und\"],[\"Holz\"]]\t Result: " ++ show (classifyRhymeWords "Nikolaus baut ein Haus aus Holz und klaut dabei ein Bauhaus.") ++ "\
	\\n\t Test: classifyRhymeWords \"Aber, aber!? Welch ein Palaver!\" => [[\"Welch\"],[\"ein\"],[\"aber\",\"Aber\"],[\"Palaver\"]]\tResult: " ++ show (classifyRhymeWords "Aber, aber!? Welch ein Palaver!") ++ "\
	\\n\t Test: classifyRhymeWords \"Tu du was ich vorlas ohne Ratio, Horatio. => [[\"ich\"],[\"was\"],[\"vorlas\"],[\"du\"],[\"ohne\"],[\"Tu\"],[\"Ratio\",\"Horatio\"]]\"\tResult: " ++ show (classifyRhymeWords "Tu du was ich vorlas ohne Ratio, Horatio."))
aufgabe5 2 = putStrLn("\
	\\t Test: classifyRhymeWords \"\" => []\t Result: " ++ show (classifyRhymeWords "") ++ "\
	\\n\t Test: classifyRhymeWords [] => []\t Result: " ++ show (classifyRhymeWords []) ++ "\
	\\n\t Test: classifyRhymeWords \",.,\" => []\t Result: " ++ show (classifyRhymeWords ",.,"))
aufgabe5 _ = putStrLn("-Aufgabe 1a-\n Tests 1 through 2. USE: aufgabe1 [testid]")

-----Exercise 6

aufgabe6 1 = putStrLn("\
	\\t Test: myMin [3,2,5,6,3,1] => 1\t Result: " ++ show(myMin [3,2,5,6.3,1]) ++ "\
	\\n\t Test: myMin \"deacb\" => 'a'\t Result: " ++ show(myMin "deacb") ++ "\
	\\n\t Test: myMin \"\" => ERROR\t Result: " ++ show(myMin ""))
	
