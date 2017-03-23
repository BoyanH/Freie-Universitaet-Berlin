

# -----------------------------------------------
# I usually don't write that many comments, as I believe code is in most cases self-explanatory
# my previous Teacher wanted a lot of comments, just tell me if I can write less and I'd be glad :)
# -----------------------------------------------



# algorithm taken from lecture, as is
def ggT(a, b):
	a0,b0 = a,b
	ra,sa = 1,0
	rb,sb = 0,1

	while b > 0:
		assert (a == ra * a0 + sa * b0 and b == rb * a0 + sb * b0)
		q = a // b
		a,b = b, a - q*b
		ra, sa, rb, sb = rb, sb, ra - q*rb, sa - q*sb

	return a, ra, sa

#passing function as argument to tell which functon should be tested; default function is ggT
def testGGT(a,b, ggtFunction = ggT):
	
	t,r,s = ggtFunction(a,b)
	myGgt = 1 #smallest possible greatest dividor for every number, will be used to recalculate ggT, as we loop through the smaller dividors anyways


	#this algorithm returns a detailed solution anyways, use it to check if it's true
	#important to make that check while a and b are stil with their initial, maybe negative values
	if r*a + s*b != t:
		return False

	#make sure a and b are positive
	#here we use abs for cleaner code, in the reworked algorithm we will need to use if-else statements
	a = abs(a)
	b = abs(b)

	# (b) i)
	if a == 0 and t == b or b == 0 and t == a: #0 can be divided by everything
		return True 
	#first 2 conditions in this statement prevent ZeroDivisionError
	elif not a == 0 and not b == 0 and not (a % t == 0 and b % t == 0): #make sure t is really a divisor of both a and b
		return False

	for divisor in range(2, max(5,15) + 1): #1 is always a divisor, check all others to make sure t is really the greatest and is divided by all common divisors of a and b
		if a % divisor == 0 and b % divisor == 0:
			myGgt = divisor #find ggt to test if its equal to the output of the other function; we are doing this loop anyway

			# make sure every divisor of a and b is also a divisor of t (b) ii)
			if not t % divisor == 0:
				return False

	#testGGT(0,0) checks that t divides a and b, therefore causing an Exception; I added an if statement to make sure everything runs smoothly
	#testGGT(a,0) same as above, in case a = 0 we check if b = t

	#ggT(0,0) should be 0, as Infinity is not allowed in python
	#ggT(a,0) should be a; zero can be divided by everything without a rest

	return myGgt == t

# algorithm taken from lecture, reworked
def wholeGGT(a,b):

	#handle r and s for negative numbers, we use this flags to return correct r and s at the end
	signOfAChanged = False
	signOfBChanged = False

	#greatest divisors are always positive; negative numbers also have positive greatest common divisors
	if(a < 0): #if-else statements are used instead of abs to be able to set flags
		a *= -1
		signOfAChanged = True
	if(b < 0): 
		b *= -1
		signOfBChanged = True

	a0,b0 = a,b
	ra,sa = 1,0
	rb,sb = 0,1

	while b > 0:
		assert (a == ra * a0 + sa * b0 and b == rb * a0 + sb * b0)
		q = a // b
		a,b = b, a - q*b
		ra, sa, rb, sb = rb, sb, ra - q*rb, sa - q*sb


	#When negative numbers are parsed to this function, we change the signs; we have to change the signs of ra and sa as well, otherwise the output won't be correct
	if signOfAChanged:
		ra *= -1
	if signOfBChanged:
		sa *= -1

	return a, ra, sa


print("testGGT(10,15, ggT): ", testGGT(10,15, ggT))

print("testGGT(30,10, ggT): ", testGGT(30,10, ggT))

print("testGGT(10,15, ggT): ", testGGT(10,15, ggT))

print("testGGT(0,0, ggT): ", testGGT(0,0, ggT))


print("testGGT(5,0, ggT): ", testGGT(5,0, ggT))


print("testGGT(-5,0, ggT): ", testGGT(-5,0, ggT))


print("testGGT(-12,4, ggT): ", testGGT(-12,4, ggT))

print("testGGT(-15,-5, ggT): ", testGGT(-15,-5, ggT))


#---------------------------------

print("testGGT(10,15, wholeGGT): ", testGGT(10,15, wholeGGT))

print("testGGT(30,10, wholeGGT): ", testGGT(30,10, wholeGGT))

print("testGGT(10,15, wholeGGT): ", testGGT(10,15, wholeGGT))

print("testGGT(0,0, wholeGGT): ", testGGT(0,0, wholeGGT))

print("testGGT(5,0, wholeGGT): ", testGGT(5,0, wholeGGT))

print("testGGT(-5,0, wholeGGT): ", testGGT(-5,0, wholeGGT))

print("testGGT(-12,4, wholeGGT): ", testGGT(-12,4, wholeGGT))

print("testGGT(-15,-5, wholeGGT): ", testGGT(-15,-5, wholeGGT))

#--------------------------------

print("ggT(-12,4): ", ggT(-12,4))

print("ggT(-15,-5): ", ggT(-15,-5))

print("ggT(-5,0): ", ggT(-5,0))

#---------------------------------

print("wholeGGT(-12,4): ", wholeGGT(-12,4))

print("wholeGGT(-15,-5): ", wholeGGT(-15,-5))

print("wholeGGT(-5,0): ", wholeGGT(-5,0))
