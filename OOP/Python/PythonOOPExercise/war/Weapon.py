from Nameful import Nameful

class Weapon(Nameful):

	maxWeaponDamage = 20

	def __init__(self, name, damage, weight):

		self.setName(name)
		self.setDamage(damage)
		self.setWeight(weight)

	def setWeight(self, weight):

		if isinstance(weight, int):
			self.__weight = weight
		else:
			raise ValueError("weight must be an Integer")
	def getWeight(self):
		return self.__weight

	def getDamage(self):
		return self.__damage
		
	def setDamage(self, damage):
		if damage <= self.maxWeaponDamage:
			self.__damage = damage
		else:
			raise ValueError("A weapon cannot be that damn powerful!")