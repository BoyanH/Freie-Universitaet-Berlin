class CurrencyRelation:

	localCurrency = ""
	foreignCurrency = ""
	localValue = ""
	foreignValue = ""

	def __init__(self, localCurrency, foreignCurrency, localValue, foreignValue, possibleCurrencyValues):
		self.localCurrency = localCurrency
		self.foreignCurrency = foreignCurrency
		
		self.possibleCurrencyValues = possibleCurrencyValues

		self.localValue = self.getFixedLengthValue(localValue) #cast to strings and then pass to function, in order to make them with fixed length
		self.foreignValue = self.getFixedLengthValue(foreignValue)
 
	#there could be a function for it already defined, but it's a small task anyways
	def getFixedLengthValue(self, value):

		value = "%.2f" % value #cast number to string with exactly two digits after decimal point

		fixedLength = len(max(self.possibleCurrencyValues).__str__()) + 3 #.xx are 3 characters

		while(len(value) < fixedLength):

			value = " " + value

		return value

	#override the default toString() function of an object, in order to easily print currency pairs
	def __str__(self):

		returnString = self.localValue + self.localCurrency + ' = ' + self.foreignValue + ' ' + self.foreignCurrency

		return returnString


class CurrencyInformer:

	#default values, which will be parsed as default arguments to the constructor
	currencyValues = [1, 2, 5, 10, 20, 50, 100, 500, 1000, 10000, 20000] #sorry, couldn't come up with a way to describe the sequence without hardcoding it
	maxObservedValue = 100
	localCurrencyString = "EUR"

	#initializing variables
	foreignCurrency = ""
	exchangeRate = 0.00
	currencyTable = []

	#the constructor, which we use as a configurator for the program
	#we packed everything in a class, so we can save settings, change if needed...etc.
	def __init__(self, foreignCurrency, exchangeRate, newCurValues = currencyValues, newMaxObsValue = maxObservedValue, newLocalCurStr = localCurrencyString):
		self.currencyValues = newCurValues
		self.maxObservedValue = newMaxObsValue
		self.localCurrency = newLocalCurStr

		self.foreignCurrency = foreignCurrency
		self.exchangeRate = exchangeRate

	def getRoundCurrencyTransfers(self):
		localCurrencyValue = 0.01
		currencyTable = []


		#keep adding 0.01 (the minimum step, so we can display correct results with .00 precision) euro and check for round numbers
		while(localCurrencyValue <= self.maxObservedValue):

			#rounding is required because:
				#1) the way floats are saved in memory(sign + exponent + significant / Mantisse, Vorzeichen, Bias) won't get in details, we need to round
				#	our local currency to prevent big mistakes while always adding 0.01 (common example for such mistakes is 0.05 + 0.01)
				#2) we will almost never get an EXACT match
			localCurrencyValue = round(localCurrencyValue, 2)
			foreigCurrencyValue = round(localCurrencyValue * self.exchangeRate, 2) #don't be so picky

			#if any of the two values in our two currencies are 'round'
			if(self.isARoundCurrencyValue(localCurrencyValue) or self.isARoundCurrencyValue(foreigCurrencyValue)):

				#we need to use objects, in order to both save pairs and remember their sequence, therefore dictionary doesn't work
				currencyTable.append(CurrencyRelation(self.localCurrency, self.foreignCurrency, localCurrencyValue, foreigCurrencyValue, self.currencyValues))
			
			localCurrencyValue += 0.01

		return currencyTable

	def isARoundCurrencyValue(self, value):									 #for code readability mainly
		return value in self.currencyValues

	# default 3 columns
	def drawCurrencyTable(self, cols = 3, inColumns = True):

		currencyTable = self.getRoundCurrencyTransfers()
		printBuffer = ""
		colCounter = 1

		for value in currencyTable:
			seperator = ' | ' if colCounter != cols else ' ' #separator to put behind each number to make the lines of the table
			printBuffer += value.__str__() + seperator
			if(colCounter == cols or not inColumns):
				colCounter = 0				#set it to zero, as we are incrementing at the end of the loop, therefore in same cycle
				printBuffer += '\n'

			colCounter += 1
		print(printBuffer)
		print("\n Number of matches:", len(printBuffer))

currencyInformer = CurrencyInformer('GBP', .01)
currencyInformer.drawCurrencyTable()

print("\n \n \n And once without columns, to see the order is just fine: \n \n \n")
currencyInformer.drawCurrencyTable(inColumns=False)



# b) 
# Das minimale Anzahl von Beträge bekommt man entwedr bei 1, wobei nur diese für 'round' Euro vorkommen, da diese für diese
# Fremdwehrung gleich sind ODER bei Wechselkurs <= 0.01/100 = 0.0001, da wir bis 100 Euro überprüfen und bis dahin sind alle
# Beträge in der Fremdwehrung kleine als 0.01 als nicht darstellbar. Also 7 für alle runde Eurobeträge

#Theoretisch, weil ohne Rundung wir nichts bekommen und wir Rundung nutzen, bekommen wir maximale Anzahl von Beträge bei 0.01
# Wechselkurs, da dabei 1 Euro = 0.01 ist, also darstellbare Zahl. Die Änderungen sind aber so klein, dass 2 Beträge für den
# selben Betrag sich ergeben können. Bei mir sind das 1606 Beträge

#Sonnst, wenn es hier gar nich um Darstellung von Floats geht, sind das maximal l + k Einträge, jeder Rundwert in jede Wehrung,
#wobei l = Betrachtungswert von Euro (bis wie viel wir rechnen) und k maximale Betrag in Fremdwehrung