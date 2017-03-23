def mul(a0,b0):
	a,b = a0,b0
	s = 0
	while a!=0:
		assert s + a*b == a0 * b0, "Oh no, something went wrong"
		if a%2==1:
			s += b
		a = a//2
		b = b*2
	return s

print(mul(13,2))