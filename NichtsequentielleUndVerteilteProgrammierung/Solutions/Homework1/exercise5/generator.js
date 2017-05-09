var sequencePNames = ['p1', 'p2', 'p3'],
	sequenceRNames = ['r1', 'r2', 'r3'],

	sequenceCommands = {
		p1: 'x=1', 
		p2: 'y=2', 
		p3: 'z=1',
		r1: 'x=y-z', 
		r2: 'y=x', 
		r3: 'z=x+y'
	},
	generatedSequences = [];

function genFurtherSequences(seqA, seqB, sequenceArray) {

	// already picked all possible execution paths, this is a execution path bottom
	if (seqA.length === 0 && seqB.length === 0) {

		generatedSequences.push(sequenceArray);
	}

	if(seqA.length) {

		/* 
			Another execution sequence possibility, if A is executed this time and not B

			Chase down this possibility by creating a new scope for this case 
			(cloning both sequenceA and sequenceArray to pass the new variants to the recursion
			while keeping the values intact for the other cases (when B is executed first))
		*/

		var clonedSeqA = seqA.slice(0),
			command = clonedSeqA.pop(),
			sequenceArrayCopy = sequenceArray.slice(0);
		
		sequenceArrayCopy.push(command);
		genFurtherSequences(clonedSeqA, seqB, sequenceArrayCopy);
	}

	if(seqB.length) {

		var clonedSeqB = seqB.slice(0),
			command = clonedSeqB.pop(),
			sequenceArrayCopy = sequenceArray.slice(0);
		
		sequenceArrayCopy.push(command);
		genFurtherSequences(seqA, clonedSeqB, sequenceArrayCopy);
	}
}

function printSequence(executionSequence, seqCommands) {

	/*
		Initialize values as said in the exercise and execute current sequence.
		Then print the case on a new line
	*/

	var x = 0,
		y = 0,
		z = 0;

	executionSequence.forEach(function (command) {

		eval(seqCommands[command]);
	});

	console.log(`${executionSequence.join(' -> ')} -> (x = ${x}, y = ${y}, z=${z})`);
}

genFurtherSequences(sequencePNames.reverse(), sequenceRNames.reverse(), []);
generatedSequences.forEach(function (sequence) {

	printSequence(sequence, sequenceCommands);
});