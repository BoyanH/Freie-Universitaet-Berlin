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
-}

selectSort :: (Ord a) => (a -> a -> Bool) -> [a] -> [a]
selectSort p [] = [] --put smallest/largest element in fron, repeat for the rest
selectSort p list = firstElem : (selectSort p (deleteElem firstElem list )) 
						where
							firstElem = calculateFirst list
							calculateFirst [x] = x --if only the biggest/smallest element is in list, return it
							calculateFirst (x:y:xs) --if there is a smaller/bigger element, call again with this and rest
													| p x y 		= calculateFirst (x:xs)
													| otherwise 	= calculateFirst (y:xs)
													--otherwise, keep the crnt max/min element and call again with rest
							deleteElem elmnt [] = [] --if the element is not found anywhere in the list, abort function
							deleteElem elmnt (x:xs) 
													| elmnt == x 	= xs --if the element isthe one to be deleted, return the rest of the array
													| otherwise  	= x:(deleteElem elmnt xs) 
											--otherwise, append it at the beginning of the recursive deleting

{-
Complexity Analysis:

selectSort p list => n* (selectSort p (deleteElem firstElem list)) => n * n * n = n^3 => selectSort = O(n^3)

-}

{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}

isSorted :: (Ord a) => [a] -> Bool
isSorted (x:y:xs) = checkSort (sortOrder x y) (y:xs)
					where
						sortOrder x y --determine the sort order from the relation of the first 2 elements
									| x >= y = (>=)
									| x <= y = (<=)
						checkSort p []  = True
						checkSort p [x] = True
						checkSort p (x:y:xs) = (p x y) && (checkSort p (y:xs)) --check if this relation is true for all others

isSorted2 :: (Ord a) => [a] -> Bool
--from all (x, x+1) check whether x is bigger or equal to x+1, as well as smaller equal
--for all pairs (x,x+1) a Bool is created. If the relation is the same for all elements (all True) => list is sorted
isSorted2 xs = and (zipWith (<=) xs (tail xs)) || and (zipWith (>=) xs (tail xs))

{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------


mult n m => m recursive calls => mult n m = O(n)


russMult n m => log2 (m) + 1 recursive calls => russMult n m = O( log(m) )


-}

{-
----------------------------------------------------------------------------------
	Exercise 4
----------------------------------------------------------------------------------
-}

--The Merge-Sort function from the lecture
split :: [a] -> [[a]]
split [] = []
split [x] = [[x]]
split (x:xs) = [x]: (split xs)

merge :: (Ord a) => [a] -> [a] -> [a]
merge [] ys = ys
merge xs [] = xs
merge (x:xs) (y:ys) | x <= y 	= x:(merge xs (y:ys))
					| otherwise = y:(merge (x:xs) ys)

mergeLists :: (Ord a) => [[a]] -> [[a]]
mergeLists [] = []
mergeLists [x] = [x]
mergeLists (x:y:xs) = (merge x y): mergeLists xs

mergeSort :: (Ord a) => [[a]] -> [[a]]
mergeSort [x] = [x]
mergeSort (x:y:xs) = mergeSort (mergeLists (x:y:xs))

startMergeSort :: (Ord a) => [a] -> [a]
startMergeSort xs = sortedList
				where
 					[sortedList] = mergeSort (split xs)



--make a list from the list, always take one element less from the beginning
allSuffixes :: (Eq a) => [a] -> [[a]]
allSuffixes [] = []
allSuffixes charList = charList:(allSuffixes (drop 1 charList))

--having 2 lists, add every element, that is equal in both lists, starting from the beginning
prefix :: (Eq a) => [a] -> [a] -> [a]
prefix xs [] = []
prefix [] ys = []
prefix (x:xs) (y:ys) 
					| x == y 	= x:(prefix xs ys) --if the elements are equal, add the equal element and continue
					| otherwise = [] --if there is a differece, add no more elements, break recursion

largestPrefix :: (Eq a) => [[a]] -> (Int, [a])
largestPrefix list = quickLP (0, []) list
					where 
						quickLP lastP [] = lastP
						quickLP lastP [x] = lastP --if there are no more pairs to check, return the largest prefix
						quickLP lastP (x:y:xs)   --if the new prefix is larger than the previous, call quickLP with it and the rest
												| fst lastP < length crntPref = quickLP (length crntPref, crntPref) (y:xs)
												| otherwise = quickLP lastP (y:xs) --otherwise, continue with the same prefix
												where
													crntPref = prefix x y --take the new equal prefix from another pair

