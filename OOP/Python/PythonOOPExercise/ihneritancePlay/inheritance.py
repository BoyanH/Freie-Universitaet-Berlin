class Mamal:
	def __init__(self, name):
		
		self.setName(name)
		print("I'm a mamal!")

	def setName(self, name):

		if(len(name) <= 2):
			raise ValueError("Sry ma nigga, da name iz too damn short!")

		self.__name = name

	def getName(self):

		return self.__name

	def getParentClass(self):
		bases = self.__class__.__bases__
		return bases[0].__name__

	def __str__(self):

		stringified = self.getName() + ", a " + self.getParentClass() + " of type " + self.__class__.__name__

		return stringified

class Human(Mamal):

	def __init__(self, name):

		super().__init__(name)
		print("I'm a human!")


boyan = Human("Boyan")
boyan.setName("Ninja developer")
boyan.__name = 'asdlasddf'
print(boyan.getName())

print(boyan)

class Student(Human):

	pass

student = Student("Nedo")

print(student)