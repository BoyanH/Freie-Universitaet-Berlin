package fu.alp3;

/**
 * Created by Boyan on 11/30/2016.
 */

public class MyHeap {

    private int[] heap;
    private int lastElem; //requested variable name, is lastElemIndex really

    public MyHeap(int[] elements) {

        this.heap = elements.clone(); //clone the array so no unexpected behavior occurs
        this.lastElem = elements.length - 1;
        this.constructHeapBottomUp();
    }

    public MyHeap(int arrayLength) {

        this.heap = new int[arrayLength];
        this.lastElem = -1;
    }

    public int getLastElem() {

        return this.lastElem;
    }

    public void insert(int newElement) {

        if(this.isFull()) {

            throw new OutOfMemoryError("The heap is already full. No further elements can be added!");
        }

        this.lastElem++;
        this.heap[lastElem] = newElement;
        this.bubbleUp(lastElem);
    }

    public int deleteMin() {

        /*
         * Summary: returns the smallest element (the root element of the heap), puts the last element as root,
         *          reorders the heap and marks the new last element
          * */

        if(this.isEmpty()) {

            throw new RuntimeException("The heap is already empty. You cannot remove further elements from it!");
        }

        int minElement = this.heap[0];

        this.heap[0] = this.heap[this.lastElem];
        this.lastElem--;
        this.bubbleDown(0);

        return  minElement;
    }

    public void updateKey(int index, int key) {

        int previousKey = this.heap[index];
        this.heap[index] = key;

        if(previousKey < key) {

            bubbleDown(index);
        }
        else {

            bubbleUp(index);
        }
    }

    public boolean isFull() {

        return this.getLastElem() == heap.length - 1;
    }

    public boolean isEmpty() {

        return this.lastElem == -1;
    }

    public int[] getHeapAsArray() {

        return this.heap.clone();
    }

    public void printHeapAsArray() {

        System.out.print("Heap: ");
        printArray(this.getHeapAsArray());
    }

    //---------------------------------------Helper Methods----------------------------------------

    private void constructHeapBottomUp() {

        // Construct heap structure

        int lastInternalNode = this.getLastInternalNode();

        for (int i = lastInternalNode; i >= 0; i--) {

            this.bubbleDown(i);
        }
    }

    private void xorSwapInHeap(int idxA, int idxB) {

        /*
        * Basic swap algorithm using bitwise XOR operations. If you don't know it I suggest a quick google search.
        * */

        this.heap[idxA] ^= this.heap[idxB];
        this.heap[idxB] ^= this.heap[idxA];
        this.heap[idxA] ^= this.heap[idxB];
    }

    private void bubbleUp(int index) {

        int currentIndex = index;
        int parentNodeIndex;

        while(this.hasParentNode(currentIndex)) {

            parentNodeIndex = this.getParentNodeIndex(currentIndex);

            if(this.heap[currentIndex] < this.heap[parentNodeIndex]) {

                this.xorSwapInHeap(currentIndex, parentNodeIndex);
                currentIndex = parentNodeIndex;
            }
            else {

                //No need to go further up the tree if there was not swap needed on the last step
                break;
            }
        }
    }

    private void bubbleDown(int index) {

        int currentIndex = index;
        int leftChildIndex;
        int rightChildIndex;

        int minIndex;

        while(this.hasLeftChild(currentIndex)) {

            // Because of the definition of a heap if a right child exists, a left child will exist as well

            leftChildIndex = this.getLeftChild(currentIndex);
            minIndex = leftChildIndex;

            if (this.hasRightChild(currentIndex)) {

                rightChildIndex = this.getRightChild(currentIndex);

                minIndex = this.heap[leftChildIndex] < this.heap[rightChildIndex] ? leftChildIndex : rightChildIndex;
            }

            if(this.heap[minIndex] < this.heap[currentIndex]) {

                this.xorSwapInHeap(minIndex, currentIndex);
                currentIndex = minIndex;
            }
            else {

                //no need to continue if both children are bigger than the root
                break;
            }

        }

    }

    private boolean hasParentNode(int index) {

        return index > 0;
    }

    private int getParentNodeIndex(int index) {

        return (int) Math.floor( (index - 1) / 2);
    }

    private boolean hasLeftChild(int index) {

        int leftChildIndex = this.getLeftChild(index);
        boolean isOutOfBounds = leftChildIndex > this.getLastElem();

        return !isOutOfBounds;
    }

    private boolean hasRightChild(int index) {

        int rightChildIndex = this.getRightChild(index);
        boolean isOutOfBounds = rightChildIndex > this.getLastElem();

        return !isOutOfBounds;
    }

    private int getLeftChild(int index) {

        return index*2 + 1;
    }

    private int getRightChild(int index) {

        return index*2 + 2;
    }

    private int getLastInternalNode() {

        return (int)Math.floor(this.heap.length / 2);
    }

    //---------------------------------------Test Methods------------------------------------------

    public static int[] sort(int[] array) {

        MyHeap heap = new MyHeap(array);
        int[] outputArray = new int[array.length];

        for(int i = 0; i < outputArray.length; i++) {

            outputArray[i] = heap.deleteMin();
        }

        return outputArray;
    }

    public static void printArray(int[] arr) {

        System.out.print("[");
        for(int i = 0; i < arr.length; i++) {

            System.out.print(arr[i] + (i == arr.length - 1 ? "" : ", "));
        }
        System.out.println("]");
    }

    public static void main(String[] args) {

        int[] numbers = {4,2,6,1,9,7};
        MyHeap heap = new MyHeap(numbers);

        System.out.print("Numbers: ");
        printArray(numbers);
        System.out.print("sort(numbers): ");
        printArray(sort(numbers));

        heap.printHeapAsArray();
        System.out.println("heap.updateKey(3, 0)");
        heap.updateKey(3, 0);
        heap.printHeapAsArray();

        System.out.println("heap.deleteMin(): " + heap.deleteMin());
        heap.printHeapAsArray();

        System.out.println("heap.insert(3)");
        heap.insert(3);
        heap.printHeapAsArray();
    }
}
