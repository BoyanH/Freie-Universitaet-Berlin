package fu.alp3;

public class ShortestPathAlgorithms {

    static final int default_algorithm_flag = 0; //execute Floyd-Warshall algorithm as is
    static final int max_vertex_algorithm_flag = 1; //execute Floyd-Warshall with weight of path = maximal vertex index
    static final int sum_of_vertexes_algorithm_flag = 2;

    static final Integer pos_infinity = null;

    /*
        ShortestPathAlgorithms Summary: Implementation of shortest path algorithms, where each Vertex is represented as an integer from
                0 to vertexCount(Graph) and the given Graph is represented as an adjacency matrix of edge weights
     */


    /*
        FloydWarshallShortestPaths
        Returns a matrix of objects with 2 properties for each entry (i, j) in the matrix:

            distance - the length of the shortest path between i and j
            prev - the previous vertex before j in the shortest path from i to j

     */
    public static ShortestPathEntry[][] floydWarshallShortestPaths(Integer[][] graph, int algorithmFlag) {

        ShortestPathEntry[][] shortestPathsMatrix = new ShortestPathEntry[graph.length][];

        //Initialization
        for (int row = 0; row < graph.length; row++) {

            shortestPathsMatrix[row] = new ShortestPathEntry[graph.length];
            for (int col = 0; col < graph[row].length; col++) {

                //null equivalent to +infinity in the algorithm
                Integer distance = pos_infinity;
                //equivalent of null as we defined vertexes as integers for simplicity
                Integer prev = null;

                if (row == col) {

                    distance = 0;
                    prev = null;
                } else if (graph[row][col] != null) {

                    if (algorithmFlag == default_algorithm_flag) {
                        distance = graph[row][col];
                    } else {
                        distance = 0;
                    }

                    prev = row;
                }  //else leave the initial values

                shortestPathsMatrix[row][col] = new ShortestPathEntry(distance, prev);
            }
        }

        for (int k = 0; k < graph.length; k++) {
            for (int i = 0; i < graph.length; i++) {
                for (int j = 0; j < graph.length; j++) {

                    /*
                        initialization to stop compiler warnings, there are only these 3 flags so one of the if
                        statements must be executed
                     */
                    boolean noBetterPathFound = true;
                    Integer newDistance = pos_infinity;
                    Integer newPrev;

                    if (algorithmFlag == default_algorithm_flag) {

                        Integer sumOfPathsThroughK = pos_infinity;

                        if (shortestPathsMatrix[i][k].getDistance() != pos_infinity &&
                                shortestPathsMatrix[k][j].getDistance() != pos_infinity) {
                            sumOfPathsThroughK = shortestPathsMatrix[i][k].getDistance() +
                                    shortestPathsMatrix[k][j].getDistance();
                        }

                        noBetterPathFound = sumOfPathsThroughK == pos_infinity ||
                                !(shortestPathsMatrix[i][j].getDistance() == pos_infinity ||
                                        shortestPathsMatrix[i][j].getDistance() > sumOfPathsThroughK);
                        newDistance = sumOfPathsThroughK;
                    } else if (algorithmFlag == sum_of_vertexes_algorithm_flag) {

                        Integer sumOfPathsThroughK = pos_infinity;

                        if (shortestPathsMatrix[i][k].getDistance() != pos_infinity &&
                                shortestPathsMatrix[k][j].getDistance() != pos_infinity) {
                            sumOfPathsThroughK = shortestPathsMatrix[i][k].getDistance() +
                                    shortestPathsMatrix[k][j].getDistance() + k;
                        }

                        noBetterPathFound = sumOfPathsThroughK == pos_infinity ||
                                !(shortestPathsMatrix[i][j].getDistance() == pos_infinity ||
                                        shortestPathsMatrix[i][j].getDistance() > sumOfPathsThroughK);
                        newDistance = sumOfPathsThroughK;
                    } else if (algorithmFlag == max_vertex_algorithm_flag) {

                        Integer maxOfPathsThroughK = pos_infinity;

                        if (shortestPathsMatrix[i][k].getDistance() != pos_infinity &&
                                shortestPathsMatrix[k][j].getDistance() != pos_infinity) {

                            maxOfPathsThroughK = Math.max(Math.max(
                                    shortestPathsMatrix[i][k].getDistance(),
                                    shortestPathsMatrix[k][j].getDistance()),
                                    k
                            );
                        }

                        noBetterPathFound = maxOfPathsThroughK == pos_infinity ||
                                !(shortestPathsMatrix[i][j].getDistance() == pos_infinity ||
                                        shortestPathsMatrix[i][j].getDistance() > maxOfPathsThroughK);
                        newDistance = maxOfPathsThroughK;
                    }

                    if(!noBetterPathFound) {
                        newPrev = shortestPathsMatrix[k][j].getPrev();
                        shortestPathsMatrix[i][j] = new ShortestPathEntry(newDistance, newPrev);
                    }

                }
            }
        }

        return shortestPathsMatrix;
    }

    public static ShortestPathEntry[][] floydWarshallShortestPaths(Integer[][] graph) {

        return floydWarshallShortestPaths(graph, default_algorithm_flag);
    }

    public static ShortestPathEntry[][] floydWarshallShortestPathsWithWeightEqualMaxVertexIndex(Integer[][] graph) {

        return floydWarshallShortestPaths(graph, max_vertex_algorithm_flag);
    }

    public static ShortestPathEntry[][] floydWarshallShortestPathsWithWeightEqualSumOfVertexIndexes(Integer[][] graph) {

        return floydWarshallShortestPaths(graph, sum_of_vertexes_algorithm_flag);
    }

    public static void printShortestPathsMatrix(ShortestPathEntry[][] matrix) {


        System.out.print("  ");
        for (int i = 0; i < matrix.length; i++) {

            System.out.format("|\t d(%d)/P(%d)", i, i);
        }
        System.out.print("|");

        for (int row = 0; row < matrix.length; row++) {

            System.out.print("\n" + row + " |\t ");
            for (int col = 0; col < matrix.length; col++) {

                System.out.format("%02d/%04d  |  ",
                        matrix[row][col].getDistance(), matrix[row][col].getPrev());
            }
        }

        System.out.println("\n");
    }
}
