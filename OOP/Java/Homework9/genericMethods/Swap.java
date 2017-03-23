class Swap {

	public static <T> void printArray(T[] arr) {

		System.out.print("[");

		for(int i = 0; i < arr.length; i++) {

			System.out.print(arr[i]);
		}

		System.out.println("]");
	}

	public static <T> void swap(T[] arr, int indexA, int indexB) {

		T temp = arr[indexA];
		arr[indexA] = arr[indexB];
		arr[indexB] = temp;
	}
	
	public static void main(String[] args) {

		Integer [] a = {1,2,0,2,3};

		printArray(a);

		System.out.println("Vertausche index 2 mit index 3");
		Swap.<Integer>swap(a, 2, 3); // vertauscht a[2] mit a[3]

		printArray(a);

		System.out.println("Vertausche index 0 mit index 4");
		swap(a, 0, 4); // auch ohne explizite Angabe des Datentyps

		printArray(a);
		// Der Typ wird aus dem Argument a erschlossen. (Typinferenz)
	}
}
