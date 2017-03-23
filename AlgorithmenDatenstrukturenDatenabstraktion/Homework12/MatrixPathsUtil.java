package fu.alp3;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class MatrixPathsUtil {

    private static int getMx(int n, Method getKeyFunction) {

        int amount = 0;
        double pathToEndVertex = Double.MIN_VALUE;

        try {

            pathToEndVertex = (double) getKeyFunction.invoke(Class.forName("fu.alp3.MatrixPathsUtil"), new Integer[] {n, n, n, n});
        } catch (Exception e) {

            System.out.println("Wrong getKey() method parsed as argument to getMx()!");
        }

        for (int i = -2 * n; i <= 2 * n; i++) {

            for (int j = -2 * n; j <= 2 * n; j++) {

                try {

                    if (((double) getKeyFunction.invoke(Class.forName("fu.alp3.MatrixPathsUtil"), new Integer[] {i, j, n, n})) < pathToEndVertex) {

                        amount++;
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    System.out.println("Wrong getKey() method parsed as argument to getMx()!");
                }
            }
        }

        return amount;
    }

    private static double getKeyDijkstra(Integer a, Integer b, Integer endA, Integer endB) {

        return Math.abs(a) + Math.abs(b);
    }

    private static double getDistanceFromEnd(int a, int b, int endA, int endB) {

        return Math.sqrt(Math.pow(a - endA, 2) + Math.pow(b - endB, 2));
    }

    private static double getKeyAStar(Integer a, Integer b, Integer endA, Integer endB) {

        return MatrixPathsUtil.getKeyDijkstra(a, b, endA, endB) + MatrixPathsUtil.getDistanceFromEnd(a, b, endA, endB);
    }

    public static int getMd(int n) {

        /*
            Summary: returns the amount of vertexes with shorter path than the path to target vertex

            in a [-2n, 2n] x [-2n, 2n] matrix

            Start vertex: (0,0)
         */

        try {
            Class[] argTypes = new Class[]{Integer.class, Integer.class, Integer.class, Integer.class};
            Method getKeyDijkstra = Class.forName("fu.alp3.MatrixPathsUtil").getDeclaredMethod("getKeyDijkstra", argTypes);

            return MatrixPathsUtil.getMx(n, getKeyDijkstra);
        } catch (Exception e) {

            System.out.println("fu.alp3.MatrixPathsUtil::getKeyDijkstra() not found!");
        }

        return -1;
    }

    public static int getMa(int n) {

        /*
            Summary: returns the amount of vertexes with smaller A* key that the one of the target vertex

            in a [-2n, 2n] x [-2n, 2n] matrix
         */


        try {
            Class[] argTypes = new Class[]{Integer.class, Integer.class, Integer.class, Integer.class};
            Method getKeyAStar = Class.forName("fu.alp3.MatrixPathsUtil").getDeclaredMethod("getKeyAStar", argTypes);

            return MatrixPathsUtil.getMx(n, getKeyAStar);
        } catch (Exception e) {

            System.out.println("fu.alp3.MatrixPathsUtil::getKeyAStar() not found!");
        }

        return -1;

    }
}
