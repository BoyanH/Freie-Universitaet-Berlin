def isSorted(arr):

	arrLength = len(arr)

	for i in range(arrLength-1):
		if arr[i] > arr[i+1]:
			return False

	return True

def bubblesort (arr):

	assert 	isinstance(arr, list), "Input must be a list!"
	
	end = len(arr) - 1

	#an important place for assertion is before a while loop, though this assertion is not needed, as it works correctly
	#without it as well, assertion for input = list was used in the beginning, needed for calculation of end
	# assert (len(arr) > 0), 'Input was either not a list or empty' 
	while end > 0:
		assert(end < len(arr)) #again, not really needed as end is assigned as len - 1, than it can recieve only value equal to an index in list
		
		lastChange = 0
		i = 0

		assert (i < end and lastChange == 0 and i == 0) #not needed again, but is theoretically an important place in the program
		while(i < end):
			#our most important assertion, the sublist after index end must be sorted, if there is such
			assert isSorted(arr[end:]), "Sublist with start index == index of last change is not sorted."
			if arr[i] > arr[i+1]:
				
				arr[i], arr[i+1] = arr[i+1], arr[i]
				lastChange = i
			i += 1
			assert lastChange < len(arr) #again, just to show this is an important place for an assertion in a programm, not needed

		assert (isSorted(arr[lastChange:])) #kind of useful, either it will stop at the end, or the numbers at bigger indexes will be greater than
											#the one at index, therefore sublist is sorted
		
		end = lastChange
		assert end < len(arr) #same thing

	#at the end of the outer loop, our list must already be sorted, check and throw an error if something went wrong, important assertion
	assert isSorted(arr), "Oh no, something went terribly wrong and array was not sorted correctly!"

	return arr #as we are passing the list by reference, we don't really need to return it, though it's more comfortable for passing
				#inline lists (anonymous lists) and saving the sorted one to a variable. Easier for 1-liners



arr = [1,8,7,2,3,4]

bubblesort(arr)

#if end = 0, that means the last changed element was 0 or there wasn't an element change. The second case is straight forward, no further changes needed.
#In 1 case, where the last changed element was the first, we don't really need to continue, as after the first two elements were sorted we
#went all the way to the end of the list and checked every pair, and every pair was in increasing order. And that is the definition
#of a sorted array. It is always true, that the array after the inex of last changed element is sorted.