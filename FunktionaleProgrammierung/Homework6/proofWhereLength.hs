test times = recFct 0 0
		where
			len = length [1..99999999]
			recFct n count
						| count == times = n
						| otherwise = recFct (n + len - 99999998) (count+1)

test2 times = recFct 0 0 [1..9999]
		where
			recFct n count xs
				| count == times = n
				| otherwise = recFct (n + (length xs) - 9998) (count+1) xs

{-

*Main> test2 99999
99999
(3.08 secs, 141,939,488 bytes)
*Main> test 99999
99999
(1.73 secs, 8,035,337,456 bytes)
*Main>

-}