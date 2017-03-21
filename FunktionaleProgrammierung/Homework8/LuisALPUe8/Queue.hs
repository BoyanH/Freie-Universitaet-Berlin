module Queue (Queue, makeQueue, isEmpty, enqueue, dequeue) where
 
data Queue a = QueueInternal [a] -- opaque!
	
qrev xs = loop xs []
	where
	loop [] ys = ys
	loop (x:xs) ys = loop xs (x:ys)
	
instance Show a => Show (Queue a)
	where
	show (QueueInternal x) = showQueue [] 0 x
		where
		showQueue string cnt [] = string
		showQueue [] 0 (x:xs) = showQueue ("#0\t" ++ (show x)) 1 xs
		showQueue string cnt (x:xs) = showQueue newstring (cnt +1) xs
			where
			newstring = "#" ++ (show cnt) ++ "\t" ++ (show x) ++ "\n" ++ string
			
instance Eq (Queue a)
	where
	(==) (QueueInternal x) (QueueInternal y) = (length x == length y)
	
instance Ord (Queue a)
	where
	(<=) (QueueInternal x) (QueueInternal y) = (length x <= length y)
	(>=) (QueueInternal x) (QueueInternal y) = (length x >= length y)
	(>) (QueueInternal x) (QueueInternal y) = (length x > length y)
	(<) (QueueInternal x) (QueueInternal y) = (length x < length y)
		
makeQueue :: (Show a) => Queue a
makeQueue = QueueInternal []

isEmpty :: (Show a) => Queue a -> Bool
isEmpty (QueueInternal x)
	| length x == 0 = True
	| otherwise = False

enqueue :: (Show a) => a -> Queue a -> Queue a
enqueue x (QueueInternal y) = QueueInternal (x:y)

dequeue :: (Show a) => Queue a -> Queue a
dequeue (QueueInternal y) = QueueInternal (tail (qrev y))