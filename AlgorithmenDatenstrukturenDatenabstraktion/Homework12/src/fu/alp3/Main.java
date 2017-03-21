package fu.alp3;

import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        System.out.println("Ma for n=1: " + MatrixPathsUtil.getMa(1));
        System.out.println("Md for n=1: " + MatrixPathsUtil.getMd(1) + "\n\n");

        System.out.println("Ma for n=2: " + MatrixPathsUtil.getMa(2));
        System.out.println("Md for n=2: " + MatrixPathsUtil.getMd(2) + "\n\n");

        System.out.println("Ma for n=5: " + MatrixPathsUtil.getMa(5));
        System.out.println("Md for n=5: " + MatrixPathsUtil.getMd(5) + "\n\n");

        System.out.println("Ma for n=10: " + MatrixPathsUtil.getMa(10));
        System.out.println("Md for n=10: " + MatrixPathsUtil.getMd(10) + "\n\n");

        System.out.println("Ma for n=20: " + MatrixPathsUtil.getMa(20));
        System.out.println("Md for n=20: " + MatrixPathsUtil.getMd(20) + "\n\n");

        System.out.println("Ma for n=50: " + MatrixPathsUtil.getMa(50));
        System.out.println("Md for n=50: " + MatrixPathsUtil.getMd(50) + "\n\n");

        System.out.println("Ma for n=100: " + MatrixPathsUtil.getMa(100));
        System.out.println("Md for n=100: " + MatrixPathsUtil.getMd(100) + "\n\n");

        System.out.println("Ma for n=200: " + MatrixPathsUtil.getMa(200));
        System.out.println("Md for n=200: " + MatrixPathsUtil.getMd(200) + "\n\n");

        // for(int i = 40; i < 10000; i++) {

            // System.out.println((double) MatrixPathsUtil.getMd(i) / MatrixPathsUtil.getMa(i));
        // }

        /*

            Md(n) / Ma(n) = 5.58 (approximately) for big n's

         */

        Integer[][] adj = new Integer[][]{
                {1,2,3,4,5},
                {0,2,4},
                {0,1,3,4},
                {0,2,4},
                {0,1,2,3,5},
                {0,4}
        };
        LinkedList<LinkedList<Integer>> edgeNumbering =  LinearGraphEdgeNumbering.getLinearGraphEdgeNumbering(adj);

        //must be 1 from graph in 3 a), which is 0 here (we start counting from 0 so first vertex is 0, first edge is 0)
//        System.out.println(LinearGraphEdgeNumbering.getEdgeNumber(adj, new Integer[]{0,1}));

        System.out.print("Numbering of " + LinearGraphEdgeNumbering.getEdgeNumber(adj, new Integer[]{0,1}) + "th edge: ");
        for(int i = 0; i < edgeNumbering.get(0).size(); i++) {

            System.out.print(edgeNumbering.get(0).get(i) + ", ");
        }

        System.out.print("\nAll numberings which should be already printed as it can be seen from exercise 3 a): ");
        System.out.print(LinearGraphEdgeNumbering.getEdgeNumber(adj, new Integer[]{0,1}) + ", ");
        System.out.print(LinearGraphEdgeNumbering.getEdgeNumber(adj, new Integer[]{0,2}) + ", ");
        System.out.print(LinearGraphEdgeNumbering.getEdgeNumber(adj, new Integer[]{0,3}) + ", ");
        System.out.print(LinearGraphEdgeNumbering.getEdgeNumber(adj, new Integer[]{0,4}) + ", ");
        System.out.print(LinearGraphEdgeNumbering.getEdgeNumber(adj, new Integer[]{0,5}) + ", ");
        System.out.print(LinearGraphEdgeNumbering.getEdgeNumber(adj, new Integer[]{0,3}) + ", ");
        System.out.print(LinearGraphEdgeNumbering.getEdgeNumber(adj, new Integer[]{1,2}) + ", ");
        System.out.print(LinearGraphEdgeNumbering.getEdgeNumber(adj, new Integer[]{1,4}));

        //function as wished in exercise
        //LinearGraphEdgeNumbering.getNumberingOfNthEdgeInLinearGraph(adj, 0);
        //return the same as LinearGraphEdgeNumbering.getLinearGraphEdgeNumbering(adj).get(0)
    }
}
