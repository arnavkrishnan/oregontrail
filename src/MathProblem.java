public class MathProblem {
    private String problem;
    private double answer;

    public MathProblem(String problem, double answer) {
        this.problem = problem;
        this.answer = answer;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public double getAnswer() {
        return answer;
    }

    public void setAnswer(double answer) {
        this.answer = answer;
    }
}

