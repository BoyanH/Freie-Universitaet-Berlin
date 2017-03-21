package fu.alp3;

class SkipListItem<K, E> {


    /*

        It can be a lowest level SkipListItem or a copy node of a skip list Item

     */

    SkipListItem<K, E> previous;
    SkipListItem<K, E> next;
    SkipListItem<K, E> upper;
    SkipListItem<K, E> lower;

    SkipListItem<K, E> referencedNode;

    K key;
    E element;

    public SkipListItem() {}

    SkipListItem(SkipListItem<K, E> refNode) {

        this.referencedNode = refNode.getItem();
    }

    SkipListItem(K k, E elem) {

        this(k, elem, null, null, null, null);
    }

    SkipListItem (K k, E elem, SkipListItem<K, E> prev, SkipListItem<K, E> nxt, SkipListItem<K, E> upr, SkipListItem<K, E> lwr) {

        this.element = elem;
        this.key = k;
        this.previous = prev;
        this.next = nxt;
        this.upper = upr;
        this.lower = lwr;
    }

    K getKey() {

        if(this.referencedNode != null) {

            return this.referencedNode.key;
        }

        return this.key;
    }

    SkipListItem<K, E> getItem() {

        if(this.referencedNode != null)
            return this.referencedNode;

        return this;
    }

    SkipListItem<K, E> getUpperPrevious() {

        SkipListItem<K, E> previous = this;

        while(previous.upper == null) {
            if (previous.previous == null) {
                return null;
            }

            previous = previous.previous;
        }

        return previous.upper;
    }
}
