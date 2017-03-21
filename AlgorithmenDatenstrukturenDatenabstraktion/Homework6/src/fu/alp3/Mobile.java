package fu.alp3;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Stack;

/**
 * Created by Boyan on 11/30/2016.
 */
public class Mobile extends BTNode{

    private static final int defaultBallRadius = 1;
    private static final int defaultBatonLength = 10;

    private float weight; //whole weight of all elements underneath
    private float length;
    private float radius;
    boolean ball;
    boolean baton;
    float leftLength;
    float rightLength;
    private float leftChildRotationalLength; //stores the radius of the circle drawn when each child rotates
    private float rightChildRotationalLength;

    public Mobile(float weight) {

        this.setElement(weight);
        this.setWeight(weight);
        this.ball = true;
        this.baton = false;

        this.setRadius(defaultBallRadius);
    }

    public Mobile(float weight, float radius) {

        this(weight);
        this.setRadius(radius);
    }

    public void setRadius(float newRadius) {

        if(newRadius >= 0) {

            this.radius = newRadius;
        }
        else {

            throw new IllegalArgumentException("Radius must be positive!");
        }
    }

    public float getRadius() {

        return this.radius;
    }


    public void setWeight(float newWeight) {

        if(newWeight >= 0) {

            this.weight = newWeight;
        }
        else {

            throw new IllegalArgumentException("Weights must be positive");
        }
    }

    public float getWeight() {

        return this.weight;
    }

    public void setLength(float newLength) {

        if(newLength <= 0) {

            throw new IllegalArgumentException("Length must be at least 0!");
        }

        this.length = newLength;
    }

    public float getLength() {

        return this.length;
    }

    public void addBalls(Mobile leftBall, Mobile rightBall) {

        this.ball = false;
        this.baton = true;
        this.length = defaultBatonLength;

        this.setLeft(leftBall);
        this.setRight(rightBall);
        leftBall.setParent(this);
        rightBall.setParent(this);
        this.setWeight(leftBall.getWeight() + rightBall.getWeight());

        this.leftChildRotationalLength = leftBall.radius;
        this.rightChildRotationalLength = rightBall.radius;

        this.balanceParents();
        this.resizeParentMobiles();
    }

    public void addBalls(float leftWeight, float rightWeight) {

        Mobile newLeft = new Mobile(leftWeight);
        Mobile newRight = new Mobile(rightWeight);

        this.addBalls(newLeft, newRight);
    }

    public void addBalls(float leftWeight, float leftRadius, float rightWeight, float rightRadius) {

        Mobile newLeft = new Mobile(leftWeight, leftRadius);
        Mobile newRight = new Mobile(rightWeight, rightRadius);

        this.addBalls(newLeft, newRight);
    }

    private void balanceParents() {

        Mobile currentNode = this;
        Mobile leftChild;
        Mobile rightChild;

        while(currentNode != null) {

            leftChild = (Mobile) currentNode.leftChild();
            rightChild = (Mobile) currentNode.rightChild();
            currentNode.setWeight(leftChild.getWeight() + rightChild.getWeight());

            currentNode.leftLength = (leftChild.getWeight() / (leftChild.getWeight() + rightChild.getWeight())) * currentNode.getLength();
            currentNode.rightLength = currentNode.getLength() - currentNode.leftLength;

            currentNode = (Mobile) currentNode.parent();
        }
    }

    private void resizeParentMobiles() {

        Mobile currentNode = this;
        Mobile leftChild;
        Mobile rightChild;
        float leftChildRotationalLength;
        float rightChildRotationalLength;

        float leftBallAtLongerSideRadius;
        float rightBallAtLongerSideRadius;

        float scalePercentage;

        while(currentNode != null) {

            leftChild = (Mobile) currentNode.leftChild();
            rightChild = (Mobile) currentNode.rightChild();

            //our plan works for resizing whole mobiles from the top, not just balls, just balls should be fine as they are
            if( leftChild.ball && rightChild.ball ) {

                currentNode = (Mobile) currentNode.parent();
                continue;
            }
            //find the rotational length first, which is the radius of the circle drawn by the mobile
            //it is calculated by multiplying the max(rotationalLengthOfChild+sideLength) for both children.

            //we need to fit the radius of the left and the right child so they don't collide when they spin
            //towards the same spot (get closest to each other)

            leftChildRotationalLength = leftChild.ball ? leftChild.getRadius() :
                    Math.max(leftChild.leftLength + leftChild.leftChildRotationalLength,
                            leftChild.rightLength + leftChild.rightChildRotationalLength);

            rightChildRotationalLength = rightChild.ball ? rightChild.getRadius() :
                    Math.max(rightChild.leftLength + rightChild.leftChildRotationalLength,
                            rightChild.rightLength + rightChild.rightChildRotationalLength);


            //Now the only thing left is to set the current nodes lengths just long enough
            //lets find the scale percentage first, then scale everything using it

            scalePercentage = (leftChildRotationalLength + rightChildRotationalLength) / currentNode.length;
            currentNode.setLength(currentNode.length * scalePercentage);
            currentNode.leftLength *= scalePercentage;
            currentNode.rightLength *= scalePercentage;

            currentNode.leftChildRotationalLength = leftChildRotationalLength;
            currentNode.rightChildRotationalLength = rightChildRotationalLength;

            currentNode = (Mobile) currentNode.parent();
        }
    }

    public void printResult () {

        //rep(m):=[m.weight] when the mobile is a single ball.
        //rep(m):=[(m.weight, m.length, m.leftLength, m.rightLength) rep(m1) rep(m2)]

        Mobile currentElement;
        Stack<Mobile> traverseStack = new Stack<>();
        traverseStack.push(this);

        System.out.print("[");

        while(!traverseStack.isEmpty()) {

            currentElement = traverseStack.pop();

            if(currentElement == null) continue;

            traverseStack.push((Mobile) currentElement.rightChild());
            traverseStack.push((Mobile) currentElement.leftChild());

            if(currentElement.ball) {

                System.out.print("[" + currentElement.getWeight() + "]");
            }
            else {

                System.out.print(
                        String.format("[%1$.2f, %2$.2f, %3$.2f, %4$.2f]", currentElement.getWeight(), currentElement.getLength(), currentElement.leftLength, currentElement.rightLength)
                );
            }
        }

        System.out.println("]");
    }

    public static void main(String[] args) {


        Mobile testOne = new Mobile(0);
        Mobile testTwo = new Mobile(0);

        //0 are set as weighs for better readability
        //when a leaf (ball) is turned into an internal node (baton) we set its own weight to 0 anyways

        testOne.addBalls(0,0);
        ((Mobile) testOne.leftChild()).addBalls(1,2);
        ((Mobile) testOne.rightChild()).addBalls(4,2);

        testTwo.addBalls(0,2);
        ((Mobile) testTwo.leftChild()).addBalls(0,3);
        ((Mobile) testTwo.leftChild().leftChild()).addBalls(1,2);

        testOne.printResult();
        testTwo.printResult();
    }
}
