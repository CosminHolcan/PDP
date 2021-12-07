import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Polynomial first = Polynomial.getRandomPolynomial(10000);
        Polynomial second = Polynomial.getRandomPolynomial(10000);
//        System.out.println("first = " + first);
//        System.out.println("second = " + second);

        long startTime = System.currentTimeMillis();
        Polynomial regularSequential = ComputeProduct.RegularSequential(first, second);
        long finishTime = System.currentTimeMillis();
        System.out.println("Regular sequential : "+(finishTime-startTime));

        startTime = System.currentTimeMillis();
        Polynomial regularParallelized = ComputeProduct.RegularParallelized(first, second, 50);
        finishTime = System.currentTimeMillis();
        System.out.println("Regular parallelized : "+(finishTime-startTime));

        startTime = System.currentTimeMillis();
        Polynomial karatsubaSequential = ComputeProduct.KaratsubaSequential(first, second);
        finishTime = System.currentTimeMillis();
        System.out.println("Karatsuba sequential : "+(finishTime-startTime));

        startTime = System.currentTimeMillis();
        Polynomial karatsubaParallelized = ComputeProduct.KaratsubaParallelized(first, second, 8, 5);
        finishTime = System.currentTimeMillis();
        System.out.println("Karatsuba parallelized : "+(finishTime-startTime));

//        System.out.println("Regular sequential = "+regularSequential);
//        System.out.println("Regular parallelized = "+regularParallelized);
//        System.out.println("Karatsuba sequential = "+karatsubaSequential);
//        System.out.println("Karatsuba parallelized = "+karatsubaParallelized);
    }
}
