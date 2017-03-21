package fu.alp3;

class Main {

    public static void main(String[] args) {

        Integer[] keys = new Integer[] {1, 2, 3, 5};
        Integer[] elements = new Integer[] {1, 2, 3, 5};
        SkipListDictionary<Integer, Integer> dictionary = new SkipListDictionary<>(keys, elements);

//        dictionary.print();

        System.out.println("dictionary.get(4): " + dictionary.get(4));

        dictionary.set(4, 4);
        System.out.println("dictionary.set(4, 4); dictionary.get(4): " + dictionary.get(4));

        dictionary.print();

        dictionary.remove(4);
        System.out.println("dictionary.remove(4); dictionary.get(4): " + dictionary.get(4));
    }
}
