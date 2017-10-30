import pandas as pd
import numpy as np
from operator import itemgetter
import matplotlib.pyplot as plt
import os
from Classifier import Classifier

class LinearRegressionClassifier(Classifier):


	# B = (X^TX)^-1X^T*y

	# hat H = X*B^ =` X(X^TX)-1X^T

	@staticmethod
	def pseudoInverse(X):
		if LinearRegressionClassifier.isInvertable(X):
			return np.linalg.inv(X)

		print("non invertable")
		return np.linalg.pinv(X) # TODO: implement yourself

	@staticmethod
	def getDetermeninat(X):
		return np.linalg.det(X) # TODO: implement yourself

	@staticmethod
	def isInvertable(X):
		return LinearRegressionClassifier.getDetermeninat(X) != 0


	def __init__(self, trainSet, testSet, classA, classB):
		self.classA = classA
		self.classB = classB

		trainSet = self.filterDataSet(trainSet)
		testSet = self.filterDataSet(testSet)

		self.trainData = list(map(lambda x: x[1:], trainSet))
		self.trainLabels = list(map(itemgetter(0), trainSet))
		self.testSet = list(map(lambda x: x[1:], testSet))
		self.testLabels = list(map(itemgetter(0), testSet))

		self.fit()

	def filterDataSet(self, dataSet):
		return list(filter(lambda x: int(x[0]) in [self.classA, self.classB], dataSet))

	def fit(self):
		ones = np.ones((len(self.trainData), 1), dtype=float)
		X = np.append(ones, self.trainData, axis = 1)
		xtInversed = LinearRegressionClassifier.pseudoInverse(X.T.dot(X))
		normalizedLabels = self.normalizeLabels(self.trainLabels)
		self.beta = xtInversed.dot(X.T).dot(normalizedLabels)
		# self.beta = self.trainSet

	def predict(self, X):
		X = np.append(np.array([1]), np.array(X), axis=0)
		return self.classA if (X.dot(self.beta) < 0) else self.classB

	def test(self):
		for i in range(len(self.testSet)):
			# print(self.predict(self.testSet[i]) == self.testLabels[i])
			assert(self.predict(self.testSet[i]) == self.testLabels[i])


	def normalizeLabels(self, labels):
		return list(map(lambda x: -1 if int(x) == self.classA else 1, labels))

def extractDataFromLine(line):
	line = line.replace(' \n', '') # clear final space and new line chars
	return list(map(float, line.split(' '))); # map line to a list of floats

def parseDataFromFile(file, dataArr):
	for line in file:
		line = line.replace(' \n', '') # clear final space and new line chars
		currentDigitData = extractDataFromLine(line)
		dataArr.append(currentDigitData)


trainSet = []
testSet = []

dir_path = os.path.dirname(os.path.realpath(__file__))
explicitPathTrainData = os.path.join(dir_path, './Dataset/train')
explicitPathTestData = os.path.join(dir_path, './Dataset/test')
trainFile = open(explicitPathTestData, 'r')
testFile = open(explicitPathTestData, 'r')

parseDataFromFile(trainFile, trainSet)
parseDataFromFile(testFile, testSet)

threeVsFiveClassifier = LinearRegressionClassifier(trainSet, testSet, 7, 8)
threeVsFiveClassifier.test()
# print(threeVsFiveClassifier.score())