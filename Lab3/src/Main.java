import Approach.BaseApproach;
import Approach.ThreadPoolApproach;
import Approach.ThreadForEachTaskApproach;
import Enums.TaskType;
import Matrix.Matrix;

import java.time.Duration;
import java.time.Instant;

public class Main {
    private static TaskType taskType1 = TaskType.KTH_ELEMENT;
    private static TaskType taskType2 = TaskType.ROW;
    private static int numberThreads = 10;
    private static int firstMatrixRows = 8;
    private static int firsMatrixColumn = 13;
    private static int secondMatrixColumn = 11;

    public static void main(String[] args) {
        Matrix firstMatrix = new Matrix(firstMatrixRows, firsMatrixColumn);
        Matrix secondMatrix = new Matrix(firsMatrixColumn, secondMatrixColumn);
        Matrix resultMatrix = new Matrix(firstMatrixRows, secondMatrixColumn);
        Matrix resultMatrix2 = new Matrix(firstMatrixRows, secondMatrixColumn);
        firstMatrix.randomInitialise();
        secondMatrix.randomInitialise();
        System.out.println("First matrix\n"+firstMatrix);
        System.out.println("Second matrix\n"+secondMatrix);

        BaseApproach approach1 = new ThreadForEachTaskApproach(firstMatrix, secondMatrix, resultMatrix, taskType1, numberThreads);

        Instant start1 = Instant.now();
        approach1.computeResultMatrix();
        Instant finish1 = Instant.now();
        float elapsedTime1 = Duration.between(start1, finish1).toMillis();
        System.out.println("Thread for each task : "+elapsedTime1);

        BaseApproach approach2 = new ThreadPoolApproach(firstMatrix, secondMatrix, resultMatrix2, taskType2, numberThreads);

        Instant start2 = Instant.now();
        approach2.computeResultMatrix();
        Instant finish2 = Instant.now();
        float elapsedTime2 = Duration.between(start2, finish2).toMillis();
        System.out.println("Thread pool : "+elapsedTime2+'\n');

        System.out.println("Result matrix thread for each task\n"+resultMatrix);
        System.out.println("Result matrix thread pool\n"+resultMatrix2);
    }
}
