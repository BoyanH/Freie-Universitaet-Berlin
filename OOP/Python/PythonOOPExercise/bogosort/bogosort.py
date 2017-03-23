import random

def isSorted(arr):

	for i in range(len(arr)-1):
		if arr[i] > arr[i+1]:
			return False

	return True

def shuffleArray(arr):

	switchedIndexes = []
	arrLen = len(arr)

	while(len(switchedIndexes) < arrLen):
		
		randA = random.randint(0, arrLen-1)
		while(randA in switchedIndexes):
			randA = random.randint(0, arrLen-1)

		randB = random.randint(0, arrLen-1)
		while(randB in switchedIndexes):
			randB = random.randint(0, arrLen-1)

		switchedIndexes.append(randA)
		switchedIndexes.append(randB)
		arr[randA], arr[randB] = arr[randB], arr[randA]
	


def bogoSort(arr):

	tries = 0

	while(not isSorted(arr)):
		shuffleArray(arr)
		tries += 1

	print("Sorted array in ", tries, " tries.")
	return arr

arr = [23,123,32,1,2,5,123]

bogoSort(arr)
print(arr)