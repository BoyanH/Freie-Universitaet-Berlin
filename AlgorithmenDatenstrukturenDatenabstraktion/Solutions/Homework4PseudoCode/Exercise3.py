# Idea is simple - we traverse the tree using a stack. That's a very famous way of traversing trees and the perfect for our ocasion. Let's get started!

# We initialize our 2 main variables. First comes the sum of all depths, or in other words each leaf's depth will be added to this variable to later on calculate
#an average based on that. The leaf count will store the number of all leafs the tree has (node that with the current pseudocode this algorithm works 
# for any binary tree). So the idea is to find the correct answer to those 2 questions and then calculate sumOfAllDepths / leafCount.

#Now let's talk about the stack - the idea is, that the moment a new leaf is found, it is stored in the stack together with its depth where it will later on
# get processed. The idea here is that we only go once down the tree and each node is being pushed/poped to/from the stack only once. We do however carry all
# the information with it - its depth in case it turns out it's a leaf node and the node itself, in case we need to traverse further down the tree.

#Why did we choose a stack? It allows us to use an iterative aproach instead of a recursive one and therefore we can easily use our variables to store things, which
# we would otherwise have to pass around as arguments and try to come up with a solution how to not lose these variables. Here everything is simple - we have a LIFO
# queue, where all the nodes are being added to as they are being discovered. When the queue is empty, we visited all. As a node turns out to be a leaf, add its
# depth to the sumOfAllDepths counter and increment the leafCount counter. Easy!

#Bei Fragen - fragen :D


findAverageDepthOfLeafs(root):

	sumOfAllDepths = 0;
	leafCount = 0;

	Stack treeStack = new Stack();
	treeStack.push(root, 0);			//push pairs to the stack, each pairs consists of element and its depth

	while(treeStack not empty):

		currentPair = treeStack.pop();


		if(currentPair.node.isLeaf):

			sumOfAllDepths += currentPair.depth;
			leafCount++;
			continue;



		#We don`t really have to check the conditions here, as its a real binary tree, therefor
		#each node has exactly 2 or none children; As it`s not a leaf, it should have 2 children

		if(currentPair.node.leftChild != null):

			treeStack.push(currentPair.node.leftChild, currentPair.depth + 1)

		if(currentPair.node.rightChild != null):

			treeStack.push(currentPair.node.leftChild, currentPair.depth + 1)


	return sumOfAllDepths / leafCount;