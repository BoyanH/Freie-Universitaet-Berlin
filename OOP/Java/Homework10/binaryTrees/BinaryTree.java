public class BinaryTree <T> {
	
	public T value;
	public BinaryTree<T> leftTree, rightTree;

	public BinaryTree(T val) {

		this(val, null, null);
	}

	public BinaryTree(T val, BinaryTree<T> lt, BinaryTree<T> rt) {

		this.value = val;
		this.leftTree = lt;
		this.rightTree = rt;
	}
}