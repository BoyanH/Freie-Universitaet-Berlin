package fu.alp3;

import java.util.LinkedList;

public class MWayNode {

    public int[] A;
    public MWayNode parent;
    public LinkedList<MWayNode> children;

    public MWayNode() {

        this.A = new int[5]; //initialize the keys array
        this.A[0] = 0; //has no children yet
        this.children = new LinkedList<MWayNode>();
    }

    public MWayNode(int key) {

        this();
        this.createNodeWithNKeys(new int[] {key});
    }

    public MWayNode(int keyOne, int keyTwo) {


        this();

                                //TODO: find Java's equivalent of javascript's function.apply(this, argument1, argument2, ...)
        this.createNodeWithNKeys(new int[] {keyOne, keyTwo});
    }

    public MWayNode(int keyOne, int keyTwo, int keyThree) {

        this();
        this.createNodeWithNKeys(new int[] {keyOne, keyTwo, keyThree});
    }

    private void createNodeWithNKeys(int[] keys) {


        this.A[0] = keys.length;

        for(int i = 0; i < keys.length; i++) {

            this.A[i + 1] = keys[i];
            this.createChildWithIndex(i);
        }

        //the number of children is equal to the number of keys + 1
        this.createChildWithIndex(keys.length);
    }

    private void createChildWithIndex(int idx) {

        MWayNode newNode = new MWayNode();
        newNode.parent = this;

        this.children.add(idx, newNode);
    }

    public boolean isLeaf() {

        return this.A[0] == 0;
    }

    public void print() {

        StringBuilder sb = new StringBuilder();

        printRecursively(this, sb);
        System.out.println(sb.toString());
    }

    private static void printRecursively(MWayNode node, StringBuilder sb) {

        int numberOfKeys = node.A[0];

        if(node.isLeaf()) {

            sb.append("()");
            return;
        }

        sb.append("(");
        for(int i = 0; i < numberOfKeys; i++) {

            sb.append(node.A[i + 1] + (i == numberOfKeys - 1 ? "" : ", "));
        }
        sb.append(")");

        sb.append("[");
        for(int idx = 0; idx < numberOfKeys + 1; idx++) {

            printRecursively(node.children.get(idx), sb);
            sb.append(idx == numberOfKeys ? "" : ", ");
        }
        sb.append("]");
    }

    public int getNumberOfChildren() {

        return this.A[0] + 1;
    }

    public boolean check_2_4_Baum() {

        boolean isTwoFourTree = true;

        //NOTE: here we sacrificed some runtime to separate the single steps and make our checks more readable

        //1. check if all nodes on the left have smaller keys and all on the right - larger
        isTwoFourTree = isTwoFourTree && this.checkKeysCorrectness(Integer.MIN_VALUE, Integer.MAX_VALUE);

        //2. check if each node has between 2 and 4 children (between 1 and 3 keys as well)
        isTwoFourTree = isTwoFourTree && this.checkKeysAmount();

        //3. check if all leaves have the same depth
        isTwoFourTree = isTwoFourTree && this.doAllLeavesHaveTheSameDepth();

        return isTwoFourTree;
    }

    private boolean checkKeysCorrectness(int minValue, int maxValue) {

        int numberOfKeys = this.A[0];

        if(this.isLeaf()) return true;

        //check if all keys in the current node fit the criteria
        for(int i = 0; i < numberOfKeys; i++) {

            if(this.A[i + 1] < minValue || this.A[i+1] > maxValue) return false;
        }

        //check if all children also have correct keys
        for(int idx = 0; idx < numberOfKeys + 1; idx++) {

            int newMin = idx == 0 ? minValue : this.A[idx];
            int newMax = idx == numberOfKeys ? maxValue : this.A[idx + 1];

            if(! this.children.get(idx).checkKeysCorrectness(newMin, newMax)) return false;
        }

        return true;
    }

    private boolean checkKeysAmount() {

        if(this.A[0] > 3 || this.isLeaf() && this.children.size() > 0 || this.children.size() < 2 && !this.isLeaf()) return false;

        for(int i = 0; i < children.size(); i++) {

            if(! this.children.get(i).checkKeysAmount() ) return false;
        }

        return true;
    }

    private boolean doAllLeavesHaveTheSameDepth() {


        int maxDepth = this.getTreeHeight();

        return allLeavesHaveDepthOf(maxDepth, 0);
    }

    private boolean allLeavesHaveDepthOf(int maxDepth, int currentDepth) {

        if(this.isLeaf() && currentDepth != maxDepth) return false;

        for(int i = 0; i < this.children.size(); i++) {

            if(! this.children.get(i).allLeavesHaveDepthOf(maxDepth, currentDepth + 1)) return false;
        }

        return true;
    }

    private int getTreeHeight() {

        int maxHeightOfChildren = Integer.MIN_VALUE;
        int heightOfCurrentChild;

        if(this.isLeaf()) return 0;

        for(int i = 0; i < children.size(); i++) {

            heightOfCurrentChild = this.children.get(i).getTreeHeight();
            maxHeightOfChildren = Math.max(maxHeightOfChildren, heightOfCurrentChild);
        }

        return 1 + maxHeightOfChildren;
    }

    public void insertKey(int key) {

        int numberOfKeysInCurrentNode = this.A[0];
        MWayNode correspondingChild = new MWayNode(); //initialize to stop compiler from complaining, not really a clean solution ;/


        if(this.isLeaf()) {

            this.parent.insertKeyForReal(key);
            return;
        }

        if(key <= this.A[1]) {

            correspondingChild = this.children.get(0);
        }

        //while the given key is smaller than the current one, keep going right the children chain
        for(int i = 0; i < numberOfKeysInCurrentNode; i++) {

            if(key < this.A[i+1]) {

                break;
            }

            correspondingChild = this.children.get(i + 1);
        }

        correspondingChild.insertKey(key);
    }

    private void insertKeyForReal(int key) {

        int numOfKeys = this.A[0];
        int crntIndex = 0;
        int temp = 0;
        MWayNode newChild = new MWayNode();

        newChild.parent = this;

        while(crntIndex < numOfKeys && key > this.A[crntIndex + 1]) crntIndex++;

        this.children.add(crntIndex, newChild);

        temp = this.A[crntIndex + 1];
        this.A[crntIndex + 1] = key;
        this.A[0]++;

        for(int i = crntIndex + 1; i < numOfKeys+1; i++) {

            //xor swap to push all values further in to the tail of the array
            temp ^= this.A[i + 1];
            this.A[i + 1] ^= temp;
            temp ^= this.A[i + 1];
        }
    }
}
