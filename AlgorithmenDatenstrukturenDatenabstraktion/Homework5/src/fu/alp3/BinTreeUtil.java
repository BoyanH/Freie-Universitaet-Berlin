package fu.alp3;

import java.util.*;

/**
 * Created by Boyan on 11/24/2016.
 */

public class BinTreeUtil {

    public static <E> void outputBT(BinTree<E> bt) {

        StringBuilder printString = new StringBuilder();
        addToPrintString(bt.root(), printString); //call recursive function with the root as parameter

        System.out.println(printString);
    }

    private static <E> void addToPrintString(BTNode<E> bt, StringBuilder sb) {

        sb.append(bt.element()); //add the element to the string

        if(bt.leftChild() != null || bt.rightChild() != null) {

            //if there are left or right children it means we need to open a bracket (per definition)

            sb.append("(");

            addToPrintString(bt.leftChild(), sb); //add the left child's tree to the string
            sb.append(","); //separate by comma
            addToPrintString(bt.rightChild(), sb); //add the whole right child's tree as well

            sb.append(")"); //close the bracket
        }
    }

    public static BinTree<Integer>  createRealBinaryTreeFromString(String btAsString) {

        //[root, leftOfRoot, rightOfRoot, leftOf(leftOfRoot), rightOf(leftOfRoot), leftOf(rightOfRoot), rightOf(rightOfRoot), ...]

        String[] btAsStringArr = btAsString.replace("(", "#(#").replace(",", "#,#")
                                                .replace(")", "#)#").split("#");

                                //Really, really sorry for this. Still didn't have any time to learn java regex patterns
                                //and this one was not as trivial. I wanted to split the String on each such character
                                //but not remove them. Otherwise I would receive a single value of "89" as ['8','9']

        BinTree<Integer> bt = new BinTree<>();
        Stack<BTNode<Integer>> lastNodeStack = new Stack<>();
        BTNode<Integer> currentNode = bt.root();


        /*
        * We use a stack here to keep the upper elements in it, so we can always climb up the tree when needed.
        *
        * The idea is:
        *
        * we find a '(', then we create two children and go down to the left one and continue from there
        *
        * we find a ',', then we go to the right sibling  (constant time, cool BinTree implementation in that aspect)
        *
        * we find a ')', then we climb up the tree using the stack's last element
        *
        *
        * setting values is trivial...
        * */

        for(int i = 0; i < btAsStringArr.length; i++) {

            String crntString = btAsStringArr[i];

            if(crntString.equals("")) continue; //because of poor string splitting, sorry!

            if(crntString.equals("(")) {

                bt.expandExternalNode(currentNode);

                lastNodeStack.push(currentNode);
                currentNode = bt.leftChild(currentNode);
            }
            else if(crntString.equals(")")) {

                currentNode = lastNodeStack.pop();
            }
            else if(crntString.equals(",")) {

                currentNode = bt.sibling(currentNode);
            }
            else {

                currentNode.setElement(Integer.parseInt(crntString));
            }
        }

        return  bt;
    }

    public static void depth(BinTree<Integer> bt) {

        /*
        * We are using stack traversal where it can be easily implemented to prevent excessive recursion.
        *
        * The idea is to push the root to the stack and start from there. When we discover new elements, we push
        * them on the stack to be ready for further processing on the next loop. The trick here is to set the
        * root elements depth first (0) and then, on each node's processing, read the element's depth and
        * set its children's depth at the same time (if there are any).
        * */

        Stack<BTNode<Integer>> traverseStack = new Stack<>();
        BTNode<Integer> currentElement = bt.root();

        currentElement.setElement(0);
        traverseStack.push(currentElement);

        while(!traverseStack.isEmpty()) {

            currentElement = traverseStack.pop();

            BTNode<Integer> leftChild = bt.leftChild(currentElement);
            BTNode<Integer> rightChild = bt.rightChild(currentElement);

            if(leftChild != null) {

                leftChild.setElement(currentElement.element() + 1);
                traverseStack.push(leftChild);
            }

            if(rightChild != null) {

                rightChild.setElement(currentElement.element() + 1);
                traverseStack.push(rightChild);
            }
        }
    }

    public static <E> int getHeight(BinTree<E> bt) {

        return getHeightFromNode(bt, bt.root());
    }

    private static <E> int getHeightFromNode(BinTree<E> bt, BTNode<E> node) {

        BTNode<E> leftChild = bt.leftChild(node);
        BTNode<E> rightChild = bt.rightChild(node);

        int heightOfLeftTree = 0;
        int heightOfRightTree = 0;

        if(leftChild != null) {

            heightOfLeftTree = getHeightFromNode(bt, leftChild);
        }

        if(rightChild != null) {

            heightOfRightTree = getHeightFromNode(bt, rightChild);
        }

        return Math.max(heightOfLeftTree, heightOfRightTree) + (bt.isLeaf(node) ? 0 : 1 );
    }

    public static void height (BinTree<Integer> bt) {

        /*
        * A little more tricky as depth, but the logic is the same, unfortunately in 2n steps.
        * We need to calculate the height first, then each element down the tree hast the height of its
        * parent - 1. For further explanations, see depth.
        * */

        int heightOfGivenBt = getHeight(bt);

        Stack<BTNode<Integer>> traverseStack = new Stack<>();
        BTNode<Integer> currentElement = bt.root();

        currentElement.setElement(heightOfGivenBt);
        traverseStack.push(currentElement);

        while(!traverseStack.isEmpty()) {

            currentElement = traverseStack.pop();

            BTNode<Integer> leftChild = bt.leftChild(currentElement);
            BTNode<Integer> rightChild = bt.rightChild(currentElement);

            if(leftChild != null) {

                leftChild.setElement(currentElement.element() - 1);
                traverseStack.push(leftChild);
            }

            if(rightChild != null) {

                rightChild.setElement(currentElement.element() - 1);
                traverseStack.push(rightChild);
            }
        }
    }

    public static <E> List<E> preOrder(BinTree<E> t) {

        /*
        * Summary: postOrder is easy to implement using stack tree traversal
        * As we find new nodes, we push them on the stack. On each loop execution we pop an element from the stack,
        * add its value to the list and push the right and then the left child.
        * Here the push order is important, as we need to add the left child before the right in preOrder.
        * As we are using a stack (LIFO), the way to read the left child first is to push it last.
        * */

        ArrayList<E> preOrderList = new ArrayList<E>();
        Stack<BTNode<E>> traverseStack = new Stack<>();

        traverseStack.push(t.root());

        while(!traverseStack.isEmpty()) {

            BTNode<E> currentElement = traverseStack.pop();

            if(currentElement == null) continue;

            preOrderList.add(currentElement.element());
            traverseStack.push(t.rightChild(currentElement));
            traverseStack.push(t.leftChild(currentElement));
        }

        return preOrderList;
    }

    public static <E> List<E> postOrder(BinTree<E> t) {

        ArrayList<E> postOrderList = new ArrayList<>();

        //parse the tree, currentElement and list to be filled to the recursive helper function
        addPostOrderElement(t, t.root(), postOrderList);

        return postOrderList;
    }

    private static <E> void addPostOrderElement(BinTree<E> t, BTNode<E> node, List<E> list) {

        /*
        * Summary: for post order, we first go as far as possible in the left tree recursively, then in the right one
        * when we find a leaf, the recursion adds the current element
        * */

        if(t.leftChild(node) != null) {

            addPostOrderElement(t, t.leftChild(node), list);
        }

        if(t.rightChild(node) != null) {

            addPostOrderElement(t, t.rightChild(node), list);
        }

        list.add(node.element());
    }
}
