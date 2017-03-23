public class BinaryTree <T> {
	
	public Comparable<T> wert;
	public BinaryTree<T> links, rechts;

	public BinaryTree(Comparable<T> val) {

		this(val, null, null);
	}

	public BinaryTree(Comparable<T> val, BinaryTree<T> lt, BinaryTree<T> rt) {

		this.wert = val;
		this.links = lt;
		this.rechts = rt;
	}
}