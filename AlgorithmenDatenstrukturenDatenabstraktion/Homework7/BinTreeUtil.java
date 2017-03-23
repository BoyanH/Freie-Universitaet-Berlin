package fu.alp3;

import java.util.Stack;

/**
 * Created by Boyan on 12/11/2016.
 */
public class BinTreeUtil {


    //createRealBinaryTreeFromString method was copied from our 5. homework
    public static BinTree  createRealBinaryTreeFromString(String btAsString) {

        //[root, leftOfRoot, rightOfRoot, leftOf(leftOfRoot), rightOf(leftOfRoot), leftOf(rightOfRoot), rightOf(rightOfRoot), ...]

        String[] btAsStringArr = btAsString.replace("(", "#(#").replace(",", "#,#")
                .replace(")", "#)#").split("#");

        //Really, really sorry for this. Still didn't have any time to learn java regex patterns
        //and this one was not as trivial. I wanted to split the String on each such character
        //but not remove them. Otherwise I would receive a single value of "89" as ['8','9']

        BinTree bt = new BinTree();
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

    public static boolean checkSearchTree(BinTree bt) {

        /*
        * 1. Tree is null -> everything fine
        * 2.1. left tree is a binary search tree -> everything fine
        * 2.2 right tree is a binary search tree -> everything fine
        *
        * 1 || (2.1 && 2.2) -> we have a binary search tree; else -> not a binary search tree
        * */

        return bt.root() == null ||
                (
                    (bt.root().leftChild() == null || checkSearchTree(bt.root().leftChild(), Integer.MIN_VALUE, bt.root().element())) &&
                    (bt.root().rightChild() == null || checkSearchTree(bt.root().rightChild(), bt.root().element(), Integer.MAX_VALUE))
                );
    }

    private static boolean checkSearchTree(BTNode<Integer> currentNode, Integer min, Integer max) {

        /*
        * 1. If element is null, or it is between the min and max set by parent elements -> everything fine
        * 2. If there are children and the leftChild is a search tree -> everything fine
        * 3. If there are children and the rightChild is a search tree -> everything fine
        *
        * 1 && 2 && 3 -> return true; else -> return false
        * */

        return  (currentNode.element() == null || currentNode.element() >= min && currentNode.element() <= max) &&
                (currentNode.isLeaf() || 
                	(checkSearchTree(currentNode.leftChild(), min, currentNode.element())
                	&& checkSearchTree(currentNode.rightChild(), currentNode.element(), max))
                );
    }

    public static boolean checkHBE(BinTree bt) {

        return bt.root() == null || checkHBEHelper(bt.root());
    }

    private static boolean checkHBEHelper(BTNode node) {

        return node == null || node.isLeaf() || (
                (Math.abs(getTreeHeight(node.leftChild()) - getTreeHeight(node.rightChild())) <= 1) &&
                 checkHBEHelper(node.leftChild()) &&
                 checkHBEHelper(node.rightChild())
        );
    }

    private static int getTreeHeight(BTNode node) {

        //Complexity T(n) = n

        if(node.isLeaf()) return 0;

        return 1 + Math.max(getTreeHeight(node.leftChild()), getTreeHeight(node.rightChild()));
    }

    public static void main(String[] args) {

        //tree from exercise 2a)
        BinTree avlSearchTree = createRealBinaryTreeFromString("5(4(2(),),7(6(),10(8(,),)))");
        BinTree avlWrongSearchTree = createRealBinaryTreeFromString("5(4(2(),),7(9(),10(8(,),)))");
        BinTree nonAVLTree = createRealBinaryTreeFromString("5(4(,),7(6(),10(8(,),)))");

        System.out.println("Is tree from 2a) a binary search tree: " + checkSearchTree(avlSearchTree));
        System.out.println("Is same tree with 9 instead of 6 a binary search tree: " + checkSearchTree(avlWrongSearchTree));

        System.out.println("Is tree from 2a) a tree with HBE: " + checkHBE(avlSearchTree));
        System.out.println("Is same tree with no 2 a tree with HBE: " + checkHBE(nonAVLTree));
    }
}
