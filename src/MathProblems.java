import java.util.ArrayList;
import java.util.Random;

public class MathProblems {

    private ArrayList<MathProblem> mathProblems;
    private Random random;

    public MathProblems() {
        mathProblems = new ArrayList<>();
        random = new Random();

        // these problems are AI generated, I hope that's okay - I didn't have much time to write all of them out.

        mathProblems.add(new MathProblem("2x + 3 = 11, find x", 4.0));
        mathProblems.add(new MathProblem("Solve for x: 3x - 4 = 8", 4.0));
        mathProblems.add(new MathProblem("x^2 - 5x + 6 = 0, find x", 2.0));
        mathProblems.add(new MathProblem("Find the perimeter of a rectangle with length 12 and width 5", 34.0));
        mathProblems.add(new MathProblem("Find the area of a circle with radius 7", 153.94));
        mathProblems.add(new MathProblem("Find the value of sin(30 degrees)", 0.5));
        mathProblems.add(new MathProblem("Find the value of cos(45 degrees)", 0.707));
        mathProblems.add(new MathProblem("Find the value of tan(60 degrees)", 1.72));
        mathProblems.add(new MathProblem("Convert 90 degrees to radians", 1.57));
        mathProblems.add(new MathProblem("Convert π/3 radians to degrees", 60.0));
        mathProblems.add(new MathProblem("Find the value of csc(30 degrees)", 2.0));
        mathProblems.add(new MathProblem("Find the inverse of sin(1/2)", 30.0));
    }

    public MathProblem getRandomMathProblem() {
        int index = random.nextInt(mathProblems.size());
        return mathProblems.get(index);
    }
}
