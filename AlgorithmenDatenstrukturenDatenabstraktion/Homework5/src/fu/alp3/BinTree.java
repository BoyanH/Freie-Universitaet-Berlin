package fu.alp3;

/**
 * Implementation of a proper linked Binary Tree with labels of type E
 */

public class BinTree<E> {
    /**
     * reference to the root:
     */
    private BTNode<E> root;
    /**
     * number of nodes:
     */
    private int size;

    /**
     * default constructor:
     */
    public BinTree() {
        root = new BTNode(null, null, null, null);
        size = 1;
    }

    /**
     * accessor methods:
     */

    public int size() {
        return size;
    }

    public boolean isTrivial() {
        return (size == 1);
    }

    /**
     * a trivial Tree consists of the root node only
     * empty trees are excluded
     */

    public boolean isInternal(BTNode<E> v) {
        return v.isInternal();
    }

    public boolean isLeaf(BTNode<E> v) {
        return v.isLeaf();
    }

    public boolean isRoot(BTNode<E> v) {
        return (v == root());
    }

    public BTNode<E> root() {
        return root;
    }

    public BTNode<E> leftChild(BTNode<E> v) {
        return v.leftChild();
    }

    public BTNode<E> rightChild(BTNode<E> v) {
        return v.rightChild();
    }

    public BTNode<E> sibling(BTNode<E> v) {
        BTNode<E> p = parent(v);
        BTNode<E> lc = leftChild(p);
        if (v == lc)
            return rightChild(p);
        else
            return lc;
    }

    public BTNode<E> parent(BTNode<E> v) {
        return v.parent();
    }

    /** update methods: */

    /**
     * turn a leaf v to an inner node by appending two new leaves
     * if v is not a leaf, the method won't do anything
     * alternatively one could throw an exception
     */
    public void expandExternalNode(BTNode<E> v) {
        if (isLeaf(v)) {
            v.setLeft(new BTNode<E>(null, v, null, null));
            v.setRight(new BTNode<E>(null, v, null, null));
            size += 2;
        }
    }

    /**
     * delete a leaf v and replace v's parent node by v's sibling
     * if v is the root or if v is not a leaf, the method won't do anything
     * alternatively one could throw an exception
     */
    public void removeAboveExternalNode(BTNode<E> v) {
        if (isLeaf(v) && !isRoot(v)) {
            BTNode<E> p = parent(v);
            BTNode<E> s = sibling(v);
            if (isRoot(p)) {
                s.setParent(null);
                root = s;
            } else {
                BTNode<E> g = parent(p);
                if (p == leftChild(g))
                    g.setLeft(s);
                else
                    g.setRight(s);
                s.setParent(g);
            }
            size -= 2;
        }
    }

    /**
     * Just a short test:
     */
    public static void main(String[] args) {
        BinTree<Integer> bt = new BinTree();
        BTNode<Integer> v = new BTNode();
        (bt.root()).setLeft(v);
        bt.expandExternalNode(v);
        bt.expandExternalNode(v.leftChild());
        System.out.println(" Size = " + bt.size());
    }
}


