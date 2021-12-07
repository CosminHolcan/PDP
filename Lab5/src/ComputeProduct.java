import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ComputeProduct {
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

    private static void computeCoefficientsInRange(Polynomial first, Polynomial second, int startDegree, int lastDegree, List<Integer> coefficients){
        for (int degree = startDegree; degree <= lastDegree; degree++) {
            int maxPossibleDegree = Math.min(degree, first.getDegree());
            int minPossibleDegree = Math.max(0, degree - second.getDegree());
            int value = 0;
            for (int i = minPossibleDegree; i <= maxPossibleDegree; i++) {
                value += first.getCoefficients().get(i) * second.getCoefficients().get(degree - i);
            }
            coefficients.set(degree, value);
        }
    }

    public static Polynomial RegularParallelized(Polynomial first, Polynomial second, int threadsNumber) {
        int resultDegree = first.getDegree() + second.getDegree();
        List<Integer> coefficients = new ArrayList<>();
        for (int index = 0; index <= resultDegree; index++) {
            coefficients.add(0);
        }

        List<Callable<Object>> tasks = new ArrayList<>();
        int taskSize =  resultDegree / threadsNumber;

        int end;
        for (int index = 0; index <= resultDegree; index += taskSize) {
            end = index + taskSize - 1;
            end = Math.min(end, resultDegree);
            int startDegree = index;
            int endDegree = end;
            Callable<Object> task = Executors.callable(() -> { computeCoefficientsInRange(first, second, startDegree, endDegree, coefficients);});
            tasks.add(task);
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);
        tasks.forEach(executor::submit);
        executor.shutdown();

        try {
            executor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public static Polynomial KaratsubaParallelized(Polynomial first, Polynomial second, int threadsNumber, int depth) throws InterruptedException, ExecutionException {
        if (depth == 0)
            return KaratsubaSequential(first, second);

        if (first.getDegree() < 2 || second.getDegree() < 2)
            return RegularSequential(first, second);

        int length = Math.max(first.getDegree(), second.getDegree()) / 2;

        Polynomial firstLow = new Polynomial(first.getCoefficients().subList(0, length));
        Polynomial firstHigh = new Polynomial(first.getCoefficients().subList(length, first.getDegree() + 1));
        Polynomial secondLow = new Polynomial(second.getCoefficients().subList(0, length));
        Polynomial secondHigh = new Polynomial(second.getCoefficients().subList(length, second.getDegree() + 1));

        int newRemainingDepth = depth -1;
        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);
        Future<Polynomial> futureMultipliedHighParts = executor.submit(() -> KaratsubaParallelized(firstHigh, secondHigh, threadsNumber, newRemainingDepth));
        Future<Polynomial> futureMultipliedLowParts = executor.submit(() -> KaratsubaParallelized(firstLow, secondLow, threadsNumber, newRemainingDepth));
        Future<Polynomial> futureMultipliedCombined = executor.submit(() -> KaratsubaParallelized(Polynomial.addTwoPolynomials(firstLow, firstHigh),
                Polynomial.addTwoPolynomials(secondLow, secondHigh), threadsNumber, newRemainingDepth));

        executor.shutdown();


        Polynomial multipliedHighParts = futureMultipliedHighParts.get();
        Polynomial multipliedLowParts = futureMultipliedLowParts.get();
        Polynomial multipliedCombined = futureMultipliedCombined.get();

        executor.awaitTermination(50, TimeUnit.SECONDS);

        Polynomial subtract = Polynomial.subtractTwoPolynomials(
                Polynomial.subtractTwoPolynomials(multipliedCombined, multipliedLowParts), multipliedHighParts);
        Polynomial addedZerosHighParts = Polynomial.extendDegree(multipliedHighParts, 2 * length);
        Polynomial addedZerosSubtract = Polynomial.extendDegree(subtract, length);

        return Polynomial.addTwoPolynomials(
                Polynomial.addTwoPolynomials(addedZerosHighParts, multipliedLowParts), addedZerosSubtract);

    }
}
