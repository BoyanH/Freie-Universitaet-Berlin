module Queue (Queue, enqueue, dequeue, isEmpty, makeQueue, (===), Eq, Ord, Show) where

	--good practise is to put all signatures at the beginning of the module for better code readability
	enqueue :: Queue a -> a -> Queue a
	dequeue :: Queue a -> (Queue a, a)
	isEmpty :: Queue a -> Bool
	makeQueue :: Queue a



	data Queue a = Q [a] [a] 	--the Queue type is basically 2 lists;
								--1. for elements to be popped, 2. with lastly pushed elements;
								--each in the appropriate for these functions order;
								--element is popped from the beginning of 1. list, pushed to the beginning of 2.
								--it is done so in order to keep the proper waiting order in the queue.
								--A newly pushed element should first wait for all the elements in the 1. list to be popped
								--and then the ones before it in the 2. List before its turn comes



	--push the new element to the beginning of the 2. List of the data type, as defined in our task
	enqueue (Q firstInQueue lastInQueue) elmnt = Q firstInQueue (elmnt:lastInQueue)

	--remove one element from the beginning of the 1. List as requested in the task
	dequeue (Q [] []) = error "Cannot dequeue an empty queue!" --it is important to check if the stack is empty and throw an error in that case
	
	--in this case, move the elements from 2. List to 1. in reverse order, as lastly pushed elements are at the beginning of the 2. List, 
	--but they must be at the end of the first; After that, call the function again recursively to do the actual pop/dequeue
	dequeue (Q [] lastInQueue) = dequeue (Q (reverse lastInQueue) [])
	dequeue (Q (first:firstInQueue) lastInQueue) = (Q firstInQueue lastInQueue, first) --most general case, simply pop from 1. List

	--an empty queue has its both lists empty, otherwise it is not empty
	isEmpty (Q [] []) = True
	isEmpty _ = False

	makeQueue = Q [] [] --simply call the Queue constructor with two empty Lists

	--we are using functions, defined outside of the instance to be able to give a signature to them, as requested
	--in the intance, we only declare those functions as equal to those in the class
	instance (Show a) => Show (Queue a) where
		show = showQueue


	showQueue :: (Show a) => Queue a -> [Char]
	showQueue (Q [] []) = "Empty Queue" --queue with no elements has no elements to display. Simply tell it's empty
	showQueue queue = showInOrder 1 queue --call the helper function with 1 as element counter
				where
					showInOrder order (Q [] []) = "" --bottom of recursion
					--if there are no more elements in the 1. List, switch lists and reverse as in dequeue
					showInOrder order (Q [] lastInQueue) = showInOrder order (Q (reverse lastInQueue) [])
					--otherwise, display each element on a new line with "n:" before it, telling its position in queue
					showInOrder order (Q (first:firstInQueue) lastInQueue) = "\t \t" ++ (show order) ++ ".: \
						\" ++ (show first) ++ "\n" ++ showInOrder (order + 1) (Q firstInQueue lastInQueue)	 

	--as above, only declare our functions defined below as equal to those, expected from the class
	instance (Eq a) => Eq (Queue a) where
		(==) = eqQueue
		(/=) queueA queueB = not (eqQueue queueA queueB)

	eqQueue :: (Eq a) => (Queue a) -> (Queue a) -> Bool
	eqQueue (Q fA []) (Q fB []) = length fA == length fB --if all elements are in the 1. list, compare lengths
	--otherwise, move them to the 1. List and call the function recursively
	eqQueue (Q firstA lastA) (Q firstB lastB) = eqQueue (Q (firstA ++ (reverse lastA)) []) (Q (firstB ++ (reverse lastB)) [])

	--compares not only length, but each element. Inspired by === operator in JavaScript, where a === b = a == b && typeof a == typeof b
	(===) :: (Eq a) => (Queue a) -> (Queue a) -> Bool
	(===) (Q fA []) (Q fB []) = fA == fB
	(===) (Q firstA lastA) (Q firstB lastB) = (Q (firstA ++ (reverse lastA)) []) === (Q (firstB ++ (reverse lastB)) [])  


	--in this instance we define the (>) operator as equal to gQueue, and (>=) as equal to geQueue 
																--(for perfomance, not to calculate length twice)
	--then we use this to define all other comparissons
	--Golden Rule Of Programming: Never repeat yourself
	instance (Ord a) => Ord (Queue a) where
		(>) = gQueue
		(>=) = geQueue
		(<) a b = not (geQueue a b)
		(<=) a b = not (gQueue a b)


	--absolutely equivalent to eqQueue, only for > and >=
	gQueue :: Queue a -> Queue a -> Bool
	gQueue (Q fA []) (Q fB []) = length fA > length fB
	gQueue (Q firstA lastA) (Q firstB lastB) = gQueue (Q (firstA ++ (reverse lastA)) []) (Q (firstB ++ (reverse lastB)) [])

	geQueue :: Queue a -> Queue a -> Bool
	geQueue (Q fA []) (Q fB []) = length fA >= length fB
	geQueue (Q firstA lastA) (Q firstB lastB) = geQueue (Q (firstA ++ (reverse lastA)) []) (Q (firstB ++ (reverse lastB)) [])