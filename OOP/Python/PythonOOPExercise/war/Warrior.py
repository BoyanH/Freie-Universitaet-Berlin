from Weapon import Weapon
from Nameful import Nameful

class Warrior(Nameful):

	def __init__(self, name, weapon):
		
		self.health = 100
		self.stamina = 100
		self.dead = False

		self.setName(name)
		self.setWeapon(weapon)

	def getWeapon(self):
		return self.__weapon

	def setWeapon(self, weapon):
		if(isinstance(weapon, Weapon)):
			self.__weapon = weapon
		else:
			raise ValueError("Not everything is a weapon!")

	def takeHit(self, damage):

		self.health -= damage

		if(self.health <= 0):
			self.dead = True

		print("Argh, I've been hit! My health now is: " + str(self.health) + "\n")

	def relax(self):
		self.stamina += 1


	def attack(self, enemy):

		ownDamage = self.getWeapon().getDamage()
		ownWeight = self.getWeapon().getWeight()

		if self.stamina >= ownWeight:
			self.stamina -= ownWeight

			print("Yarrr, I hit this son of a bitch for good! My stamina now is: " + str(self.stamina) + "\n")

			enemy.takeHit(ownDamage)
		else:
			print(":P I'm too exhausted to hit")


	def __str__(self):
		representation = "Hi, i'm the mighty " + self.getName() + " and I carry a " + self.getWeapon().getName() + "\n health: " + str(self.health) + '; stamina: ' + str(self.stamina)

		return representation