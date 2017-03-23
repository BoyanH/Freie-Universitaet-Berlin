package fu.alp3;

import java.util.Random;

public class SkipListDictionary<K extends Comparable<K>, E> {

    private SkipListItem<K, E> lastListStart;
    private final SkipListItem<K, E> negInfinity;
    private final SkipListItem<K, E> posInfinity;

    private final Random randomGenerator;

    public SkipListDictionary() {

        this.randomGenerator = new Random();
        this.negInfinity = new SkipListItem<>();
        this.posInfinity = new SkipListItem<>();

        this.lastListStart = this.negInfinity;
        this.negInfinity.next = this.posInfinity;
        this.posInfinity.previous = this.negInfinity;
    }

    public SkipListDictionary(K[] keys, E[] elements) {

        this();
        this.constructSkipLists(keys, elements);
    }

    private void constructSkipLists(K[] keys, E[] elements) {

        assert(keys.length == elements.length);

        for(int i = 0; i < keys.length; i++) {

            this.set(keys[i], elements[i]);
        }
    }

    private SkipListItem<K, E> searchKey(K key) {

        SkipListItem<K, E> currentElement = this.lastListStart;

        while(true) {

            while(currentElement.next != null && currentElement.next.getItem() != posInfinity &&
                    currentElement.next.getKey().compareTo(key) < 1) {

                currentElement = currentElement.next;
            }

            if(currentElement.lower == null) {

                return currentElement;
            }

            currentElement = currentElement.lower;
        }
    }

    private SkipListItem<K, E> getPosInfinityOfLevel(SkipListItem<K, E> level) {

        while(level.next != null) {

            level = level.next;
        }

        return level;
    }

    public E get(K key) {

        SkipListItem<K, E> itemAtKey = searchKey(key);

        if(itemAtKey.key != null && itemAtKey.key.compareTo(key) == 0)
            return itemAtKey.element;

        return null;
    }

    public void set(K key, E element) {

        SkipListItem<K, E> elementAtKey = searchKey(key);
        SkipListItem<K, E> newItem = new SkipListItem<>(key, element);
        SkipListItem<K, E> currentPrevious;

        SkipListItem<K, E> newNegInf;
        SkipListItem<K, E> newPosInf;

        if(elementAtKey.key != null && elementAtKey.key.compareTo(key) == 0) {

            elementAtKey.element = element;
            return;
        }

        //Add the new element at the lowest level

        elementAtKey.next.previous = newItem;
        newItem.previous = elementAtKey;
        newItem.next = elementAtKey.next;
        elementAtKey.next = newItem;

        currentPrevious = elementAtKey.getUpperPrevious();

        //and push it up using probability
        while(randomGenerator.nextBoolean()) {

            newItem.upper = new SkipListItem<>(newItem);
            newItem.upper.lower = newItem;

            //if there is no upper level, create one
            if(currentPrevious == null) {

                newNegInf = new SkipListItem<>(this.negInfinity);
                newPosInf = new SkipListItem<>(this.posInfinity);

                newItem.upper.previous = newNegInf;
                newItem.upper.next = newPosInf;
                newNegInf.next = newItem.upper;
                newPosInf.previous = newItem.upper;

                newNegInf.lower = this.lastListStart;
                this.lastListStart.upper = newNegInf;
                this.lastListStart = newNegInf;

                newPosInf.lower = this.getPosInfinityOfLevel(this.lastListStart);
                newPosInf.lower.upper = newPosInf;

            }
            else {

                newItem.upper.previous = currentPrevious;
                newItem.upper.next = currentPrevious.next;
                currentPrevious.next.previous = newItem.upper;
                currentPrevious.next = newItem.upper;

                currentPrevious = currentPrevious.getUpperPrevious();
            }

            newItem = newItem.upper;
        }
    }

    public void remove(K key) {

        SkipListItem<K, E> keyToDelete = this.searchKey(key);

        if(keyToDelete.key != key)
            throw new RuntimeException("No such key!");

        while(keyToDelete != null) {

            if(keyToDelete.previous == this.negInfinity && keyToDelete.next == this.posInfinity) {

                this.lastListStart = keyToDelete.previous;
                this.lastListStart.upper = null;
                this.lastListStart.next.upper = null;
            }

            keyToDelete.lower = null;
            keyToDelete.previous.next = keyToDelete.next;
            keyToDelete.next.previous = keyToDelete;
            keyToDelete = keyToDelete.upper;
        }
    }

    void print() {

        SkipListItem<K, E> currentLevel = this.lastListStart;
        SkipListItem<K, E> currentItem;

        while(currentLevel != null) {

            System.out.print("[");

            currentItem = currentLevel;
            while(currentItem != null) {

                System.out.print(currentItem.getItem().key + (currentItem.next != null ? ", " : ""));

                currentItem = currentItem.next;
            }

            System.out.println("]");

            currentLevel = currentLevel.lower;
        }

        System.out.println("");
    }

}
