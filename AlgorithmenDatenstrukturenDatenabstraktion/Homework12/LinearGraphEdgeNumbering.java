package fu.alp3;

import sun.awt.image.ImageWatched;

import java.util.Comparator;
import java.util.LinkedList;

public class LinearGraphEdgeNumbering {

    private static int getNumberOfEdgesInG(Integer[][] adj) {

        /*
            Summary: count only the edges (u,v) where u > v, as in a non-directional graph
            the same edges are found twice in the adjacency list
         */

        int count = 0;

        for(int i = 0; i < adj.length; i++) {


            for(int j = 0; j < adj[i].length; j++) {

                //Count only edges {(u, v) | u <= v} as in an unidirectional graph, same edges are found twice in the
                //adjacency list
                if(adj[i][j] > i) {

                    count++;
                }
            }
        }

        return count;
    }

    public static LinkedList<LinkedList<Integer>> getLinearGraphEdgeNumbering(Integer[][] adj) {

        /*
            Summary: returns the adjacency list of the linear graph from the given one,
                    where all edges are already replaced with their numbers
         */

        //P.S.: Please don't say again that the comments are copied, I am adding them in the last minute ^.^

        //we could always add nested LinkedLists to linearEdgeNumbering when the index is not available,
        //this just speeds things up and better shows how everything works
        int numberOfEdges = LinearGraphEdgeNumbering.getNumberOfEdgesInG(adj);
        LinkedList<LinkedList<Integer>> linearEdgeNumbering = new LinkedList<>();

        for(int i = 0; i < numberOfEdges; i++) {

            linearEdgeNumbering.add(new LinkedList<>());
        }

        for(int i = 0; i < adj.length; i++) {


            for(int j = 0; j < adj[i].length; j++) {

                //find every adjacency pair (vertex -> adjacency list -> nth adjacent vertex)

                //get it's numbering for later on
                Integer currentLinearGraphVertexNumber = getEdgeNumber(adj, new Integer[]{i, adj[i][j]});

                int u = adj[i][j];

                //get all adjacent edges, or said otherwise - all edges that are between the current adjacent
                //vertex and it's adjacent vertexes
                for(int l = 0; l < adj[u].length; l++) {

                    int v = adj[u][l];

                    //get the numbering of the current "adjacent edge" (explained on line 67, 68)
                    Integer currentLinearGraphEdgeNumber = getEdgeNumber(adj, new Integer[]{u, v});
                    if(linearEdgeNumbering.get(currentLinearGraphVertexNumber) == null) {

                        //if this is the first adjacent edge we find, initialize the linked list
                        linearEdgeNumbering.set(currentLinearGraphVertexNumber, new LinkedList<>());
                    }

                    //if we didn't already find and add this edge to the adjacency list (happens as graph is unidirectional)
                    if(!linearEdgeNumbering.get(currentLinearGraphVertexNumber).contains(currentLinearGraphEdgeNumber)) {

                        //add the new edge's numbering and sort the list again to keep things clear
                        linearEdgeNumbering.get(currentLinearGraphVertexNumber).add(currentLinearGraphEdgeNumber);
                        linearEdgeNumbering.get(currentLinearGraphVertexNumber).sort(new Comparator<Integer>() {

                            @Override
                            public int compare(Integer a, Integer b) {

                                return a - b;
                            }
                        });
                    }
                }
            }
        }

        return linearEdgeNumbering;
    }

    public static Integer[] getNumberingOfNthEdgeInLinearGraph(Integer[][] adj, int k) {

        /*

           We developed an algorithm to get the whole linear graph of the given one as adjacency list
           and replace each edge with its numbering

           Therefore we only need to call this function and return the k-th edge's adjacency list

           P.S.: we are returning it as an Integer[], because...reasons :D (could remain a LinkedList as well)

         */

        return (Integer[]) getLinearGraphEdgeNumbering(adj).get(k).toArray();
    }

    public static int getEdgeNumber(Integer[][] adj, Integer[] e) {

        int counter = -1; //to start counting from 0 not 1

        for(int i = 0; i < adj.length; i++) {

            for(int j = 0; j < adj[i].length; j++) {

                if(adj[i][j] > i) {

                    counter++;
                }
                if(e[0] == i && e[1] == adj[i][j] ||
                        e[0] == adj[i][j] && e[1] == i)
                    return counter;
            }
        }

        return counter;
    }
}
