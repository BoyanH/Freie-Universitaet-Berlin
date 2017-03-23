from turtle import *
import math

#a)

class Point:

	def __init__(self, x, y):
		self.x = x
		self.y = y

class Verfolger(Turtle):

	ziel = Point(0,0)

	def __init__(self, ziel):
		super().__init__()		#for newbies: super is the way to go. It gives us the class we inherit from. This way we provide abstraction, not hardcoding
								#the parent constructor + we can easily change the class we inherit from, without the need to change
								#every parent method we use
		self.ziel = ziel

		turtlePosition = self.position()
		differenceX = self.ziel.x - turtlePosition[0]
		differenceY = self.ziel.y - turtlePosition[1]

		#decided I will show what stays behind the turtle towards. Look at exercise c) for the drawing if you don't love math
		angleInDegrees = math.atan(differenceY / differenceX) * 180 / math.pi
		self.setheading(angleInDegrees)

	def verfloge(self, schrittweite):

		self.forward(schrittweite)

targetPoint = Point(1448, 890)

# t = Verfolger(targetPoint)
# t.verfloge(150)


# b)

class Rechteck:

	def __init__(self, l, r, u, o):
		self.left_corner = l
		self.right_corner = r
		self.lower_corner = u
		self.upper_corner = o

		self.ownDrawer = Turtle()

	def zeichne(self): #def draw(self):

		self.ownDrawer.up()
		self.ownDrawer.goto(self.left_corner, self.lower_corner)
		self.ownDrawer.down()

		for i in range(0, 2): #nothing much, calculate a and b of the rectangle, draw them and then repeat for the other 2 sides
			self.ownDrawer.forward(self.right_corner - self.left_corner)
			self.ownDrawer.left(90)
			self.ownDrawer.forward(self.upper_corner - self.lower_corner)
			self.ownDrawer.left(90)


rectangle = Rechteck(-300, 300, -200, 200) #this is the rectangle we use later on as boundary for the run/chase thing

#c)

class RunningTurtle(Turtle):

	def __init__(self, begrenzung = None):
		super().__init__()

		self.setBoundaries(begrenzung)

	def setBoundaries(self, boundaries):

		if(isinstance(boundaries, Rechteck)):
			self.boundaries = boundaries
			self.boundaries.zeichne()

	def lauf_in_richtung(self, schrittweite, richtung): #the main logic is here, lauf_weg and chase will use this function

		"""
			Move as far as you can in this direction without hitting a wall, than continue recursively in another direction untill you
			manage to make all the stepps you need to
		"""

		#see in which direction we are moving, will be important for decisions later on
		runningRight = richtung <= 90 or richtung >= 270
		runningUp = richtung < 180	or richtung > 360

		#initialize our distance variables. 
		distanceToXEnd = 0
		distanceToYEnd = 0

		#get distance until left/right top/lower end of rectangle in straight line
		#depending on the direction we are running in, left/right side line will be closer
		#analog for top/bottom
		if runningRight:
			distanceToXEnd = self.boundaries.right_corner - self.position()[0]
		else:
			distanceToXEnd = self.position()[0] - self.boundaries.left_corner

		if runningUp:
			distanceToYEnd = self.boundaries.upper_corner - self.position()[1]
		else:
			distanceToYEnd = self.position()[1] - self.boundaries.lower_corner

		#then, using the distance in straight line and the angle we are running at, calculate the distance remaining until
		#we are forced to cross the rectange; It looks something like this where a is distanceToXEnd/distanceToYEnd

#       |\alpha 
# 		| \
# 		|  \
# 	  a |   \x
# 		|    \
# 		|     \
# 		|      \
# 		|90     \    x = a / sin(90 - alpha)
# 		__________
		# the idea behing this is to calculate collisions earlier, before starting a step, as turtle is slow and glitchy
		#otherwise, another way is to make a step of length 1 at a time and check for collisions
		distanceToXEnd /= abs(math.sin(90 - richtung)) if richtung != 90 else 1	#handle zero divison and miscalculated distances(negative ones)
		distanceToYEnd /= abs(math.sin(90 - richtung))  if richtung != 90 else 1
		
		distanceUntilBoundaries = min(distanceToXEnd, distanceToYEnd) #get the smaller distance, we don't want to crash in the wall after all


		#sorry for lazy hard-coded angles. Didn't come up with a formula to calculate this in a better way
		#idea is to always bounce up like a ball from the borders
		if distanceUntilBoundaries == distanceToYEnd and runningUp: #crossing top/bottom line

			newAngle = 225 if runningRight else 315
		elif distanceUntilBoundaries == distanceToYEnd and not runningUp:

			newAngle = 45 if runningRight else 135
		elif distanceUntilBoundaries == distanceToXEnd and runningRight: #crossing left/right line
			
			newAngle = 135 if runningUp else 225
		else:
			newAngle = 45 if runningUp else 315 


		#IMPORTANT: here is the main logic. Run as much as you can in this direction and as soon as you hit a wall, go in
		#another direction and finish the amount of steps you have left
		if distanceUntilBoundaries <= 0:
			self.lauf_in_richtung(schrittweite, newAngle)
		else:
			self.setheading(richtung)
			if(schrittweite <= distanceUntilBoundaries - 10):
				self.fd(schrittweite)
			else:
				self.fd(distanceUntilBoundaries - 10)
				self.lauf_in_richtung(schrittweite + 10, newAngle)

class Wegläufer(RunningTurtle):
	
	anzahl = 0

	def __init__(self, z, begrenzung = None):
		super().__init__(begrenzung)
		self.wovor = z
		Wegläufer.anzahl += 1

	def lauf_weg(self, schrittweite=30):
		
		richtung = 180 + self.towards(self.wovor.position()) #pretty straight forward, run away from the enemy
		self.lauf_in_richtung(schrittweite, richtung)		#GO!

class Chaser(RunningTurtle):

	def __init__(self, begrenzung = None):
		super().__init__(begrenzung)

	def chase(self, schrittweite=30):
		direction = self.towards(self.chased.position())	#set direction towards chased one
		self.lauf_in_richtung(schrittweite, direction)		#GO!


enemy = Chaser(rectangle)
enemy.up()
enemy.goto(30, 20)
enemy.down()


runner = Wegläufer(enemy, rectangle)
runner.shape("turtle")
runner.up()
runner.goto(20, 20)
runner.down()

enemy.chased = runner

#may the running away/chasing B.E.G.I.N.!!!
while True:
	runner.lauf_weg(5)
	enemy.chase(5)