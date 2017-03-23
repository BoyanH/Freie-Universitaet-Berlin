def fak(n,s=1):
	"Berechne n!*s. wenn s nicht angegeben ist, berechne n!"
	if n<0:
		raise ValueError("negatives Argument",n)
	if n==0:
		return s
	return fak(n-1,s*n)



# def pseudo_fak(n,s=1):
# 	"Berechne n!*s. wenn s nicht angegeben ist, berechne n!"
# 	if n<0:
# 		raise ValueError("negatives Argument",n)

	
# 	if(n==0):
# 		goto End
#
# 	Fak_Loop:
# 		s *= n     !IMPORTANT: the sequence first multiply s and then decrement n is important, this is the true order of operations
# 		n -= 1
#
# 		if n!=0:			
# 			goto Fak_Loop
#
# 	End:
# 		return s


# negative values in both functions return an error, as negative factorials are undefned
def iterative_fak(n,s=1):

	if n<0:
		raise ValueError("negatives Argument",n)

	while(n != 0):
		s *= n
		n -= 1

	return s


print("-"*30)

print("iterative_fak(0,1) == 0!*2 == 2: ", iterative_fak(0,2) == 2)
print("iterative_fak(3,1) == 5!*2 == 6: ", iterative_fak(3,1) == 6)
print("iterative_fak(3,7) == 5!*2 == 42: ", iterative_fak(3,7) == 42)
print("iterative_fak(5,1) == 5!*2 == 120: ", iterative_fak(5,1) == 120)
print("iterative_fak(5,3) == 5!*2 == 360: ", iterative_fak(5,3) == 360)
print("iterative_fak(8,1) == 5!*2 == 40320: ", iterative_fak(8,1) == 40320)
print("iterative_fak(8,4) == 5!*2 == 161280: ", iterative_fak(8,4) == 161280)

print("-"*30,"\n\n")

print("iterative_fak(0,1) == 0!*2 == 2: ",iterative_fak(0,2) == fak(0,2))
print("iterative_fak(3,1) == fak(3,1): ", iterative_fak(3,1) == fak(3,1))
print("iterative_fak(3,7) == fak(3,7): ", iterative_fak(3,7) == fak(3,7))
print("iterative_fak(5,1) == fak(5,1): ", iterative_fak(5,1) == fak(5,1))
print("iterative_fak(5,3) == fak(5,3): ", iterative_fak(5,3) == fak(5,3))
print("iterative_fak(8,1) == fak(8,1): ", iterative_fak(8,1) == fak(8,1))
print("iterative_fak(8,4) == fak(8,4): ", iterative_fak(8,4) == fak(8,4))