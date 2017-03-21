fix :: (a -> a) -> a
fix r = r (fix r)

collatz :: (Int->[Int]) -> Int -> [Int]
collatz = (\clz n-> 
				if n == 1 
					then [n] 
				else if (n `mod` 2 == 0) 
					then n:(clz (n `div` 2)) 
				else n:(clz (n*3 + 1))
			)