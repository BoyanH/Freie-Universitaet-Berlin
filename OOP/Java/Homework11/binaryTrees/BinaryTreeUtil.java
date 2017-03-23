public class BinaryTreeUtil {

	//Angenommen der Wurzel und die Blätter sind Knoten, wie in der Mathematik
	public static int getVetexCount(BinaryTree bt) {

		//ein Null-Knoten ist kein Knoten
		if(bt == null) {
			return 0;
		}

		//sonst, zähle diesen Knoten UND diese im linken und rechten Teilbaum
		return 1 + getVetexCount(bt.links) + getVetexCount(bt.rechts);
	}


	//nimm das großere von 2 Variablen, trivial
	private static int getMax(int a, int b) {

		return a > b ? a : b;
	}

	//Je Kante du nach unten im Baum gehst, addiere 1. Wenn es zwei Kanten gibt (fast immer der Fall), nimm die Höhe der längere
	public static int getTreeHeight(BinaryTree bt) {

		if(bt == null) {

			return 0;
		}

		return 1 + getMax(getTreeHeight(bt.links), getTreeHeight(bt.rechts));
	}

	//von einer Liste nimm das erste Element für den Wurzel und dann füge Knoten ein
	public static <T> BinaryTree<T> createBalancedTree(Comparable<T>[] values) {

		int numberOfChildren = values.length;

		BinaryTree<T> newTree = new BinaryTree<T>(values[0]);

		for(int i = 1; i < numberOfChildren; i++) {

			addLeaf(newTree, values[i]);
		}

		return newTree;
	}

	//hier bleibt der Baum balanciert. Wir fügen da Knoten ein, wo es wenigere gibt (die höhe dieses Teilbaums ist kleiner)
	public static <T> void addLeaf(BinaryTree<T> bt, Comparable<T> value) {

		if(bt.links == null) {

			bt.links = new BinaryTree<T>(value);
		}
		else if(bt.rechts == null) {

			bt.rechts = new BinaryTree<T>(value);
		}
		else if(getTreeHeight(bt.links) < getTreeHeight(bt.rechts)) {

			addLeaf(bt.links, value);
		}
		else {

			addLeaf(bt.rechts, value);
		}
	}

	public static <T> boolean istSortiert(BinaryTree<T> bt) {

		if(bt == null) {

			return true;
		}

		return bt.wert >= bt.links.wert && bt.wert <= bt.rechts.wert && istSortiert(bt.links) && istSortiert(bt.rechts);
	}

	public static void main(String[] args) {

		Integer[] treeNodes = {1,2,3,4,5,6,7};
		BinaryTree<Integer> tree = createBalancedTree(treeNodes);


		System.out.println("Vertex count: " + getVetexCount(tree));
		System.out.println("Is binary tree:" + istSortiert(tree));

	}

}