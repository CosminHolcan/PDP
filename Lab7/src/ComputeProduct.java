import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ComputeProduct {
    public static Polynomial RegularSequentialInRange(Polynomial first, Polynomial second, int startDegree, int finishDegree) {
        List<Integer> coefficients = new ArrayList<>();
        int productDegree = first.getDegree() + second.getDegree();
        for (int index = 0; index <= productDegree; index++) {
            coefficients.add(0);
        }

        for (int i = startDegree; i < finishDegree; i++) {
            for (int j = 0; j <= second.getDegree(); j++) {
                int newValue = coefficients.get(i + j) + first.getCoefficients().get(i) * second.getCoefficients().get(j);
                coefficients.set(i + j, newValue);
            }
        }
        return new Polynomial(coefficients);
    }

    public static Polynomial RegularSequential(Polynomial first, Polynomial second) {
        int resultDegree = first.getDegree() + second.getDegree();
        List<Integer> coefficients = new ArrayList<>();
        for (int index = 0; index <= resultDegree; index++) {
            coefficients.add(0);
        }
        for (int i = 0; i <= first.getDegree(); i++) {
            for (int j = 0; j <= second.getDegree(); j++) {
                int current_coefficient_degree = i + j;
                int value = coefficients.get(current_coefficient_degree) + first.getCoefficients().get(i) * second.getCoefficients().get(j);
                coefficients.set(current_coefficient_degree, value);
            }
        }

        return new Polynomial(coefficients);
    }

    public static Polynomial KaratsubaSequential(Polynomial first, Polynomial second) {
        if (first.getDegree() < 2 || second.getDegree() < 2)
            return RegularSequential(first, second);

        int length = Math.max(first.getDegree(), second.getDegree()) / 2;

        Polynomial firstLow = new Polynomial(first.getCoefficients().subList(0, length));
        Polynomial firstHigh = new Polynomial(first.getCoefficients().subList(length, first.getDegree() + 1));
        Polynomial secondLow = new Polynomial(second.getCoefficients().subList(0, length));
        Polynomial secondHigh = new Polynomial(second.getCoefficients().subList(length, second.getDegree() + 1));

        Polynomial multipliedHighParts = KaratsubaSequential(firstHigh, secondHigh);
        Polynomial multipliedLowParts = KaratsubaSequential(firstLow, secondLow);
        Polynomial multipliedCombined = KaratsubaSequential(Polynomial.addTwoPolynomials(firstLow, firstHigh),
                Polynomial.addTwoPolynomials(secondLow, secondHigh));

        Polynomial subtract = Polynomial.subtractTwoPolynomials(
                Polynomial.subtractTwoPolynomials(multipliedCombined, multipliedLowParts), multipliedHighParts);
        Polynomial extendedHighParts = Polynomial.extendDegree(multipliedHighParts, 2 * length);
        Polynomial extendedSubtract = Polynomial.extendDegree(subtract, length);

        return Polynomial.addTwoPolynomials(
                Polynomial.addTwoPolynomials(extendedHighParts, multipliedLowParts), extendedSubtract);
    }
}
