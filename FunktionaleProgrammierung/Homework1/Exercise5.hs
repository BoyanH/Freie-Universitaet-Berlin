isBracket :: Char -> Bool
--For the char to be a bracket, it needs to be equalt to any of the given bracket symbols
isBracket c = (c == '(') || 
				(c == ')') || 
				(c == '[') || 
				(c == ']') || 
				(c == '{') || 
				(c == '}')