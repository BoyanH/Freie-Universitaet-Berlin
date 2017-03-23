def finde(array, element): #why not find people, wtf
	assert (type(array) == list), "List expected as input!"

	indexOfSearchedElement = -1

	for i in range(0, len(array)):
		if array[i] == element:
			indexOfSearchedElement = i
			break

	assert (indexOfSearchedElement == -1 or array[indexOfSearchedElement] == element), "Something went terribly wrong!" 

	return indexOfSearchedElement


def findeUsingWhile(array, element):
	assert (type(array) == list), "List expected as input!"	

	indexInArray = 0
	lenOfArray = len(array)
	indexOfSearchedElement = -1

	while(indexInArray < lenOfArray and array[indexInArray] != element):

		if (indexInArray == lenOfArray - 1) and (array[indexInArray] != element): #last element
			#element not found, break loop in order not to execute the else statement
			break
		indexInArray += 1
	else:
		return indexInArray

	return -1

print("findeUsingWhile([4,23,1,23,23,123,13], 123): ", findeUsingWhile([4,23,1,23,23,123,13], 123))
print("findeUsingWhile([4,23,1,23,23,123,13], 123): ", findeUsingWhile([4,23,1,23,23,123,13], 48))

print("findeUsingWhile() == finde(): ", findeUsingWhile([4,23,1,23,23,123,13], 48) == finde([4,23,1,23,23,123,13], 48))
