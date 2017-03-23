module UTM where
-- ALP-I
-- Turing Maschine Simulator
-- leicht veraendert von Prof. Dr. Margarita Esponda-Argüero (2010)
-- aus dem original von Prof. Dr. Raul Rojas (1999)

data Move = L | R | S
              deriving (Show, Eq)

step :: (Int, [Char], Char, [Char]) -> [(Int, Char, Int, Char, Move)] -> (Int, [Char], Char, [Char])
step (q,l,input,r) state_table 
                 | q==0      = (q,l,input,r)
                 | otherwise    = (qn,new_l,new_input,new_r)
                   where
                     (qn,out,dir) = find_next (q,input) state_table
                     (new_l,new_input,new_r) 
                                   | dir==L = move_left(l,out,r) 
                                   | dir==R = move_right(l,out,r)
                                   | dir==S = (l,out,r)
                              
find_next :: (Int, Char) -> [(Int, Char, Int, Char, Move)] -> (Int, Char, Move)
find_next (p,input) [] = error ("END OF STATE-TABLE and not Matching founded; State: " ++ show p ++ "; Input: " ++ show input)
find_next (q,input) ((a,b,qn,out,dir):rest) 
                    | (a==q && b==input)    = (qn,out,dir)
                    | (a==q && b=='#')      = (qn,input,dir)
                    | otherwise             = find_next (q,input) rest

empty_tape :: [Char]
empty_tape = '_':empty_tape

move_left :: ([Char], Char, [Char]) ->  ([Char], Char, [Char])
move_left (l,s,r) 
                   | head_l == '_' = error "left margin reached"
                   | otherwise     = (tail l,head_l,s:r)
                     where head_l = head l

move_right :: ([Char], Char, [Char]) ->  ([Char], Char, [Char])
move_right(l,s,r) 
                   | head_r == '_' = error "right margin reached"
                   | otherwise     = (s:l,head_r,tail r)
                     where head_r = head r

sim :: (Int,[Char],Char,[Char]) -> [(Int,Char,Int,Char,Move)] -> [(Int,[Char],Char,[Char])] -> [(Int,[Char],Char,[Char])]
sim (0,l,input,r) state_table history = history
sim (q,l,input,r) state_table history = sim new state_table (new:history)
                           where new = step (q,l,input,r) state_table

start (q,l,input,r) state_table 
   = putStr(show_states(sim (q1,l1,s1,r1) state_table [(q1,l1,s1,r1)]))
        where (q1,l1,s1,r1) = (q,reverse(l)++empty_tape,input,r++empty_tape)

-- auxiliary functions

show_states [] = ""
show_states (x:xs) = (show_states xs)++"  "++show_state(x)

show_state (q,l,input,r) = show(q)++"  "++rev_l++[input]++cut_tape r++"\n"
                       ++ spaces (5+length(rev_l))++"T\n"
                          where rev_l = reverse_finite l []

spaces n = [' '|_<-[1..n]]

cut_tape ('_':x) = []
cut_tape (x:y) = x:(cut_tape y)

reverse_finite ('_':x) y = y
reverse_finite (x:xs)  y = reverse_finite xs (x:y)

-- Vorlesungsbeispiele

add1 = [ (1, 'A', 1, 'A', L),
         (1, '0', 2, '1', L),
         (1, '1', 1, '0', L),
         (1, 'B', 0, 'F', S),
         (2, '0', 2, '0', L),
         (2, '1', 2, '1', L),
         (2, 'B', 0, 'B', S)
       ]

div2 = [ (1, 'A', 1, 'A', R),
         (1, '0', 1, '0', R),
         (1, '1', 2, '0', R),
         (2, '1', 2, '1', R),
         (2, '0', 1, '1', R),
         (1, 'H', 0, 'H', S),  -- 0 ist Endzustand
         (2, 'H', 0, 'H', S)   -- 0 ist Endzustand
       ]

-- The program checks balancing of parenthesis

klammer= [(1,'A',2,'A',R),
          (2,'(',2,'(',R),
          (2,'X',2,'X',R),
          (2,')',3,'X',L),
          (2,'B',4,'B',L),
          (3,'X',3,'X',L),
          (3,'(',2,'X',R),
          (3,'A',0,'0',S),
          (4,'X',4,'X',L),
          (4,'(',0,'0',S),
          (4,'A',0,'1',S)
         ]

