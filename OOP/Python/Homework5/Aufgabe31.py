def f1(a):
	a = a + [a] 	#here a new local variable a is being initialized with the value a + [a], NOT copied by reference
					#so as remains the same

def f2(a):
	b = a 			#here b ist defined as a, so a reference to a is saved in b
					#therefore changes in a change b as well and vice versa, as
					#they point to the same thing in memory
	

	b.append(7)		#therefore 7 is appended to a as well

def f3(a):
	b = a + ['88'] 	#b is now a new array, not a reference to a, with '88' as last element in the list
	a.append([a,b]) #we appen a new list out of a and b to a

a=[4,5,6]

f1(a) 				#nothing happens
print(a)

f2(a)				#7 is appended to a, because of reference-assigning
print(a)

f3(a)				#because of appending, [[[4,5,6,7], [4,5,6,7,'88']]] is appended to a
print(a)

print(a[-1][-1][-1]) #thats how we get 88, because:


#after appending [[[4,5,6,7], [4,5,6,7,'88']]] to a, which btw already is [4,5,6,7] after f2. in f3 we get the list
#[4,5,6,7,[[[4,5,6,7], [4,5,6,7,'88']]]]
#its last element ist the lest appended list, so a[-1] = [[[4,5,6,7], [4,5,6,7,'88']]]

#now we need the last element of it, which is the non-nested list a + ['88'] = [4,5,6,7,'88']
#so we are at a[-1][-1] so far

#we just need the last element of this now, '88', so at the end we get a[-1][-1][-1]