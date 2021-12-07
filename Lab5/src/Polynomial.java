import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public final class Polynomial {
    private List<Integer> coefficients;
    private static int MAXIMUM_RANDOM_COEFFICIENT_VALUE = 10;

    public Polynomial(List<Integer> coefficients) {
        this.coefficients = coefficients;
    }

    public List<Integer> getCoefficients() {
        return coefficients;
    }

    public int getDegree() {
        return this.coefficients.size() - 1;
    }

    public static Polynomial getRandomPolynomial(int degree) {
        Random random = new Random();
        List<Integer> coefficients = new ArrayList<>();
        for (int index = 0; index < degree; index++) {
            coefficients.add(random.nextInt(MAXIMUM_RANDOM_COEFFICIENT_VALUE));
        }
        coefficients.add(random.nextInt(MAXIMUM_RANDOM_COEFFICIENT_VALUE - 1) + 1);

        return new Polynomial(coefficients);
    }

    public static Polynomial extendDegree(Polynomial polynomial, int differenceDegree) {
        List<Integer> coefficients = new ArrayList<>();
        for (int index = 0; index < differenceDegree; index++) {
            coefficients.add(0);
        }
        coefficients.addAll(polynomial.getCoefficients());

        return new Polynomial(coefficients);
    }

    public static Polynomial getMinusPolynomial(Polynomial polynomial) {
        List<Integer> coefficients = new ArrayList<>();

        for (int index = 0; index <= polynomial.getDegree(); index++) {
            coefficients.add(-polynomial.getCoefficients().get(index));
        }

        return new Polynomial(coefficients);
    }

    public static Polynomial addTwoPolynomials(Polynomial first, Polynomial second) {
        List<Integer> coefficients = new ArrayList<>();

        int minDegree = Math.min(first.getDegree(), second.getDegree());
        int maxDegree = Math.max(first.getDegree(), second.getDegree());

        for (int index = 0; index <= minDegree; index++) {
            coefficients.add(first.getCoefficients().get(index) + second.getCoefficients().get(index));
        }

        if (minDegree != maxDegree) {
            if (maxDegree == first.getDegree()) {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(first.getCoefficients().get(i));
                }
            } else {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(second.getCoefficients().get(i));
                }
            }
        }

        int index = coefficients.size() - 1;
        while (coefficients.get(index) == 0 && index > 0) {
            coefficients.remove(index);
            index--;
        }

        return new Polynomial(coefficients);
    }

    public static Polynomial subtractTwoPolynomials(Polynomial first, Polynomial second) {
        Polynomial minusSecond = Polynomial.getMinusPolynomial(second);

        return Polynomial.addTwoPolynomials(first, minusSecond);
    }
    
    @Override
    public String toString() {
        String toReturn = "";
        for (int index = coefficients.size() - 1; index > 0; index--) {
            toReturn += coefficients.get(index) + " X ^ " + index + " + ";
        }
        toReturn += coefficients.get(0);
        return toReturn;
    }
}
