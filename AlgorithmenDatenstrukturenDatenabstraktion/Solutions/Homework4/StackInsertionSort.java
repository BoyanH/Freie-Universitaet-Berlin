package fu.alp3;

import java.util.Scanner;
import java.util.Stack;

public class StackInsertionSort {

    public static <T extends Comparable<T>> T[] sort(T[] arr) {

        //Summary:      sorts an array using two stacks to implement an insertion sort algorithm

        //Explanation:  that is a more native, human related approach to insertion sort;
        //A good example would be stacking a pile of books. You place all of the smaller than the current book
        //ones aside so you can place the current book above the first in the pile that's bigger than it, then you
        //put the books back in one pile. Only difference is we are sorting in ascending order here and we
        //are not grabbing the whole pile, as we cannot see exactly how many items we need to move out of the way
        //to make place for the new item.

        Stack<T> s = new Stack<>();
        Stack<T> t = new Stack<>();


        for (T crntElem : arr) {

            while (!t.isEmpty() && t.peek().compareTo(crntElem) == -1) s.push(t.pop());
            t.push(crntElem);
            while (!s.isEmpty()) t.push(s.pop());
        }

        for (int i = 0; i < arr.length; i++) {

            arr[i] = t.pop();
        }

        return arr;
    }

    public static <T> String stringifyArray(T[] arr) {

        StringBuilder sb = new StringBuilder();

        sb.append("[");

        for (int i = 0; i < arr.length; i++) {

            sb.append(arr[i] + (i != arr.length - 1 ? ", " : "]"));
        }

        return sb.toString();
    }

    public static Integer[] readIntegerArrayFromConsole() {

        Scanner scanner = new Scanner(System.in);
        String userInput;
        String[] userInputArrayUnparsed;
        Integer[] userInputArray = new Integer[1];
        int userInputArrayLength = 0;
        boolean arrayParsed = false;

        while (!arrayParsed) {

            System.out.print("Enter an array to receive its sorted equivalent!" +
                    "Type integer values separated by commas/spaces. \n\n arr: ");

            //Read a new line, remove trailing spaces and split it to a string array, using regular expression

            //Regex explanation:    built using knowledge from ALP2, topic regular expressions

            //the idea:             we need to split the given string using any string that matches the regex as separator

            //regex meaning:        a string of one of the following as first character - ' ', ',', ';'
            //                      followed by a sequence of the three in any order, any length

            userInput = scanner.nextLine().trim();
            userInputArrayUnparsed = userInput.trim().split("(\\s|,|;)(\\s*,*;*)*");
            userInputArray = new Integer[userInputArrayUnparsed.length];

            for (int i = 0; i < userInputArray.length; i++) {

                try {
                    userInputArray[i] = Integer.parseInt(userInputArrayUnparsed[i]);
                    arrayParsed = true;
                } catch (NumberFormatException e) {

                    System.out.println(
                            String.format("Invalid input! Unexpected expression \"%1$s\"! " +
                                            "Please input only integers separated by commas, spaces or semicolons.",
                                    userInputArrayUnparsed[i])
                    );

                    arrayParsed = false;
                    break;
                }
            }

        }

        return userInputArray;
    }

    public static void main(String[] args) {

        Integer[] arr = readIntegerArrayFromConsole();

        System.out.println("Initial array: " + stringifyArray(arr));
        sort(arr);
        System.out.print("Result: " + stringifyArray(arr));
    }
}