unary_number_copy = [ (1,'1',1,'A',S),
                      (1,'A',2,'A',R),
                      (1,'0',6,'0',L),
                      (2,'1',2,'1',R),
                      (2,'0',3,'0',R),
                      (3,'1',3,'1',R),
                      (3,'0',4,'1',S),
                      (4,'1',4,'1',L),
                      (4,'0',5,'0',L),
                      (5,'1',5,'1',L),
                      (5,'A',1,'A',R),
                      (6,'1',6,'1',L),
                      (6,'A',6,'1',S),
                      (6,'0',0,'0',R)
                    ]
           
-- tests

test1 = start (1,"_",'A',"(()())B_") klammer
test2 = start (1,"_",'A',"0111H_") div2
test4 = start (1,"B00110111",'A',"_") add1
test5 = start (1,"_00",'1',"1100000000_") unary_number_copy


------------------ Universelle-Turing-Maschine nach Minsky -------------------


minsky_tm = [ (1,'0',1,'A',L), -- P0, Za
			  (1,'1',1,'B',L),
			  (1,'Y',2,'Y',R),
			  (1,'#',1,'#',L),
			  
			  (2,'A',3,'0',R), -- P1, Z1
			  (2,'B',4,'1',R),
			  (2,'X',7,'X',R),
			  (2,'#',2,'#',R),
			  
			  (3,'0',5,'A',L), -- P1, Z2
			  (3,'1',6,'B',R),
			  (3,'#',3,'#',R),
			  
			  (4,'0',6,'A',R), -- P1, Z3
			  (4,'1',5,'B',L),
			  (4,'#',4,'#',R),
			  
			  (5,'Y',2,'Y',R), -- P1, Z4
			  (5,'#',5,'#',L),
			  
			  (6,'X',1,'X',L), -- P1, Z5
			  (6,'Y',0,'Y',S),
			  (6,'#',6,'#',R),
			  
			  (7,'0',8,'A',L), -- P2, Z6
			  (7,'1',9,'B',L),
			  (7,'#',7,'#',R),
			  
			  (8,'Y',10,'Y',R), -- P2, Z7
			  (8,'#',8,'#',L),
			  
			  (9,'Y',11,'Y',R), -- P2, Z8
			  (9,'#',9,'#',L),
			  
			  (10,'0',12,'A',R), -- P2, Z9
			  (10,'1',12,'A',R),
			  (10,'X',13,'X',L),
			  (10,'#',10,'#',R),
			  
			  (11,'0',12,'B',R), -- P2, Z10
			  (11,'1',12,'B',R),
			  (11,'X',14,'X',L),
			  (11,'#',11,'#',R),
			  
			  (12,'X',7,'X',R), -- P2, Z11
			  (12,'#',12,'#',R),
			  
			  (13,'M',15,'A',R), -- P3, Z12
			  (13,'#',13,'#',L),
			  
			  (14,'M',15,'B',R), -- P3, Z13
			  (14,'#',14,'#',L),
			  
			  (15,'A',15,'0',R), -- P3, Z14
			  (15,'B',15,'1',R),
			  (15,'X',16,'X',R),
			  (15,'#',15,'#',R),
			  
			  (16,'0',17,'0',L), -- P3, Z15
			  (16,'1',17,'1',L),
			  (16,'Y',17,'Y',L),
			  (16,'#',16,'#',R),
			  
			  (17,'A',17,'0',L), -- P3, Z16
			  (17,'B',17,'1',L),
			  (17,'0',18,'S',L),
			  (17,'1',19,'S',L),
			  (17,'#',17,'#',L),
			  
			  (18,'A',21,'0',L), -- P4, Z17
			  (18,'B',20,'0',R),
			  (18,'#',18,'#',L),
			  
			  (19,'A',21,'1',L), -- P4, Z18
			  (19,'B',20,'1',R),
			  (19,'#',19,'#',L),
			  
			  (20,'0',22,'M',R), -- P4, Z19
			  (20,'1',23,'M',R),
			  (20,'#',20,'#',R),
			  
			  (21,'0',22,'M',R), -- P4, Z20
			  (21,'1',23,'M',R),
			  (21,'#',21,'#',L),
			  
			  (22,'S',1,'A',L), -- P4, Z21
			  (22,'#',22,'#',R),
			  
			  (23,'S',1,'B',L), -- P4, Z22
			  (23,'#',23,'#',R)
			]
			
-- TM nach Minsky
-- Umwandlung in Nullen
test6 = start (1,"_M11Y0010",'X',"001000101X001100101Y_") minsky_tm