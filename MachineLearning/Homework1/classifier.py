import os
import math

dir_path = os.path.dirname(os.path.realpath(__file__))
trainingSetData = []
K_NN_COUNT = 1

def extractDataFromLine(line):
	line = line.replace(' \n', '') # clear final space and new line chars
	
	return list(map(float, line.split(' '))); # map line to a list of floats

def train():
	explicitPathRead = os.path.join(dir_path, './Dataset/train')
	f = open(explicitPathRead, 'r')

	for line in f:
		line = line.replace(' \n', '') # clear final space and new line chars
		currentDigitData = extractDataFromLine(line)
		trainingSetData.append(currentDigitData)

	# TODO: maybe construct a kd-tree out of data

def test():
	explicitPathRead = os.path.join(dir_path, './Dataset/test')
	f = open(explicitPathRead, 'r')

	errors = 0
	testsCount = 0

	for line in f:
		currentLineData = extractDataFromLine(line)
		result = classify(currentLineData)
		expected = str(int(currentLineData[0]))

		testsCount += 1
		if result != expected:
			errors += 1
		print("Tests completed: {}".format(testsCount))

	errorRate = errors / testsCount
	print("Error rate is {}%".format(errorRate * 100))

def classify(digitData):
	kNN = getKNN(trainingSetData, digitData, K_NN_COUNT)
	labelsOfKNN = list(map(getLabel, kNN)) # intentionally as integers, to make mapping easier
	digitRepetitions = [0 for x in range(10)]

	for label in labelsOfKNN:
		digitRepetitions[label] += 1

	return str(digitRepetitions.index(max(digitRepetitions)))


def getKNN(trainingSetData, digitData, k):
	knn = [] # distance to the kNN
	knnToDataMapper = {}

	for trainingDigitData in trainingSetData:
		currentDistance = getDistanceBetweenPoints(getCoords(digitData), getCoords(trainingDigitData))

		if len(knn) < k or max(knn) > currentDistance:
			knn.append(currentDistance)
			knn.sort()
			knnToDataMapper[currentDistance] = trainingDigitData
			knn = knn[:k]

	return list(map(lambda x: knnToDataMapper[x], knn))


def getCoords(data):
	return data[1:]

def getLabel(data):
	return int(data[0])

def getDistanceBetweenPoints(a, b):
	if len(a) != len(b):
		raise Exception('Points must be in the same n-dimensional space to calculate distance!')

	squares = 0

	for i in range(len(a)):
		squares += math.pow(a[i] - b[i], 2)

	return math.sqrt(squares)


train()
test()