package fu.alp3;

/** Implementation of a proper linked Binary Tree with labels of type E */

public class BinTree {
    /** reference to the root: */
    private BTNode<Integer> root;
    /** number of nodes: */
   private int size;
  
   /** default constructor: */
   public BinTree() {
      root = new BTNode(null,null,null,null);
      size = 1;
   }
      
    /** accessor methods: */
  
   public int size() { return size; }

   public boolean isTrivial() { return (size==1); }
       /** a trivial Tree consists of the root node only
	* empty trees are excluded   */ 

   public boolean isInternal(BTNode<Integer> v) {return v.isInternal(); }

   public boolean isLeaf(BTNode<Integer> v) {return  v.isLeaf(); }

   public boolean isRoot(BTNode<Integer> v) { return (v==root()); }

   public BTNode<Integer> root() { return root; }

   public BTNode<Integer> leftChild(BTNode<Integer> v) { return v.leftChild(); }

   public BTNode<Integer> rightChild(BTNode<Integer> v) { return v.rightChild(); }

   public BTNode<Integer> sibling(BTNode<Integer> v) {
      BTNode<Integer> p  = parent(v);
      BTNode<Integer> lc = leftChild(p);
      if (v == lc)
         return rightChild(p);
      else
         return lc;
   }

   public BTNode parent(BTNode<Integer> v) { return v.parent(); }
   
    /** update methods: */
 
    /** turn a leaf v to an inner node by appending two new leaves 
     *  if v is not a leaf, the method won't do anything
     *  alternatively one could throw an exception */
   public void expandExternalNode(BTNode<Integer> v){
      if( isLeaf(v)){
         v.setLeft(new BTNode<Integer>(null,v,null,null));
         v.setRight(new BTNode<Integer>(null,v,null,null));
         size += 2;
      }
   }
}


