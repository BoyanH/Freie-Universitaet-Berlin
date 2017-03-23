//Am Anfang dache ich, dass ich es nur für VerketteteSchlangen machen werde, da es trivial ist im anderen Fall
//dann habe ich listToQueue und QueueToList implementiert, gesehen dass es vollig ineffizient ist und deswegen später auch eine Implementierung speziell für Listen...ja

import java.io.*;

public class RadixSort {

	private static final byte lastLevel = 4;

	public static VerketteteSchlange fromListToQueue(VerketteteListe arr) {

		VerketteteSchlange queue = new VerketteteSchlange();

		queue.anhaengen(arr);

		return queue;
	}

	public static VerketteteListe reverseList(VerketteteListe arr) {

		Knoten currentElement = arr.eingang;

		arr.eingang = null;

		while(currentElement != null) {

			arr.einfuege(currentElement.wert);
			currentElement = currentElement.next;
		}

		return arr;
	}

	public static VerketteteListe fromQueueToList(VerketteteSchlange queue) {

		VerketteteListe arr = new VerketteteListe();
		Knoten currentElement = queue.eingang;

		while(currentElement != null) {

			arr.einfuege(currentElement.wert);
			currentElement = currentElement.next;
		}
		//cool, but it is in reversed order now

		return reverseList(arr);

	} 

															//byte is more than enough, we need only 1,2,3,4 and we have capacity of 2^8 numbers with byte
	private static int getNth8BitSequenceInNumber(int number, byte n) {

		//summary: from 0000 0000 0000 0000 0000 0000 0000 0000 (32-bits) gets the n-th 2 blocks of 4 bits(0000 0000) starting from right

		int lastBlockMask = 0xff;
		int nThBlockMask = lastBlockMask << 8*(n-1); //shift to the left to the nth 8-bit-sequence
													//example for n = 3
										//nThBlockMask = 0000 0000 1111 1111 0000 0000 0000 0000
		int sequence = number & nThBlockMask;

		return (sequence >> 8*(n-1)) & lastBlockMask; //we have our correct bit sequence but it looks like that for example with random number and 3. block:  
																												//0000 0000 0101 1101 0000 0000 0000 0000
													//but we need it like that 0101 1101. When finished we can cast it to byte 
	}

														//it looks better as a parameter and not hard-coded, dunno
	private static VerketteteSchlange[] createLists(int numberOfLists) {

		VerketteteSchlange[] queues = new VerketteteSchlange[numberOfLists];

		//initialize lists
		for (int i = 0; i < numberOfLists ; i++ ) {
			
			queues[i] = new VerketteteSchlange();
		}


		return queues;
	}

	private static void sortByLevel(VerketteteListe arr, byte level) {

		VerketteteSchlange[] lists = createLists(256);
		Knoten currentElement = arr.eingang;
		int currentValue;
		int currentBitSequence;

		while(currentElement != null) {

			currentValue = currentElement.wert;

			currentBitSequence = getNth8BitSequenceInNumber(currentValue, level);
			lists[currentBitSequence].anhaengen(currentValue);

			currentElement = currentElement.next;
		}

		concatenate(arr, lists, level);

		System.out.print("After " + level + "th 8-bit-pair sort, counting from right: ");
		arr.durchlaufe();
	}

	private static void concatenate(VerketteteSchlange arr, VerketteteSchlange[] lists, byte level) {

		//break all references to elements, same as deleting, but keep the same object so it is being changed after the function
		arr.eingang = null;
		arr.ende = null;

		if(level != lastLevel) {
			for(int i = 0; i < lists.length; i++) {

				arr.anhaengen(lists[i]);
			}
		}
		else {

			for(int i = 128; i < 256; i++) {

				arr.anhaengen(lists[i]);
			}

			for(int i = 0; i < 128; i++) {

				arr.anhaengen(lists[i]);
			}
		}
	}

	private static void concatenate(VerketteteListe arr, VerketteteSchlange[] lists, byte level) {

		VerketteteListe resultList;
		VerketteteSchlange queue = new VerketteteSchlange();

		concatenate(queue, lists, level);

		resultList = fromQueueToList(queue);

		arr.eingang = resultList.eingang;
	}

	//So macht man es mit queueToList und listToQueue

	// public static void sort(VerketteteListe arr) {

	// 	VerketteteSchlange queue = fromListToQueue(arr);
	// 	VerketteteListe result;
		
	// 	sort(queue);

	// 	result = fromQueueToList(queue);

	// 	arr.eingang = result.eingang; //thats all it takes to convert one list to another
	// }

	public static void sort(VerketteteListe arr) {

		//hmm yeah, long story short, thought it was better to call the last block my 1-bock and the 1st the '4'th block, so yeah, not very professional 
		for(byte i = 1; i <= 4; i++) {

			sortByLevel(arr, i);
		}
	}


	public static void main(String[] args) {

		// VerketteteListe arr = new VerketteteListe(); //NOTE: Läuft auch mit Listen
		VerketteteSchlange arr = new VerketteteSchlange();
		IntReader in;
        int x, newInt;

        try{
        	in = new IntReader(args);
        }
        catch(Exception e) {

        	System.out.println("Could not open reader");
        	return;
        }

		System.out.println("Please enter each member of the list followed by enter and then ctrl+Z and enter for end");

        try { 
        	
        	while(true) { 

        		newInt = in.readInt();

            	arr.anhaengen(newInt);
			}
        }
        catch (EOFException e) {

        }
        catch(IOException e) {

        	System.out.println("Unexpected error occured while reading from stream");
        }

        //cool input
		//arr.einfuege(255);
		// arr.einfuege(1);
		// arr.einfuege(256);
		// arr.einfuege(65536);
		// arr.einfuege(16777216);
		// arr.einfuege(-4);

		sort(arr);
		arr.durchlaufe();
	}
}