package fu.alp3;

public class Main {

    public static void main(String[] args) {

        /*

            The graph given as example (Note my super duper mega drawing skills)


                  (0)
             2   /\  \   5
               /   1   \/
             (1)   -> (2)
              /\       /
             2  \    \/  -1
                  (3)

         */

        Integer[][] exampleGraph = new Integer[][] {

                {null, null, 5, null},
                {2, null, 1, null},
                {null, null, null, -1},
                {null, 2, null, null}
        };

        ShortestPathEntry[][] floydWarshallShortestPaths =
                ShortestPathAlgorithms.floydWarshallShortestPaths(exampleGraph);
        ShortestPathEntry[][] floydWarshallShortestPathsWeightIsMax =
                ShortestPathAlgorithms.floydWarshallShortestPathsWithWeightEqualMaxVertexIndex(exampleGraph);
        ShortestPathEntry[][] floydWarshallShortestPathsWeightIsSum =
                ShortestPathAlgorithms.floydWarshallShortestPathsWithWeightEqualSumOfVertexIndexes(exampleGraph);

        System.out.println("NOTE: null for distance d = +Infinity!\n");

        System.out.println("Floyd-Warshall result: ");
        ShortestPathAlgorithms.printShortestPathsMatrix(floydWarshallShortestPaths);

        System.out.println("Floyd-Warhsall with weight of path = max of it's vertexes' indexes: ");
        ShortestPathAlgorithms.printShortestPathsMatrix(floydWarshallShortestPathsWeightIsMax);

        System.out.println("Floyd-Warhsall with weight of path = sum of it's vertexes' indexes: ");
        ShortestPathAlgorithms.printShortestPathsMatrix(floydWarshallShortestPathsWeightIsSum);

    }
}
