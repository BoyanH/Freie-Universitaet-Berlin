#sorry for the mix of english and german, I actually find writing code in everything but english a bad practise

stifte = {}
target = ''

def Hanoi(n, Start, Hilfsstift, Ziel):
	
	"""Turm von Hanoi
	
	Bewege n Scheiben, die zu Beginn auf dem Stift "Start" sind,
	zum Stift "Ziel"; "Hilfsstift" ist der dritte Stift."""

	if n==0: return #done
	Hanoi(n-1, Start, Ziel, Hilfsstift)

	setze_um(n, Start, Ziel)

	Hanoi(n-1, Hilfsstift, Start, Ziel)
	   
def setze_um(n, x, y):
	
	print("Setze Scheibe",n,"von Stift",x,"auf Stift",y)

	if stifte[x].index(n) != len(stifte[x]) - 1: #if it's not the most upper element, it cannot be moved according to Hanoi's rules
		raise RuntimeError("Du kannst nur die oberste Scheibe bewegen!")
	elif len(stifte[y]) != 0 and stifte[y][-1] < n: #in case the stack is not empty and the existing last piece is smaller than the one attempted to be place, throw an exception (raise)
		raise RuntimeError("Die bewegte scheibe ist grosser als die schon liegende!")
	else:
		crntElement = stifte[x].pop() #remove the last from the old stack
		stifte[y].append(crntElement) #place it in the new stack
		anzeigen()					  #make sure we show the user how cool we move elements
		print('Fertig: ', fertig())

	

def init_Hanoi (n, x, y, z):
	global stifte, target

	stifte[x] = [i for i in range(n, 0, -1)] #initialize the start stack, with numbers in reversed order
	stifte[y] = []
	stifte[z] = []

	target = z

	anzeigen()
	Hanoi(n, x, y, z)

def anzeigen():
	
	print("-"*30)

	for key in sorted(stifte.keys()): #sort the keys to make sure we print them in correct order; it is a solution for abc, need to use global vars for 
									  #non-alphabetically sorted stacks (decided it's not required in task, as it makes no sense to name the stack in non-folowwing order)
		print(key, ": ", stifte[key])

	print("-"*30)

def fertig():
	global stifte, target

	sortedCount = 0

	for i in stifte.keys():
		if len(stifte[i]) == 0:
			sortedCount += 1

	return sorted(stifte[target])[::-1] == stifte[target] and sortedCount == len(stifte.keys()) - 1


#no tests written, as everything runs and is being called
init_Hanoi(5, 'A', 'B', 'C')