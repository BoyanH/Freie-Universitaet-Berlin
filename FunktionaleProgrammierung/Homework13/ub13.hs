import PRF

{-
	Funktionale Programmierung

	Übungsblatt 5 (Abgabe: Mi., den 02.12. um 10:10 Uhr)
	Author: Boyan Hristov, Luis Herrmann
	Tutor: Zachrau, Alexander
	Tutorium: Dienstag; 12:00 - 14:00
-}

{-
----------------------------------------------------------------------------------
	Exercise 1
----------------------------------------------------------------------------------
-}

{-
	a)

		  2
	g = C
		  1

				 3   3
	h = mult o [P , P ]
                 1   2

-}


powPR :: PRFunction
powPR = pr powPR (const 1) (compose mul [(p 1), (p 3)])

{-
	b)
					2		   2   2
	maxPR = sub o [P , sub o [P , P ]]
					1		   2   1
-}

maxPR :: PRFunction
maxPR = compose add [(p 1), compose sub [(p 2), (p 1)]]

minPR :: PRFunction
minPR = compose sub [(p 2), compose sub [(p 2), (p 1)] ]


{-
----------------------------------------------------------------------------------
	Exercise 2
----------------------------------------------------------------------------------
-}


pFunc :: PRFunction
pFunc = const 1

hFunc :: PRFunction
hFunc = const 2

kFunc :: PRFunction
kFunc = const 3


fPR :: PRFunction
fPR = compose add [compose mul [compose pFunc [(p 1)], compose hFunc [(p 3), (p 1), (p 2)]], compose kFunc [(p 3)] ]

{-
----------------------------------------------------------------------------------
	Exercise 3
----------------------------------------------------------------------------------
-}

{-
	a)

-}

andPR :: PRFunction
andPR = pr andPR (const 0) (p 3) 

{-
	b)

-}

equalPR :: PRFunction
equalPR = compose sub [compose s [compose minPR [(p 1), (p 2)]], compose maxPR [(p 1), (p 2)] ]


{-
----------------------------------------------------------------------------------
	Exercise 4
----------------------------------------------------------------------------------
-}

{-
	a)

-}

exFourPR :: PRFunction
exFourPR = compose add [  (p 1), compose idiv [ compose mul [ compose add [(p 1), (p 3)], compose add [compose add [(p 3), (p 2)], (const 2)] ], (const 2) ] ]

{-
	b)

-}

exFourPPR :: PRFunction
exFourPPR = compose sub [compose powPR [(p 2), (p 1)], (const 1)]

{-
	c)

-}

abst :: PRFunction
abst = compose add [compose sub [(p 1), (p 2)], compose sub [(p 2), (p 1)] ]

{-
	d)

-}

exFourDPR :: PRFunction
exFourDPR = pr exFourDPR (const 1) (compose add [(p 1), compose s [(p 2)]])