maxLengthRepSeq :: (Ord a) => [a] -> [a]
--sort all prefixes and check them pair by pair to find the largest one, than return the prefix
--when sorted, prefixes that are likely to be equal are staying one after another, so we only need to check
--the pairs, which are one after the other, not every possible combination of pairs
maxLengthRepSeq list = snd (largestPrefix (startMergeSort (allSuffixes list)))


{-
Complexity Analysis:

allSuffixes - n recursive calls, n-1 additions of two lists
=> allSuffixes = c*n = n= O(n) , where c is the required time for the addition of two lists


prefix - n recursive calls, for n being the length of the shorter list
prefix = c*n , wher c is the required time for 1 comparison and 1 appending of element to list
=> prefix = n = O(n)

largestPrefix = n recursive calls of quickLP and for each n recursive calls of prefix
=> largestPrefix = n_1*n_2*c ,where c is the required time for 1 comparison
=> largestPrefix = n*n = O (n^2)

maxLengthRepSeq = snd (largestPrefix (startMergeSort (allSuffixes list))) = snd (n^2 (startMergeSort (allSuffixes list))) =
	= snd (n^2  (n.log(n)  (allSuffixes list))) = snd (n^2 (n.log(n) (n))) = snd (n^2 + n*log(n) + n) = c*n^2 = O(n^2)
-}

{-
----------------------------------------------------------------------------------
	Exercise 5
----------------------------------------------------------------------------------
-}

--like in the previous homework, split a sentance in words
splitWords :: [Char] -> [[Char]]
splitWords sentance = quickSplit sentance [[]]
					where
						separators = [',', '.', '!', '?', ' ']
						quickSplit [] words 
											| length (head words) == 0 = reverse (drop 1 words)
											| otherwise				   = reverse words
						quickSplit (x:xs) (y:ys)
											| elem x separators && length y > 0 = quickSplit xs ([]:(y:ys))
											| elem x separators 				= quickSplit xs (y:ys)
											| otherwise							= quickSplit xs ((y++[x]):ys)

rhymes :: [Char] -> [[Char]] -> [[Char]]
rhymes word [] = []
rhymes word (x:xy) 
			--check if last three letters from the crnt word are equal to those of the word we are trying to find rhymes for
			--if so, add the crnt word to the rhymes of the word we are searching rhymes for and continue with the rest
				| (take 3 (reverse word)) == (take 3 (reverse x)) = [x] ++ rhymes word xy
				| otherwise = rhymes word xy --otherwise, skip the word

--removes the intersection of two lists from the 1 one, XOR on lists
deleteRepsInList :: [[Char]] -> [[Char]] -> [[Char]]
deleteRepsInList [] _ = [] --if there are no more to check for removal, break
deleteRepsInList (x:xs) toRemove 
							--if the element exists in the toBeRemoved list, skip it
							| elem x toRemove = deleteRepsInList xs toRemove
							| otherwise = x:(deleteRepsInList xs toRemove) --otherwise, add it and continue


classifyRhymeWords :: [Char] -> [[[Char]]]
classifyRhymeWords sentance = toGroups wordsInSentance wordsInSentance
							where
								wordsInSentance = splitWords sentance
								toGroups [] allWords = [] --if there are no more words to find rhymes of, break
								toGroups (x:xs) aW --if there are rhymes of the word x, add them to a group and continue
															--each time the rhymes are removed from the list of allWords
															--as a word cannot be in 2 different rhyme groups
													| length (rhymes x aW) > 0 = [rhymes x aW] ++ toGroups xs (deleteRepsInList aW (rhymes x aW))
													| otherwise 			   = toGroups xs aW --otherwise, continue

{-
----------------------------------------------------------------------------------
	Exercise 6
----------------------------------------------------------------------------------
-}

myMin :: (Ord a) => [a] -> a
myMin [] = error "Empty list, no minimal element!"
myMin (x:xs) = foldr min x xs  -- ...min (min x (last xs)) (take 1 (drop 1(reverse xs)))
--foldr min x xs always takes min x and last xs, then min from result and the next elem from the end of the list...etc.