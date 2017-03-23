{-# LANGUAGE NPlusKPatterns #-}

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

data Bits = Zero | One
				deriving (Show, Eq, Ord)

quickComp :: [Int] -> [Bits] -> [Int]
quickComp compressed [last] = qReverse compressed
quickComp (crnt:prev) (x:y:rest) 
								| x == y 	= quickComp ((crnt+1):prev) (y:rest)
								| otherwise = quickComp (1:(crnt:prev)) (y:rest)

quickDeComp :: Bits -> [Bits] -> [Int] -> [Bits]
quickDeComp bit prev [] = qReverse prev
quickDeComp bit prev (x:xs) = quickDeComp (revBit bit) ((getBits bit x []) ++ prev) xs
							where 
								revBit One = Zero
								revBit Zero = One

								getBits bit 0 bits = bits
								getBits bit x bits = getBits bit (x-1) (bit:bits) 

compress :: [Bits] -> [Int]
compress (One:xs) = 0:(quickComp [1] (One:xs))
compress (x:xs) = quickComp [1] (x:xs)

decompress :: [Int] -> [Bits]
decompress (0:xs) = quickDeComp One [] xs
decompress list = quickDeComp Zero [] list

{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}			
data Nat = Null | S Nat
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
	a * b = multNat a b

nsucc :: Nat -> Nat
nsucc a = (S a) -- return the successor of a natural number

lt :: Nat -> Nat -> Bool
lt Null (S _) = True --0 is smaller than any other natural number
lt (S a) (S b) = lt a b --if both numbers are successors of a natural number (both not 0), then decrement both
lt _ _ = False --lt (S _) Zero = False, because there is no natural number < 0

equal :: Nat -> Nat -> Bool
equal Null Null = True -- 0 = 0
equal (S a) (S b) = equal a b --if both are >0, decrement both until a Zero is reached. If both are 0, then True, else False
equal _ _ = False --Zero is not equal to any other natural number

nat2int :: Nat -> Integer
nat2int Null = 0 --bottom of recursion, by Zero add nothing, break recursion
nat2int (S a) = (nat2int a) + 1 --otherwise, decrement the natural number and increment the integer untill Zero is reached

int2nat :: Integer -> Nat
int2nat a
	| a < 0 	= error "Not a natural number!" --natural numbers are only positive. If input is not, throw an error
	| otherwise = toNat Null a --otherwise call the helper function
		where
		toNat nat 0 = nat --bottom of recursion, return the natural number
		toNat nat int = toNat (S nat) (int-1) --increment the natural nuber and decrement the integer, untill integer is 0

natUngerade :: Nat -> Bool
natUngerade Null = False --if at the bottom of the recursion 0 is left after n subtractions of 2, then number `mod` 2 = 0 => even => False
natUngerade (S (Null)) = True --otherwise, at the bottom of the recursion 1 is left => number `mod` 2 = 1 => True
natUngerade (S (S n)) = natUngerade n --if the natural number is >= (S (S n)) >= 2, then decrement it by 2 

cutSub :: Nat -> Nat -> Nat --if b >= a then a - b = 0 
cutSub a Null = a --if subtraction has finished, return a // (a-0 = a)
cutSub Null b = Null --Zero is the smallest natural number => a <= b  => as per task definition, return Zero
cutSub (S a) (S b) = cutSub a b --decrement both natural numbers // (x-y = (x-1) - (y-1))

addNat :: Nat -> Nat -> Nat
addNat a Null = a -- a + 0 = 0, bottom of recursion, return a
addNat a (S b) = addNat (S a) b -- a + b = (a+1) + (b-1)

multNat :: Nat -> Nat -> Nat
multNat a Null = Null
multNat Null b = Null
multNat a b = quickMult Null a b
				where
					quickMult acc a Null = acc 
					quickMult acc a (S b) = quickMult (acc + a) a b

foldn :: (Nat -> Nat) -> Nat -> Nat -> Nat
foldn h c Null = c
foldn h c (S n) = h (foldn h c n)

natPow :: Nat -> Nat -> Nat
natPow nat pow = foldn (multNat nat) (S Null) pow

data ZInt = Z Nat Nat
				deriving (Show)

zint2Int :: ZInt -> Integer 
zint2Int (Z a b) = (nat2int b) - (nat2int a)

int2Zint :: Integer -> ZInt
int2Zint num 
			| num == 0 	= (Z Null Null)
			| num > 0  	= (Z Null (int2nat num))
			| otherwise = (Z (int2nat (abs num)) Null)

znormalize :: ZInt -> ZInt
znormalize (Z a Null) = (Z a Null)
znormalize (Z Null b) = (Z Null b)
znormalize (Z (S a) (S b)) = znormalize (Z a b)

zneg :: ZInt -> ZInt
zneg (Z Null b) = (Z b Null)
zneg (Z a Null) = (Z Null a)
zneg (Z a b) = zneg (znormalize (Z a b))

zabs :: ZInt -> ZInt 
zabs (Z Null b) = (Z Null b)
zabs (Z a Null) = (Z Null a)
zabs (Z a b) = zabs (znormalize (Z a b))

zpow :: ZInt -> Nat -> ZInt
zpow (Z Null b) nat = (Z Null (natPow b nat)) 
zpow (Z a Null) nat 
						| natUngerade nat 	= (Z (natPow a nat) Null)
						| otherwise 		= (Z Null (natPow a nat))
zpow zint nat = zpow (znormalize zint) nat

zIsDivisor :: ZInt -> ZInt -> Bool
zIsDivisor divisor num = isDivisor (zabs (znormalize divisor)) (zabs (znormalize num))
						where
							isDivisor (Z Null d) (Z Null n)
												| n >= d 		= isDivisor (Z Null d) (Z Null (n - d))
												| n == Null 	= True
												| otherwise 	= False

--Using the Euclidian Algorithm
zggt :: ZInt -> ZInt -> ZInt
zggt a b = quickGCD (zabs (znormalize a)) (zabs (znormalize b))
			where quickGCD (Z Null x) (Z Null y)
											| x > y 	= zggt (Z Null (x-y)) (Z Null y)
											| y > x 	= zggt (Z Null x) (Z Null (y-x))
											| x == y 	= (Z Null x)


{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}	


--Tree painting implementation by M. Esponda
paintTree :: SimpleBT -> ([[Char]], Int)
paintTree L = ([" L  "], 1)
paintTree (N lTree rTree) = ([nodeLine, nodeHLine, horLine] ++ subTrees, newNodePos)
            where                   
                (lNodePicture, leftNodePos)  = paintTree lTree
                (rNodePicture, rigthNodePos) = paintTree rTree
                     
                ltNewPicture = moveTreePos lNodePicture rNodePicture
                rtNewPicture = moveTreePos rNodePicture lNodePicture
                
                {- write spaces in between if necessary -}
                moveTreePos :: [String] -> [String] -> [String]
                moveTreePos str1 str2 | length str1 >= length str2 = str1
                                      | otherwise = str1 ++ (take rowsToFill (repeat spaces))
                                           where
                                              spaces = gen (length (head str1))  " "
                                              rowsToFill = (length str2) - (length str1)
                     
                leftWidth = length (head lNodePicture)
                rightWidth = length (head rNodePicture)
                width = leftWidth + rightWidth            

                hLineLength = (leftWidth - leftNodePos) + rigthNodePos
                newNodePos = leftNodePos + (div hLineLength 2)

                horLine  = (gen leftNodePos " ") ++ "*" ++ gen (hLineLength - 1) "-" ++ "*"
                                                 ++ gen (width - hLineLength - leftNodePos - 1) " "
                nodeLine  = (gen newNodePos " ") ++ "N" ++ gen (width - newNodePos - 1) " "
                nodeHLine = (gen newNodePos " ") ++ "|" ++ gen (width - newNodePos - 1) " " 
               
                                      
                     
                subTrees = zipWith (++) ltNewPicture rtNewPicture
                                              
{- concatenates n times the String str -}
gen :: Int -> [a] -> [a]
gen n str = take n (foldr (++) [] (repeat str))

{- insert the necesary new lines to show the rows of the list picture on the screen -}
printCharList list = putStr (foldr (++) [] (map (++"\n") list))

{- print a simple binary tree -}
printSimpleBT tree = printCharList (fst (paintTree tree))

--end of tree painting implementation

--generate simple binary tree from the lecture for easy testing

type Height = Integer

genSimpleBT :: Height -> SimpleBT
genSimpleBT 0 = L
genSimpleBT (n+1) = N (genSimpleBT n) (genSimpleBT n)

--end of generate simple binary tree

data SimpleBT = L | N SimpleBT SimpleBT deriving Show

nodes :: SimpleBT -> Integer
nodes L = 1
nodes (N lTree rTree) = 1 + nodes lTree + nodes rTree

insLeaf :: SimpleBT -> SimpleBT 
insLeaf L = N L L
insLeaf (N lTree rTree) 
						| nodes lTree > nodes rTree 	= (N lTree (insLeaf rTree))
						| otherwise 					= (N (insLeaf lTree) rTree)

insLeaves :: SimpleBT -> Integer -> SimpleBT
insLeaves bTree 0 = bTree
insLeaves bTree numOfLeaves = insLeaves (insLeaf bTree) (numOfLeaves - 1)

delLeaf :: SimpleBT -> SimpleBT
delLeaf (N L L) = L
delLeaf (N lTree rTree) 
						| nodes lTree > nodes rTree 	= (N (delLeaf lTree) rTree)
						| otherwise 					= (N lTree (delLeaf rTree))

delLeaves :: SimpleBT -> Integer -> SimpleBT
delLeaves bTree 0 = bTree
delLeaves L numOfLeaves = error "Cannot remove all leaves from the binary tree!"
delLeaves bTree numOfLeaves = delLeaves (delLeaf bTree) (numOfLeaves - 1)

{-
----------------------------------------------------------------------------------
	Exercise 4
----------------------------------------------------------------------------------
-}

data BSearchTree a = Nil | Node a (BSearchTree a) (BSearchTree a)
											deriving ( Show, Eq )

nodesBST :: BSearchTree a -> Integer
nodesBST (Node a Nil Nil) = 1
nodesBST (Node a b Nil) = 1 + (nodesBST b)
nodesBST (Node a Nil c) = 1 + (nodesBST c)
nodesBST (Node a b c) = 1 + (nodesBST b) + (nodesBST c)

heightBST :: BSearchTree a-> Height
heightBST Nil = 0
heightBST (Node a b Nil) = 1 + (heightBST b)
heightBST (Node a Nil c) = 1 + (heightBST c)
heightBST (Node a b c) = 1 + (max (heightBST b) (heightBST c))

twoChildren :: (Ord a) => BSearchTree a -> Bool
twoChildren (Node a Nil Nil) = True
twoChildren (Node a Nil _ ) = False
twoChildren (Node a _ Nil) = False
twoChildren (Node a b c) = twoChildren b && twoChildren c

full :: (Ord a) => BSearchTree a -> Bool
full bST = ((2 ^ (heightBST bST)) - 1) == (nodesBST bST)

mapTree :: (Ord a, Ord b) => (a -> b) -> BSearchTree a -> BSearchTree b
mapTree f Nil = Nil 
mapTree f (Node value lTree rTree) = (Node (f value) (mapTree f lTree) (mapTree f rTree))

--I assume z is the value to be used instead of Nil. So for addition 0 will be used, for multiplication 1 ...etc.
foldTree :: (Ord a) => b -> (a -> b -> b -> b) -> BSearchTree a -> b
foldTree z f (Node value Nil Nil) = f value z z
foldTree z f (Node value lt Nil) = f value (foldTree z f lt) z
foldTree z f (Node value Nil rt) = f value z (foldTree z f rt)
foldTree z f (Node value lt rt) = f value (foldTree z f lt) (foldTree z f rt)

testFunc4FoldTree a b c = a + b + c

{-
----------------------------------------------------------------------------------
	Test functions
----------------------------------------------------------------------------------
-}

-----Exercise 1

aufgabe1 1 = putStrLn("-Aufgabe 1-a\n\n\
	\\n Test: compress [One, Zero, Zero, One, One, One, One] => [0,1,2,4] \t Result: " ++ show(compress [One, Zero, Zero, One, One, One, One]) ++ "\
	\\n Test: compress [Zero, Zero, Zero, Zero, One, One, Zero, Zero, Zero, Zero] => [4,2,4] \t Result: " ++ show(compress [Zero, Zero, Zero, Zero, One, One, Zero, Zero, Zero, Zero]))
	

aufgabe1 2 = putStrLn("-Aufgabe 1-b\n\n\
	\\n Test: decompress [0,1,2,4] => [One, Zero, Zero, One, One, One, One] \t Result: " ++ show(decompress [0,1,2,4]) ++ "\
	\\n Test: decompress [4,2,4] => [Zero, Zero, Zero, Zero, One, One, Zero, Zero, Zero, Zero] \t Result: " ++ show(decompress [4,2,4]))
	
-----Exercise 2
	
aufgabe2 1 = putStrLn("-Aufgabe 2-a\n\n\
 \ Test: zneg (Z (S (S (S Null))) Null ) => Z Null (S (S (S Null))) \t Result: " ++ show(zneg (Z (S (S (S Null))) Null )) ++ "\
 \\n Test: zneg (Z (S (S (S Null))) (S (S (S Null))) ) => Z Null Null \t Result: " ++ show(zneg (Z (S (S (S Null))) (S (S (S Null))) )) ++ "\
 \\n Test: zneg (Z Null Null ) => Z Null Null \t \t \t \t Result: " ++ show((zneg (Z Null Null ) )) ++ "\
 \\n Test: zneg ( Z Null (S (S (S Null))) ) => Z (S (S (S Null))) Null \t Result: " ++ show(zneg ( Z Null (S (S (S Null))) ) ))

aufgabe2 2 = putStrLn("-Aufgabe 2-b\n\n\
 \ Test: zabs (Z (S (S (S Null))) Null) => Z Null (S (S (S Null))) \t Result: " ++ show(zabs (Z (S (S (S Null))) Null)) ++ "\
 \\n Test: zabs (Z Null (S (S (S Null))) ) => Z Null (S (S (S Null))) \t Result: " ++ show(zabs (Z Null (S (S (S Null))) )))

aufgabe2 3 = putStrLn("-Aufgabe 2-c\n\n\
 \ Test: zint2Int (zpow (int2Zint 4) (int2nat 3)) =>  64 \t \t Result: " ++ show(zint2Int (zpow (int2Zint 4) (int2nat 3))) ++ "\
  \\n Test: zint2Int (zpow (int2Zint (-3)) (int2nat 3)) => -27 \t Result: " ++ show(zint2Int (zpow (int2Zint (-3)) (int2nat 3))) ++ "\
  \\n Test: zint2Int (zpow (int2Zint (-3)) (int2nat 2)) => 9 \t Result: " ++ show(zint2Int (zpow (int2Zint (-3)) (int2nat 2))))

aufgabe2 4 = putStrLn("-Aufgabe 2-d\n\n\
 \ Test: zIsDivisor (int2Zint 4) (int2Zint 64) =>  True \t \t Result: " ++ show( zIsDivisor (int2Zint 4) (int2Zint 64)) ++ "\
  \\n Test: zIsDivisor (int2Zint (-3)) (int2Zint 27) => True \t Result: " ++ show(zIsDivisor (int2Zint (-3)) (int2Zint 27)) ++ "\
  \\n Test: zIsDivisor (int2Zint 2) (int2Zint 27) => False \t \t Result: " ++ show(zIsDivisor (int2Zint 2) (int2Zint 27)))

aufgabe2 5 = putStrLn("-Aufgabe 2-e\n\n\
 \ Test: zint2Int (zggt (int2Zint 24) (int2Zint 36)) =>  12 \t \t Result: " ++ show( zint2Int (zggt (int2Zint 24) (int2Zint 36)) ) ++ "\
  \\n Test: zint2Int (zggt (int2Zint 26) (int2Zint 13)) => 13 \t \t Result: " ++ show( zint2Int (zggt (int2Zint 26) (int2Zint 13)) ) ++ "\
  \\n Test: zint2Int (zggt (int2Zint 13) (int2Zint 29)) => 1 \t \t Result: " ++ show(zint2Int (zggt (int2Zint 13) (int2Zint 29)) ))

aufgabe2 6 = putStrLn("-Aufgabe 2- int2Zint, zint2Int\n\n\
 \ Test: zint2Int (Z (S (S (S Null))) Null) =>  -3 \t \t Result: " ++ show( zint2Int (Z (S (S (S Null))) Null) ) ++ "\
 \\n Test: zint2Int (Z Null (S (S (S (S Null))))) =>  4 \t \t Result: " ++ show( zint2Int (Z Null (S (S (S (S Null))))) ) ++ "\
 \\n Test: int2Zint (-3) => Z (S (S (S Null))) Null \t \t Result: " ++ show( int2Zint (-3) ) ++ "\
 \\n Test: int2Zint (4) => Z Null (S (S (S (S Null)))) \t \t Result: " ++ show( int2Zint (4) ))

-----Exercise 3
aufgabe3 1 = do
	putStrLn("-Aufgabe 3-a\n\n\
		 \ Test: printSimpleBT (genSimpleBT 3)")
	printSimpleBT (genSimpleBT 3)
	
	putStrLn("\n Test: printSimpleBT (insLeaf  (genSimpleBT 3)) =>")
	printSimpleBT (insLeaf  (genSimpleBT 3))


	putStrLn("\n Test: printSimpleBT (insLeaves  (genSimpleBT 3) 2) =>")
	printSimpleBT (insLeaves  (genSimpleBT 3) 2)


	putStrLn("\n Test: printSimpleBT (insLeaves  (genSimpleBT 3) 5) => \n")
	printSimpleBT (insLeaves  (genSimpleBT 3) 5)

	putStrLn("\n Test: printSimpleBT (insLeaves  (genSimpleBT 3) 9) => \n")
	printSimpleBT (insLeaves  (genSimpleBT 3) 9)

aufgabe3 2 = do
	putStrLn("-Aufgabe 3-b\n\n\
		 \ printSimpleBT (delLeaf (insLeaves  (genSimpleBT 3) 9)) =>")
	printSimpleBT (delLeaf (insLeaves  (genSimpleBT 3) 9))
	
	putStrLn("\n Test: printSimpleBT (delLeaves (insLeaves  (genSimpleBT 3) 9) 2) =>")
	printSimpleBT (delLeaves (insLeaves  (genSimpleBT 3) 9) 2)


	putStrLn("\n Test: printSimpleBT (delLeaves (insLeaves  (genSimpleBT 3) 9) 5) =>")
	printSimpleBT (insLeaves  (genSimpleBT 3) 2)


	putStrLn("\n Test: printSimpleBT (delLeaves (insLeaves  (genSimpleBT 3) 9) 9) => \n")
	printSimpleBT (delLeaves (insLeaves  (genSimpleBT 3) 9) 9)

-----Exercise 4

aufgabe4 1 = putStrLn("-Aufgabe 2- a\n\n\
 \ Test: twoChildren (Node 3 (Node 2 Nil Nil) Nil) =>  False \t \t Result: " ++ show( twoChildren (Node 3 (Node 2 Nil Nil) Nil) ) ++ "\
 \\n Test: twoChildren (Node 3 (Node 2 Nil Nil) (Node 3 Nil Nil)) =>  True \t Result: " ++ show( twoChildren (Node 3 (Node 2 Nil Nil) (Node 3 Nil Nil)) ) ++ "\
\\n Test: twoChildren (Node 3 (Node 2 (Node 1 Nil Nil) Nil) (Node 3 Nil Nil)) =>  False) \t Result: " ++ show( twoChildren (Node 3 (Node 2 (Node 1 Nil Nil) Nil) (Node 3 Nil Nil)) ))

aufgabe4 2 = putStrLn("-Aufgabe 2- b\n\n\
 \ Test: full (Node 3 (Node 2 Nil Nil) Nil) =>  False \t \t \t \t Result: " ++ show( full (Node 3 (Node 2 Nil Nil) Nil) ) ++ "\
 \\n Test: full (Node 3 (Node 2 Nil Nil) (Node 3 Nil Nil)) =>  True \t \t Result: " ++ show( full (Node 3 (Node 2 Nil Nil) (Node 3 Nil Nil)) ) ++ "\
 \\n Test: full (Node 3 (Node 2 Nil Nil) (Node 3 Nil (Node 4 Nil Nil))) =>  False \t Result: " ++ show( full (Node 3 (Node 2 Nil Nil) (Node 3 Nil (Node 4 Nil Nil))) ))


aufgabe4 3 = putStrLn("-Aufgabe 2- c\n\n\
 \ Test: mapTree (*2) (Node 3 (Node 2 Nil Nil) Nil) =>  Node 6 (Node 4 Nil Nil) Nil \n \t \t Result: " ++ show( mapTree (*2) (Node 3 (Node 2 Nil Nil) Nil) ) ++ "\
 \\n\n Test: mapTree (+1) (Node 3 (Node 2 Nil Nil) (Node 3 Nil Nil)) =>  Node 4 (Node 3 Nil Nil) (Node 4 Nil Nil) \n \t \t Result: " ++ show( mapTree (+1) (Node 3 (Node 2 Nil Nil) (Node 3 Nil Nil)) ) ++ "\
 \\n\n Test: mapTree (subtract 2) (Node 3 (Node 2 Nil Nil) (Node 3 Nil (Node 4 Nil Nil))) =>  Node 1 (Node 0 Nil Nil) (Node 1 Nil (Node 2 Nil Nil)) \n \t \t Result: " ++ show (mapTree (subtract 2) (Node 3 (Node 2 Nil Nil) (Node 3 Nil (Node 4 Nil Nil))) ))

aufgabe4 4 = putStrLn("-Aufgabe 2- d\n\n\
 \ Test: foldTree 0 testFunc4FoldTree (Node 3 (Node 2 Nil Nil) Nil) => 5  \t \t \t \t Result: " ++ show( foldTree 0 testFunc4FoldTree (Node 3 (Node 2 Nil Nil) Nil) ) ++ "\
 \\n Test: foldTree 0 testFunc4FoldTree (Node 3 (Node 2 Nil Nil) (Node 3 Nil Nil)) => 8  \t \t \t Result: " ++ show( foldTree 0 testFunc4FoldTree (Node 3 (Node 2 Nil Nil) (Node 3 Nil Nil)) ) ++ "\
 \\n Test: foldTree 0 testFunc4FoldTree (Node 3 (Node 2 Nil Nil) (Node 3 Nil (Node 4 Nil Nil))) => 12  \t Result: " ++ show ( foldTree 0 testFunc4FoldTree (Node 3 (Node 2 Nil Nil) (Node 3 Nil (Node 4 Nil Nil))) ))
