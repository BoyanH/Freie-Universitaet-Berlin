package fu.alp3;

import java.util.List;
import java.util.Objects;

/**
 * Created by Boyan on 11/24/2016.
 */
public class BinTreeTest {

    private static <E> void printList(List<E> list) {

        System.out.print("[");
        for(int i = 0; i < list.size(); i++) {

            System.out.print(list.get(i) + (i == list.size() - 1 ? "" : ", "));
        }
        System.out.println("]");
    }

    public static void main (String[] args) {

        BinTree<Integer> testBT = BinTreeUtil.createRealBinaryTreeFromString("8(5(9,3),3(6,2(5,1)))");
        List<Integer> preOrderList;
        List<Integer> postOrderList;

        /* Who makes such an impractical BinTree class...
        *  The idea of the exercise could possibly be to be able to understand code, therefore we won't change
        *       the given classes
        * */

        BinTreeUtil.outputBT(testBT);

        BinTreeUtil.depth(testBT);
        System.out.print("Depth: ");
        BinTreeUtil.outputBT(testBT);

        BinTreeUtil.height(testBT);
        System.out.print("Height: ");
        BinTreeUtil.outputBT(testBT);

        //We would otherwise make a copy of the tree anytime we want to change it, but be able to recover it
        //though we didn't find an easy way to copy objects in java without further implementation

        testBT = BinTreeUtil.createRealBinaryTreeFromString("8(5(9,3),3(6,2(5,1)))");
        preOrderList = BinTreeUtil.preOrder(testBT);
        System.out.print("PreOrder: ");
        printList(preOrderList);

        postOrderList = BinTreeUtil.postOrder(testBT);
        System.out.print("PostOrder: ");
        printList(postOrderList);

    }
}
