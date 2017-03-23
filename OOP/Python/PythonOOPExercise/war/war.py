from Weapon import Weapon
from Warrior import Warrior


theTitle = Weapon("The Title", 6, 30)
john = Warrior('John Cena', theTitle)

headphones = Weapon("headphones", 3, 6)
nedoMaina = Warrior("Nedo, maina, sesh se,", headphones)

fightRound = 1

while (not john.dead) and (not nedoMaina.dead):

	print("Round " + str(fightRound) + ", GO!")

	john.attack(nedoMaina)
	nedoMaina.attack(john)

	john.relax()
	nedoMaina.relax()
	fightRound += 1

bothDead = nedoMaina.dead and john.dead




if not bothDead:
	print("Winner is: " + (nedoMaina.getName() if john.dead else john.getName()))
else:
	print("This was an epic battle with no survivors")
