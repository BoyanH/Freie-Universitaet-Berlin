class Nameful:

	def __init__(self, name):
		self.setName(name)

	def setName(self, name):

		if isinstance(name, str):
			self.__name = name
		else:
			raise ValueError("Name should be string!")

	def getName(self):
		return self.__name