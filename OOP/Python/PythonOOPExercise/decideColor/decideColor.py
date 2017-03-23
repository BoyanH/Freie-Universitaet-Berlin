from turtle import *
import math

class ColourfulSquare:

	def __init__(self, x, y, size):

		self.coords = (x,y)
		self.sideLength = size

	def getIncrementedCoords(self, numberToAdd):

		return (self.coords[0] + numberToAdd, self.coords[1] + numberToAdd)

	def isUnderDiagonal(self, coords):

		xSideLength = coords[0] - self.coords[0]
		alpha = math.radians(45) #diagonals in square

		heightOfPoint = (self.sideLength - xSideLength) * math.tan(alpha)

		return heightOfPoint >= coords[1]


	def draw(self):

		ownDrawer = Turtle()
		ownDrawer.speed(2000)
		ownDrawer.pensize(10)

		for i in range((self.coords[0] + self.sideLength)//10) :

			for j in range((self.coords[1] + self.sideLength) // 10):

				crntCoords = (i*10,j*10)
				crntColor = self.decide_color(crntCoords)
				ownDrawer.pencolor(crntColor)

				ownDrawer.penup()
				ownDrawer.goto(crntCoords)
				ownDrawer.pendown()
				ownDrawer.dot()

				print(crntCoords)
		pass

	def decide_color (self, coords):

		innerSquareCoords = self.getIncrementedCoords(self.sideLength / 3)
		isInInnreSquare = (coords[0] >= innerSquareCoords[0]) and (coords[0] <= innerSquareCoords[0] + self.sideLength / 3) and \
							(coords[1] >= innerSquareCoords[1]) and (coords[1] <= innerSquareCoords[1] + self.sideLength / 3)
		isUnderDiagonal = self.isUnderDiagonal(coords)

		if isInInnreSquare:

			return "white"
		elif isUnderDiagonal:
			return "black"
		else:
			return "gray"





square = ColourfulSquare(0,0,200)
square.draw()

while True:
	pass