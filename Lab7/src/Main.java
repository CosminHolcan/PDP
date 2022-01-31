import mpi.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int POLYNOMIAL_ORDER = 1000;

    private static void mainProcess(Polynomial first, Polynomial second, int nrProcs) {
        long startTime = System.currentTimeMillis();
        int currentStartDegree, currentFinishDegree = 0;
        int lengthRangeDegrees = (first.getDegree() + 1 ) / (nrProcs - 1);

        for (int i = 1; i < nrProcs; i++) {
            currentStartDegree = currentFinishDegree;
            currentFinishDegree += lengthRangeDegrees;
            if (i == nrProcs - 1) {
                currentFinishDegree = first.getDegree() + 1;
            }
            MPI.COMM_WORLD.Send(new Object[]{first}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{second}, 0, 1, MPI.OBJECT, i, 0);

            MPI.COMM_WORLD.Send(new int[]{currentStartDegree}, 0, 1, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{currentFinishDegree}, 0, 1, MPI.INT, i, 0);

        }

        Object[] results = new Object[nrProcs - 1];
        for (int i = 1; i < nrProcs; i++) {
            MPI.COMM_WORLD.Recv(results, i - 1, 1, MPI.OBJECT, i, 0);
        }

        List<Integer> coefficients = new ArrayList<>();
        int productDegree = first.getDegree() + second.getDegree();
        for (int degree = 0; degree <= productDegree; degree++) {
            coefficients.add(0);
        }

        for (int degree = 0; degree <= productDegree; degree++)
            for (Object object : results) {
                int newValue = coefficients.get(degree) + ((Polynomial) object).getCoefficients().get(degree);
                coefficients.set(degree, newValue);
            }
        Polynomial result = new Polynomial(coefficients);
        long endTime = System.currentTimeMillis();

        System.out.println("Result : " + result);
        System.out.println("Time: " + (endTime - startTime) + " ms");
    }

    private static void multiplySimpleWorker() {
        Object[] p = new Object[2];
        Object[] q = new Object[2];
        int[] startDegree = new int[1];
        int[] finishDegree = new int[1];

        MPI.COMM_WORLD.Recv(p, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(q, 0, 1, MPI.OBJECT, 0, 0);

        MPI.COMM_WORLD.Recv(startDegree, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(finishDegree, 0, 1, MPI.INT, 0, 0);

        Polynomial first = (Polynomial) p[0];
        Polynomial second = (Polynomial) q[0];

        Polynomial result = ComputeProduct.RegularSequentialInRange(first, second, startDegree[0], finishDegree[0]);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);

    }

    private static void multiplyKaratsubaWorker() {
        Object[] p = new Object[2];
        Object[] q = new Object[2];
        int[] begin = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(p, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(q, 0, 1, MPI.OBJECT, 0, 0);

        MPI.COMM_WORLD.Recv(begin, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial first = (Polynomial) p[0];
        Polynomial second = (Polynomial) q[0];

        for (int i = 0; i < begin[0]; i++) {
            first.getCoefficients().set(i, 0);
        }
        for (int j = end[0]; j < first.getCoefficients().size(); j++) {
            first.getCoefficients().set(j, 0);

        }

        Polynomial result = ComputeProduct.KaratsubaSequential(first, second);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        if (me == 0) {
            Polynomial p = Polynomial.getRandomPolynomial(POLYNOMIAL_ORDER);
            Polynomial q = Polynomial.getRandomPolynomial(POLYNOMIAL_ORDER);

            System.out.println("First polynomial : " + p);
            System.out.println("Second polynomial : " + q);

            mainProcess(p, q, nrProcs);
        } else {
//            multiplySimpleWorker();
            multiplyKaratsubaWorker();
        }
        MPI.Finalize();
    }


}
