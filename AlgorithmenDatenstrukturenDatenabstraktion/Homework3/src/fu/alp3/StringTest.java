package fu.alp3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class StringTest {

    public static int[] countLettersAndDigits(String input) {

        //Summary:  counts the amount of letters and digit in a given string
        //          and returns the result as an array [letters, digits]

        char[] inputCharArray = input.toCharArray();
        int letters = 0;
        int digits = 0;
        int[] result = new int[2];

        //go through all characters
        for(char currentChar : inputCharArray) {


            //if it's a letter, increment the count of letters
            if(Character.isLetter(currentChar)) {

                letters++;
            }//same more digits
            else if(Character.isDigit(currentChar)) {

                digits++;
            }
        }

        //save the result in an array (not ideal, but easy to implement)

        result[0] = letters;
        result[1] = digits;

        return result;
    }

    public static String getUserInput(String explanation, String inputPrefix) {

        // we could also use System.console().readLine(), but userd this to make debugging inside an IDE easier

        //Summary:  Print explanation to the user on the console. Then print the inputPrefix and read the the
        //          whole line a user types and return it

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        boolean stringParsed = false;

        System.out.println(explanation);

        while(!stringParsed) {

            try {

                System.out.print(inputPrefix);
                input = reader.readLine();
                stringParsed = true;
            }
            catch(IOException e) {

                System.out.println("Something went wrong during input. Please try again!");
            }
        }

        return input;
    }

    public static void printNumberOfLettersAndDigits(String inputString, int[] counter) {

        System.out.println("Der String \"" + inputString +"\" enthaelt " +
                counter[0] + " Buchstaben und " + counter[1] + " Ziffern.");
    }

    public static void main(String[] args) {

        boolean userExited = false;
        String userContinueInput;
        String userInput;
        int[] result;


        while(!userExited) {

            userInput = getUserInput("Enter a string to get the number of digits and number of letters in it.", "Input string: ");
            result = countLettersAndDigits(userInput);
            printNumberOfLettersAndDigits(userInput, result);

            userContinueInput = getUserInput("Would you like to continue? (y=continue, any other key = exit)", "Answer: ");
            userExited = !userContinueInput.toLowerCase().equals("y");
        }
    }
}
