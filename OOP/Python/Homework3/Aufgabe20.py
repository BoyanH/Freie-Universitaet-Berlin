while B:
	S
	if C:
		T
		if D:
			U
			V 	#V is only executed, when D is true, therefore could easily be moved in the statement
	if (not C) | D
		W 		#W is executed, if C is false, as we have no chance of getting in the code-block with continue statement, 
				#OR if C is true and D is true as well, so we don't get in this evil else block


while B & C: 	#if C is false we only execute S and U, then brake, so we can reconstructor our code
	S
	if C:
		T
		V 		#V is executed only if C is True, otherwise we enter the else-statement and break the loop

if B & (not C): #loop breaked because of C, execute S and U
	S
	U
else:
	W 				#loop didn't break, execute W