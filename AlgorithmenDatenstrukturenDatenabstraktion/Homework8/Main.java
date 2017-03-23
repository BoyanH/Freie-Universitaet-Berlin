package fu.alp3;

import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
	// write your code here

        MWayNode testTree = new MWayNode(3, 8);

        MWayNode firstChild = new MWayNode(1,2);
        MWayNode secondChild = new MWayNode(5);
        MWayNode thirdChild = new MWayNode(12, 15, 19);

        firstChild.parent = testTree;
        secondChild.parent = testTree;
        thirdChild.parent = testTree;
        testTree.children.set(0, firstChild);
        testTree.children.set(1, secondChild);
        testTree.children.set(2, thirdChild);

        testTree.print();

        System.out.println("Is 2-4 Tree: " + testTree.check_2_4_Baum());

        //add a smaller key on the right
        testTree.children.set(2, new MWayNode(7)); //7 < 8
        System.out.println("Is 2-4 Tree: " + testTree.check_2_4_Baum());

        //fix previous error and add an overflowing node (works for underflows as well)
        testTree.children.set(2, new MWayNode(12, 15, 19));
        testTree.children.get(2).A[0] = 4;
        System.out.println("Is 2-4 Tree: " + testTree.check_2_4_Baum());

        //fix previous error and create a tree with leaves of unequal depth
        testTree.children.set(2, new MWayNode());
        System.out.println("Is 2-4 Tree: " + testTree.check_2_4_Baum());


        //fix tree again
        testTree.children.set(2, thirdChild);

        System.out.print("\n \nInitial tree: ");
        testTree.print();
        testTree.insertKey(6);
        System.out.print("After testTree.insertKey(6): ");
        testTree.print();


    }
}
