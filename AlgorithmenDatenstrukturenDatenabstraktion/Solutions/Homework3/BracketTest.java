package fu.alp3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class BracketTest {

    public static String allBrackets = "[()]";

    public static String getUserInput(String explanation, String inputPrefix) {

        // we could also use System.console().readLine(), but used this to make debugging inside an IDE easier

        //Summary:  Print explanation to the user on the console. Then print the inputPrefix and read the the
        //          whole line a user types and return it

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        boolean stringParsed = false;

        System.out.println(explanation);

        //keep asking user for input until successful
        while(!stringParsed) {

            //as with all input, we need to wrap it in a try/catch block
            try {

                System.out.print(inputPrefix);
                input = reader.readLine();
                stringParsed = true;
            }
            catch(IOException e) {

                System.out.println("Something went wrong during input. Please try again!");
            }
            //here we would normally close the input stream, but System.in should never be closed
        }

        return input;
    }

    public static boolean isAnOpeningBracket(char c) {

        return c == '(' || c == '[';
    }

    public static char getCorrespondingClosingBracket(char c) {

        switch(c) {

            case '[': return ']';
            case '(':
            default : return ')';
        }
    }

    public static boolean isABracket(char c) {

        //check if the given symbol is existent in the array of brackets we accept for this program
        return allBrackets.indexOf(c) != -1;
    }

    public static boolean processChar( char symbol, Stack<Character> charStack) {

        Character wrappedSymbol = new Character(symbol); //no longer necessary for java 5 and newer

        //ignore all non-bracket symbols, they don't have effect on the bracket-expression
        if(!isABracket(symbol)) {

            return true;
        }

        //opening brackets we push to the stack
        if(isAnOpeningBracket(symbol)) {

            charStack.push(wrappedSymbol);
        }
        else if(!charStack.isEmpty() && getCorrespondingClosingBracket(charStack.peek().charValue()) == symbol) {

            //if it's a closing bracket, check if it's corresponding one is on top of the stack
            //if so, remove it from the stack
            charStack.pop();
        }
        else {

            //otherwise we have something similar to [), which is not a valid expression, return false
            //for bracket not processed successfully
            return false;
        }

        //if we didn't explicitly return false in the else block, then everything worked out fine,
        //symbol processed successfully
        return true;
    }

    public static boolean isAValidBracketExpresion(String expression) {

        Stack<Character> charStack = new Stack<>();
        char[] exprCharArray = expression.toCharArray();

        //go through all characters and try to process them
        for(char currentChar : exprCharArray) {

            //if symbol was not processed correctly, break the loop and return false immediately,
            //no need to continue checking
            if(!processChar(currentChar, charStack)) return false;
        }

        //if we successfully processed each character, check if the bracket-stack is empty, if so,
        //every bracket has its corresponding closing bracket in the correct place, expression is valid
        return charStack.isEmpty();
    }

    public static void printResult(boolean isValid) {

        System.out.println("This is " +
                            (isValid ? "" : "NOT") +
                            " a valid bracket expression!");
    }

    public static void main(String[] args) {

        String bracketExpression = getUserInput("Enter a bracket expression to see if it's valid.",
                "Bracket expression: ");
        boolean isValid = isAValidBracketExpresion(bracketExpression);

        printResult(isValid);
    }
}
