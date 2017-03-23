def zuklein(a,i): #(auch min-heapify)

	pIdx = (i-1)//2 #short for index of parent element in tree

	if(pIdx < 0):
		return

	if a[i] < a[pIdx]:
		a[i], a[pIdx] = a[pIdx], a[i]
	
	zuklein(a, pIdx)


def zugross(a,i): #(auch max-heapify)

	lIdx = 2*i + 1 #short for index of left child element in tree

	if lIdx >= len(a):
		return
	elif lIdx == len(a) - 1:
		j = lIdx
	else:
		j = lIdx if a[lIdx] <= a[lIdx + 1] else lIdx + 1 
	
	if a[i] > a[j]:
		a[i], a[j] = a[j], a[i]
		zugross(a, j)

def heapsortDescending(a, n):

	arrLength = len(a)

	if n >= arrLength or n < 0:
		n = arrLength - 1
	
	minHeapify(a, n)

	while n:
		a[0], a[n] = a[n], a[0]
		zuklein(a, n-1)
		n -= 1

#Heapsort in aufnehmende Reihenfolge ist schwieriger mit diesem Pseudocode, da mit diesen Funktionen wir ein Min-Heap bekommen.
#Das Problem ist aber, das bei Min-Heap die kleinste Elemente davorne bleiben sollen, und nicht dahinten. Wir konnen aber nicht einfach
#von n-ten Element ab mit diesen Funktionen sortieren, da wir i übergeben, was nicht stimmt. Also z.B wollen wir ab 3 Element (Index) den Heap
#rekonstruieren. Dann übergeben wir 3, aber es fängt nicht von 4 und 5 an, sondern von 7 und 8.
#zu klein kann auch nicht benutzt werden, da wir dann die Elemente von hinten ab ordnen müssen und so die kleinste die großte werden
def heapsort(a,n):

	arrLength = len(a)

	if n > arrLength or n < 0:
		n = arrLength - 1

	heap = a[:n] + [] #make copy of a, so we don't change it unwillingly. As we are not returning any list from the function, we need to change a;
						#therefore, use heap as the heap and instead of filling a with elements, change elements after one another

	maxHeapify(heap, n) #create the heap

	for i in range(n):

		a[i] = heap[0] #take root of heap, place it in a, remove it from heap
		del heap[0]
		zugross(heap, 0) #and reheapify

	del heap #it will be "garbage-collected" anyways, but still


def minHeapify(a, n):

	while n:
		zuklein(a, n)
		n -= 1

def maxHeapify(a, n):

	for i in range(len(a)):
		zugross(a, i)


a = [6, 8, 2, 5, 13, 2]

print('a: ', a)


heapsort(a, len(a))
print('in ascending: ', a)

heapsortDescending(a, len(a))
print('in descending: ', a)