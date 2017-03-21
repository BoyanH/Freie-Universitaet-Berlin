{-# LANGUAGE NPlusKPatterns #-}

{-
	Funktionale Programmierung

	Übungsblatt 6 (Abgabe: Mi., den 09.12. um 10:10 Uhr)
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
	
listJoin :: [[a]] -> [a]
listJoin list = join list []
	where
	join [] retList = qReverse(retList)
	join (x:xs) retList = join xs (foldl (flip (:)) retList x)

{-
----------------------------------------------------------------------------------
	Exercise 1
----------------------------------------------------------------------------------
-}

data Bits = Null | One
	deriving (Show, Eq)

complement :: Bits -> Bits
complement Null = One
complement One = Null

compress :: [Bits] -> [Int]
compress (x:xs)
	| x == Null = loop (x:xs) Null 0 []
	| x == One = loop (x:xs) One 0 [0]
	where
	loop :: [Bits] -> Bits -> Int -> [Int] -> [Int]
	loop [] currBit count compressed = qReverse (count:compressed)
	loop (x:xs) currBit count compressed
		| x == currBit = loop xs currBit (count+1) compressed
		| x /= currBit = loop xs x 1 (count:compressed)

decompress :: [Int] -> [Bits]
decompress (x:xs)
	| x == 0 = loop xs One []
	| otherwise = loop (x:xs) Null []
	where
	loop :: [Int] -> Bits -> [Bits] -> [Bits]
	loop [] currBit decompressed = qReverse decompressed
	loop (x:xs) currBit decompressed = loop xs (complement currBit) (listJoin [times,decompressed])
		where times = [currBit | i <- [1..x]]
		
{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}

-----Definitions from lecture:

data Nat = Zero | S Nat
               deriving Show
			   
add ::  Nat -> Nat -> Nat
add a Zero = a
add a (S b) = add (S a) b

add' ::  Nat -> Nat -> Nat
add' a Zero  = a
add' a (S b) = S (add' a b)

cutSub :: Nat -> Nat -> Nat
cutSub a Zero = a
cutSub Zero b = Zero
cutSub (S a) (S b) = cutSub a b

nsucc :: Nat -> Nat
nsucc n = S n

npred :: Nat -> Nat
npred Zero = Zero
npred (S n) = n

mult :: Nat -> Nat -> Nat
mult _ Zero  = Zero
mult a (S b) = add a (mult a b)

{- convenient help functions -}

nat2int :: Nat -> Integer
nat2int Zero = 0 --bottom of recursion, by Zero add nothing, break recursion
nat2int (S a) = (nat2int a) + 1 --otherwise, decrement the natural number and increment the integer untill Zero is reached

int2nat :: Integer -> Nat
int2nat a
	| a < 0 	= error "Not a natural number!" --natural numbers are only positive. If input is not, throw an error
	| otherwise = toNat Zero a --otherwise call the helper function
		where
		toNat nat 0 = nat --bottom of recursion, return the natural number
		toNat nat int = toNat (S nat) (int-1) --increment the natural nuber and decrement the integer, untill integer is 0

{-- power function --}

npow :: Nat -> Nat -> Nat
npow b Zero  = S Zero
npow b (S e) = mult b (npow b e)

{-- relations --}

equal :: Nat -> Nat -> Bool
equal Zero Zero = True -- 0 = 0
equal (S a) (S b) = equal a b --if both are >0, decrement both until a Zero is reached. If both are 0, then True, else False
equal _ _ = False --Zero is not equal to any other natural number

lt :: Nat -> Nat -> Bool
lt Zero (S _) = True
lt (S a) (S b) = lt a b
lt _ _ = False

gt :: Nat -> Nat -> Bool
gt (S _) Zero = True
gt (S a) (S b) = gt a b
gt _  _  = False

data ZInt = Z Nat Nat
                 deriving Show
               
zadd :: ZInt -> ZInt -> ZInt
zadd (Z a b) (Z c d) = Z (add a c) (add b d)

zsub :: ZInt -> ZInt -> ZInt
zsub (Z a b) (Z c d) = Z (add a d) (add b c)

{-- (a,b)*(c,d) equiv. (b-a)*(d-c) = bd - ad - cb + ac
                                   equiv.  (ad + cd, bd + ac)
--}

zmult :: ZInt -> ZInt -> ZInt
zmult (Z a b) (Z c d) = Z (add (mult a d) (mult c b)) (add (mult a c) (mult b d)) 

zsimplify :: ZInt -> ZInt
zsimplify (Z Zero b) = Z Zero b
zsimplify (Z a Zero) = Z a Zero
zsimplify (Z (S a) (S b)) = zsimplify (Z a b)

zlt :: ZInt -> ZInt -> Bool
zlt (Z a b) (Z c d) = lt (add b c) (add a d)

-----Our function definitions:

zneg :: ZInt -> ZInt
zneg (Z a b) = zsimplify (Z b a)

zabs :: ZInt -> ZInt
zabs (Z a Zero) = (Z a Zero)
zabs (Z Zero a) = (Z a Zero)
zabs (Z a b) = zabs (zsimplify (Z a b))
	
zpow :: ZInt -> Nat -> ZInt
zpow (Z a Zero) pow = (Z (npow a pow) Zero)
zpow (Z Zero a) pow = (Z Zero (npow a pow))
zpow (Z a b) pow = zpow (zsimplify (Z a b)) pow

zIsDivisor :: ZInt -> ZInt -> Bool
zIsDivisor (Z a Zero) (Z b Zero)
	| a `gt` b = loop a b b
	| otherwise = loop b a a
	where
	loop :: Nat -> Nat -> Nat -> Bool
	loop x y addr
		| x `gt` y = loop x (add addr y) addr
		| x `lt` y = False
		| x `equal` y = True
zIsDivisor (Z a b) (Z c d) = zIsDivisor (zabs (zsimplify (Z a b))) (zabs (zsimplify (Z c d)))

zggt :: ZInt -> ZInt -> ZInt
zggt (Z a Zero) (Z b Zero) = (Z (euclid a b) Zero)
zggt (Z Zero a) (Z Zero b) = (Z (euclid a b) Zero)
zggt (Z a Zero) (Z Zero b) = (Z Zero (euclid a b))
zggt (Z Zero a) (Z b Zero) = (Z Zero (euclid a b))
zggt (Z a b) (Z c d) = zggt (zsimplify (Z a b)) (zsimplify (Z c d))

euclid :: Nat -> Nat -> Nat
euclid a b
	| a `equal` b = a
	| a `gt` b = euclid (a `cutSub` b) b
	| otherwise = euclid a (b `cutSub` a)

zint2Int :: ZInt -> Integer
zint2Int (Z a Zero) = (nat2int a)
zint2Int (Z Zero a) = -(nat2int a)
zint2Int (Z a b) = zint2Int (zsimplify (Z a b))

int2Zint :: Integer -> ZInt
int2Zint z
	| z == 0 = (Z Zero Zero)
	| z > 0 = (Z (int2nat z) Zero)
	| z < 0 = (Z Zero (int2nat z))
	

--TODO: Conversion:
	
{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}

-----Imported definitions from lecture:

data SimpleBT = L | N SimpleBT SimpleBT  deriving Show

type Height = Integer
genSimpleBT :: Height -> SimpleBT
genSimpleBT   0  =  L
genSimpleBT (n+1) = N (genSimpleBT n) (genSimpleBT n)

nodes :: SimpleBT -> Integer
nodes L = 1
nodes (N leftT rightT) = 1 + nodes leftT + nodes rightT

height :: SimpleBT -> Integer
height L = 0
height (N lt rt) = (max (height lt) (height rt)) + 1

joinTrees :: SimpleBT -> SimpleBT -> SimpleBT
joinTrees leftTree rightTree = N leftTree rightTree

balanced :: SimpleBT -> Bool
balanced  L = True
balanced  (N lt rt) = (balanced lt) && (balanced rt) && height lt == height rt

{- some simple trees for testing -}
tree0 = genSimpleBT 2
tree1 = genSimpleBT 4
tree2 = joinTrees (genSimpleBT 3) (genSimpleBT 4)
tree3 = joinTrees (genSimpleBT 4) (joinTrees (genSimpleBT 1) (genSimpleBT 2))

test_nodes = nodes (genSimpleBT  4)
test_heigth = height  (N (N (N L L) L) (N L L))

{- The following functions visualize the simple binary trees (SimpleBT)
   you are not supposed to understand the semantics of the functions.
   There are many details, which are not relevant for the lecture understanding.
-}
   
{- node = "N" + "|" (below it)
   horizontal line = "---" between the subtrees
-}
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

{- print some of the tree examples -}
paint_t0 = printSimpleBT tree0
paint_t1 = printSimpleBT tree1
paint_t2 = printSimpleBT tree2
paint_t3 = printSimpleBT tree3
	
-----Our function definitions:

insLeaf :: SimpleBT -> SimpleBT
insLeaf L = (N L L)
insLeaf (N ltree rtree)
	| (nodes ltree) <= (nodes rtree) = (N (insLeaf ltree) rtree)
	| otherwise = (N ltree (insLeaf rtree))
	
insLeaves :: SimpleBT -> Integer -> SimpleBT
insLeaves tree 0 = tree
insLeaves tree leafNum = insLeaves (insLeaf tree) (leafNum - 1)

delLeaf :: SimpleBT -> SimpleBT
delLeaf (N L L) = L
delLeaf (N ltree rtree)
	| (nodes ltree) >= (nodes rtree) = (N (delLeaf ltree) rtree)
	| otherwise = (N ltree (delLeaf rtree))

delLeaves :: SimpleBT -> Integer -> SimpleBT
delLeaves tree 0 = tree
delLeaves tree leafNum = delLeaves (delLeaf tree) (leafNum - 1)

{-
----------------------------------------------------------------------------------
	Exercise 4
----------------------------------------------------------------------------------
-}
	
data BSearchTree a = Nil | Node a (BSearchTree a) (BSearchTree a)
	deriving ( Show, Eq )

-----Definitions from lecture:
	
smallest:: (Ord a) => BSearchTree a -> a
smallest (Node x Nil _) = x
smallest (Node x leftTree _) = smallest leftTree

biggest:: (Ord a) => BSearchTree a -> a
biggest(Node x _ Nil) = x
biggest(Node x _ rTree) = biggest rTree

mirror:: (Ord a) => BSearchTree a -> BSearchTree a
mirror Nil = Nil
mirror (Node x xl xr) = Node x (mirror xr) (mirror xl)

inOrder :: (Ord a) => BSearchTree a -> [a]
inOrder Nil = []
inOrder (Node x ltree rtree) = inOrder ltree ++ x : inOrder rtree

-----Our function definitions:

nodesBS :: (Ord a) => BSearchTree a -> Integer
nodesBS Nil = 1
nodesBS (Node x leftT rightT) = 1 + (nodesBS leftT) + (nodesBS rightT)

twoChildren :: (Ord a) => BSearchTree a -> Bool
twoChildren (Node x Nil Nil) = True
twoChildren (Node x Nil rtree) = False
twoChildren (Node x ltree Nil) = False
twoChildren (Node x ltree rtree) = (twoChildren ltree) && (twoChildren rtree)

full :: (Ord a) => BSearchTree a -> Bool
full tree = ((nodesBS tree) == 2^(lbranchLength tree 0) - 1)
	where
	lbranchLength Nil cnt = cnt + 1
	lbranchLength (Node x ltree rtree) cnt = lbranchLength ltree (cnt + 1)
	
mapTree :: (Ord a, Ord b) => (a -> b) -> BSearchTree a -> BSearchTree b
mapTree f Nil = Nil
mapTree f (Node x ltree rtree) = Node (f x) (mapTree f ltree) (mapTree f rtree)

foldTree :: (Ord a) => b -> (a -> b -> b -> b) -> BSearchTree a -> b
foldTree e f Nil = e
foldTree e f (Node x ltree rtree) = f x (foldTree e f ltree) (foldTree e f rtree)
