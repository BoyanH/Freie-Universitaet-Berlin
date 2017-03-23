package fu.alp3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class CourseStatistics {

    public static int getInputArrayLength() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int arrayLength = 0;
        boolean arrayLengthParsed = false;
        String currentInputLine;

        while(!arrayLengthParsed) {
            try {

                System.out.print("How many courses do you want to enter?: ");
                currentInputLine = reader.readLine();
                arrayLength = Integer.parseInt(currentInputLine);
                arrayLengthParsed = true;
            } catch (NumberFormatException e) {

                System.out.println("Unable to parse number of courses. Please enter an integer!");
            } catch (IOException e) {

                System.out.println("Something went wrong during input. Please try again!");
            }
        }

        return arrayLength;
    }

    public static float[] getInputArrayFromLength(int inputLength) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        float[] inputArray = new float[inputLength];
        String currentInputLine;
        boolean currentFloatParsed = false;

        //for all array indexes ask user for input
        for(int i = 0; i < inputLength; i++) {

            //until successful
            while(!currentFloatParsed) {

                //here there's more to go wrong, therefore also two catch blocks for the 2 most common
                //errors. Of course, if something goes wrong during input and this time if we are unable
                //to parse a float (e.g. if user enters "string" and no number)
                try {

                    System.out.print("Grade of course number " + (i+1) + ": ");
                    currentInputLine = reader.readLine();
                    inputArray[i] = Float.parseFloat(currentInputLine);
                    currentFloatParsed = true;
                } catch (NumberFormatException e) {

                    System.out.println("Unable to parse current course's grade. Please enter a real number!");
                } catch (IOException e) {

                    System.out.println("Something went wrong during input. Please try again!");
                }
            }

            currentFloatParsed = false;
        }

        return inputArray;
    }

    public static void greetUserAndExplain() {

        System.out.println("Enter all courses and their rating/grade to see for how long each course has been the best." +
                            "We assume all courses happen one after another, one course per day. \n \n");
    }

    public static void processCourse(RateEntry crntEntry, Stack<RateEntry> stack, int[] results) {

        //remove all courses from the stack, that have lower or equal grade to the currently processed course
        while(stack.peek().getGrade() <= crntEntry.getGrade()) stack.pop();

        //then save the best since of the current course
        results[crntEntry.getIndex()] = crntEntry.getIndex() - stack.peek().getIndex();
        stack.push(crntEntry); //and push it to the stack

        //NOTE: it's important to calculate the course's best since right after all <= courses has been popped
        //          and just before we pushed the current course, to make good use of stack.peek()
    }

    public static int[] getEachCoursesBestSince(float[] coursesGrades) {

        Stack<RateEntry> coursesStack = new Stack<>();
        int[] bestSinceResults = new int[coursesGrades.length];
        RateEntry initialEntry = new RateEntry(-1, Float.MAX_VALUE);
        RateEntry currentEntry;

        coursesStack.push(initialEntry);

        for(int i = 0; i < coursesGrades.length; i++) {

            currentEntry = new RateEntry(i, coursesGrades[i]);
            processCourse(currentEntry, coursesStack, bestSinceResults);

            //Here we make good use of non-primitive data types. As we know, they are passed to functions
            //by reference, so changes made in processCourse will be made in getEachCoursesBestSince
            //as they both keep the same reference to bestSinceResults and coursesStack
        }

        return bestSinceResults;
    }

    public static void printResults(int[] results) {

        System.out.println("\n\nResults: \n");

        for(int i = 0; i < results.length; i ++) {

            //Print each courses best since, add 1 to i to have more human-readable indexes
            //as not everyone starts counting from 0
            System.out.println((i+1) + ". course has been the best course for the last " + results[i] + " days");
        }
    }

    public static void main(String[] args) {

        greetUserAndExplain();

        int inputArrayLength = getInputArrayLength();
        float[] inputArray = getInputArrayFromLength(inputArrayLength);
        int[] coursesBestSinceResults = getEachCoursesBestSince(inputArray);

        printResults(coursesBestSinceResults);
    }
}
