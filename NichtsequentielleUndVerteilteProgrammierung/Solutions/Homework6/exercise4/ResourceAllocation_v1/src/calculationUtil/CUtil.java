package calculationUtil;

public class CUtil {

	/**
	 * Adds each value in Array a to its corresponding value in Array b.
	 * The result is stored in a new Array.
	 * 
	 * @param a		Array a
	 * @param b		Array b
	 * @return		Result Array
	 */
	public static int[] add(int[] a, int[] b) {
		int length = Math.min(a.length, b.length);
		int[] res = new int[length];
		for (int i = 0; i < length; i++) {
			res[i] = a[i] + b[i];
		}
		return res;
	}

	/**
	 * Adds the content of an arbitrary number of Arrays.
	 * The result is stored in a new Array.
	 * 
	 * @param 		Input Arrays
	 * @return		Result Array
	 */
	public static int[] add(int[]... a) {
		int arrayCount = a.length;
		int length = Integer.MAX_VALUE;
		for (int i = 0; i < arrayCount; i++) {
			length = Math.min(a[i].length, length);
		}
		int[] res = new int[length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; i < a.length; j++) {
				res[i] += a[j][i];
			}
		}
		return res;
	}
	
	/**
	 * Subtracts the content of two arrays.
	 * The result is stored in a new Array.
	 * 
	 * @param a		Minuend
	 * @param b		Subtrahend
	 * @return		Difference
	 */
	public static int[] sub(int[] a, int[] b) {
		int length = Math.min(a.length, b.length);
		int[] res = new int[length];
		for (int i = 0; i < length; i++) {
			res[i] = a[i] - b[i];
		}
		return res;
	}

	/**
	 * Returns the lowest int-value in Array a.
	 * 
	 * @param a		Array a
	 * @return		Min value
	 */
	public static int min(int[] a) {
		int res = Integer.MAX_VALUE;
		for (int i = 0; i < a.length; i++) {
			res = Math.min(a[i], res);
		}
		return res;
	}
	
	/**
	 * Returns the highest int-value in Array a.
	 * 
	 * @param a		Array a
	 * @return		max value
	 */
	public static int max(int[] a) {
		int res = Integer.MIN_VALUE;
		for (int i = 0; i < a.length; i++) {
			res = Math.max(a[i], res);
		}
		return res;
	}
	
	/**
	 * Given two arrays this method will combine the arrays
	 * by taking the highest value for each position into the 
	 * result Array.
	 * 
	 * @param a		Array a
	 * @param b		Array b
	 * @return		New, combined Array
	 */
	public static int[] max(int[] a, int[] b) {
		int length = Math.min(a.length, b.length);
		int[] res = new int[length];
		for (int i = 0; i < a.length; i++) {
			res[i] = Math.max(a[i], b[i]);
		}
		return res;
	}
	
	/**
	 * Given two arrays this method will combine the arrays
	 * by taking the lowest value for each position into the 
	 * result Array.
	 * 
	 * @param a		Array a
	 * @param b		Array b
	 * @return		New, combined Array
	 */
	public static int[] min(int[] a, int[] b) {
		int length = Math.min(a.length, b.length);
		int[] res = new int[length];
		for (int i = 0; i < a.length; i++) {
			res[i] = Math.min(a[i], b[i]);
		}
		return res;
	}
	
	/**
	 * Checks if each value of Array a is greater equal than 
	 * its corresponding in array b.
	 * Assumption: length of Array a == length of Array b
	 * 
	 * @param a		Array a
	 * @param b		Array b
	 * @return		True, if "Array a >= Array b"
	 */
	public static boolean greaterEqual(int[] a, int[] b){
		int length = Math.min(a.length, b.length);
		boolean res = true;
		for (int i = 0; i < length; i++) {
			res &= a[i] >= b[i];
		}
		return res;
	}
	
	/**
	 * Checks if two arrays are equal.
	 * Assumption: length of Array a == length of Array b
	 * 
	 * @param a		Array a
	 * @param b		Array b
	 * @return		True, if Array a == Array b
	 */
	public static boolean equal(int[] a, int[] b){
		return greaterEqual(a,b) && greaterEqual(b,a);
	}
	
	/**
	 * Checks if the first 5 values of Array a are zero.
	 * 
	 * @param 		Array a
	 * @return		True, if the first 5 values equal zero. 
	 */
	public static boolean allZero(int[] a){
		return equal(new int[]{0,0,0,0,0},a);
	}

}
