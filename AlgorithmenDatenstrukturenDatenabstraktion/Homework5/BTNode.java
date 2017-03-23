package fu.alp3;

/**
 * Node of a binary tree with labels of type E
 */

public class BTNode<E> {
    private E element;
    private BTNode<E> left, right, parent;

    /**
     * default constructor
     */
    public BTNode() {
    }

    /**
     * constructor with parameters
     */
    public BTNode(E e, BTNode u, BTNode v, BTNode w) {
        setElement(e);
        setParent(u);
        setLeft(v);
        setRight(w);
    }

    /**
     * accessor methods:
     */

    public E element() {
        return element;
    }

    public void setElement(E e) {
        element = e;
    }

    public BTNode<E> leftChild() {
        return left;
    }

    public void setLeft(BTNode<E> v) {
        left = v;
    }

    public BTNode<E> rightChild() {
        return right;
    }

    public void setRight(BTNode<E> v) {
        right = v;
    }

    public BTNode<E> parent() {
        return parent;
    }

    public void setParent(BTNode<E> v) {
        parent = v;
    }

    /**
     * methods for  checking basic properties:
     */

    public boolean isLeaf() {
        return (left == null && right == null);
    }

    public boolean isInternal() {
        return (left != null || right != null);
    }

    /**
     * note that in proper binary trees one could use && instead of ||
     */

    public boolean isRoot() {
        return (parent == null);
    }

}

