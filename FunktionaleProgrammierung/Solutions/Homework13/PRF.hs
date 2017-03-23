{-#  LANGUAGE NPlusKPatterns  #-}

{-- 
    Primitive recursive functions in Haskell 
    Tuples of natural numbers are represented by lists

    Original version: WS-99-00 Prof. Dr. Raul Rojas
    modified WS-12-13 by: Prof. Dr. Margarita Esponda
    modified WS-15-16 by: Prof. Dr. Margarita Esponda
--}

---------------------- Basic functions ---------------------------------
module PRF where

type PRFunction = ([Integer]->Integer)

-- Zero function

z :: PRFunction
z xs = 0

--Succesor function

s :: PRFunction
s [x] = x+1

--Projection functions

p::Integer -> [Integer] -> Integer
p 1 (a:b) = a
p n (a:b) = p (n-1) b

---------------------- Metha-functions --------------------------------

-- Composition

compose :: PRFunction -> [PRFunction] -> [Integer] -> Integer
compose h gs xs = h [g xs | g<-gs ]

-- Primitive recursion

pr :: PRFunction -> PRFunction -> PRFunction -> [Integer] -> Integer

pr rec g h (  0  :xs) = g xs
pr rec g h ((n+1):xs) = h ( (rec (n:xs)):n:xs )

----------- some examples of primitive recursive functions ------------

-- Predecessor
 
predd :: PRFunction
predd  = pr predd (const 0) (p 2) 

-- Addition

add :: PRFunction
add  = pr add (p 1) (compose s [(p 1)]) 

-- Test of equality to zero

isZero :: PRFunction
isZero  = pr isZero (const 1) (const 0) 

-- Test of positive

isPositive :: PRFunction
isPositive = pr isPositive (const 0) (const 1)

-- greater or equal

leq :: PRFunction
leq = compose isZero [sub]

-- smaller (<) and greater (>)

smaller :: PRFunction
smaller = compose isPositive [compose sub [(p 2), (p 1)]]

greater = compose isPositive [compose sub [(p 1), (p 2)]]

-- smaller or equal (<=)

geq :: PRFunction
geq = compose isZero [compose sub [(p 2), (p 1)]]

-- Multiplication

mul :: PRFunction
mul  = pr mul z (compose add [(p 1),(p 3)]) 

-- Subtraction

sub :: PRFunction
sub  = compose sub' [(p 2),(p 1)] 
       where
         sub'  = pr sub' (p 1) (compose predd [(p 1)]) 

-- Logical operators

nott :: PRFunction
nott  = isZero 

andd :: PRFunction
andd  = mul 

-- equal

eq :: PRFunction
eq  = compose andd [compose geq [(p 1), (p 2)], compose leq [(p 1), (p 2)]]

-- If then else

iff :: PRFunction
iff = pr iff (p 2) (p 3)

-- Conditional

cond :: PRFunction
cond = compose add [compose mul [(compose sub [(const 1), (compose isZero[(p 1)])]), 
                                  (p 2)], (compose mul [(compose isZero [(p 1)]), (p 3)])]

-- True if n is odd

odd2 :: PRFunction
odd2 = pr odd2 (const 0) (compose nott [(p 1)])

-- True if n is even

even2 :: PRFunction
even2 [n] = nott[odd2 [n]]

-- divide with 2

half :: PRFunction
half [n] = pr half (const 0) (compose add [(p 1),(compose odd2 [(p 2)])]) [n]

-- Factorial

fact :: PRFunction
fact [n] = pr fact (const 1) (compose mul [(p 1),(compose s [(p 2)])]) [n]

-- The idiv function is not defined for y=0

idiv :: PRFunction
idiv [x,y] = (compose idiv' [(p 1), (p 2), (p 1)]) [x,y]

idiv' :: PRFunction
idiv' [n,y,x] = pr idiv' g h [n,y,x]
               where
               g = const 0
               h = compose add [(p 1), compose isZero [ compose sub [ compose mul [compose s [(p 2)], (p 3)], (p 4)]]]

tupel :: PRFunction
tupel = compose add [compose half [compose mul [compose add [(p 1), (p 2)], compose add [(p 1), compose add [(p 2), (const 1)]]]], (p 2)]


