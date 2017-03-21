package fu.alp3;

import java.util.Random;

public class SkipListDictionary_bak<K extends Comparable<K>, E> {

    private SkipListItem<K, E> lastListStart;
    private final SkipListItem<K, E> negInfinity;
    private final SkipListItem<K, E> posInfinity;

    private final Random randomGenerator;

    @SuppressWarnings("unchecked")
    public SkipListDictionary_bak() {

        //TODO: find a better workaround
        this((K[]) new Object[0], (E[]) new Object[0]);
    }

    public SkipListDictionary_bak(K[] keys, E[] elements) {

        this.randomGenerator = new Random();
        this.negInfinity = new SkipListItem<>();
        this.posInfinity = new SkipListItem<>();

        this.constructSkipList(keys, elements);
    }

    private void constructSkipList(K[] keys, E[] elements) {

        SkipListItem<K, E> currentElement = null;

        assert(keys.length == elements.length);

        this.initializeLowestLevel(currentElement, keys, elements);
        this.createRandomizedSkipLists(currentElement);
    }

    private void initializeLowestLevel(SkipListItem<K, E> currentElement, K[] keys, E[] elements) {

        for(int i = 0; i < elements.length; i++) {

            //initialize the bottom level

            SkipListItem<K, E> currentItem = new SkipListItem<>(keys[i], elements[i]);

            if(i == 0) {

                this.lastListStart = negInfinity;
                negInfinity.next = currentItem;
                currentItem.previous = negInfinity;
            }
            else {

                currentItem.previous = currentElement;
                currentElement.next = currentItem;
            }

            currentElement = currentItem;
        }
        if(currentElement != null) {

            currentElement.next = posInfinity;
            posInfinity.previous = currentElement;
        }
        else {

            /*
                No keys are given, initialize with only one list [-inf, +inf]
             */

            this.lastListStart = negInfinity;
            negInfinity.next = posInfinity;
            posInfinity.previous = negInfinity;
        }
    }

    private void createRandomizedSkipLists(SkipListItem<K, E> currentElement) {

        while(this.lastListStart.next != posInfinity &&
                (this.lastListStart.next.referencedNode == null || this.lastListStart.next.referencedNode != posInfinity) ) {

            SkipListItem<K, E> previousNode = null;
            currentElement = this.lastListStart;

            while(currentElement != null) {

                boolean shouldIncludeNode = currentElement == posInfinity ||
                        currentElement.referencedNode == posInfinity ||
                        currentElement.referencedNode == negInfinity ||
                        currentElement == negInfinity ||
                        this.randomGenerator.nextBoolean();

                SkipListItem<K, E> refNode = currentElement.referencedNode != null ? currentElement.referencedNode : currentElement;

                if(shouldIncludeNode) {

                    currentElement.upper = new SkipListItem<>(refNode);
                    currentElement.upper.lower = currentElement;

                    if(previousNode != null) {

                        currentElement.upper.previous = previousNode;
                        previousNode.next = currentElement.upper;
                    }

                    previousNode = currentElement.upper;
                }


                currentElement = currentElement.next;
            }

            this.lastListStart = this.lastListStart.upper;
        }
    }

    private SkipListItem<K, E> searchKey(K key) {

        SkipListItem<K, E> currentElement = this.lastListStart;

        while(currentElement.lower != null) {

            currentElement = currentElement.lower;

            while(currentElement.next != null && currentElement.next.getItem() != posInfinity &&
                    currentElement.next.getKey().compareTo(key) < 1) {

                currentElement = currentElement.next;
            }
        }

        return  currentElement;
    }

    public E get(K key) {

        SkipListItem<K, E> itemAtKey = searchKey(key);

        if(itemAtKey.key.compareTo(key) == 0)
            return itemAtKey.element;

        return null;
    }

    public void set(K key, E element) {

        SkipListItem<K, E> elementAtKey = searchKey(key);
        SkipListItem<K, E> newItem = new SkipListItem<>(key, element);
        SkipListItem<K, E> currentPrevious;

        if(elementAtKey.key.compareTo(key) == 0) {

            elementAtKey.element = element;
            return;
        }

        //Add the new element at the lowest level

        elementAtKey.next.previous = newItem;
        newItem.previous = elementAtKey;
        newItem.next = elementAtKey.next;
        elementAtKey.next = newItem;

        currentPrevious = elementAtKey;

        //and push it up using probability
        while(randomGenerator.nextBoolean()) {

            currentPrevious = currentPrevious.getUpperPrevious();

            //if there is no upper level
            if(currentPrevious == null)
                break;

            newItem.upper = new SkipListItem<>(newItem);
            newItem.upper.previous = currentPrevious;
            newItem.upper.next = currentPrevious.next;
            currentPrevious.next.previous = newItem.upper;
            currentPrevious.next = newItem.upper;
            newItem.upper.lower = newItem;

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
