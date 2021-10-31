package Approach;

import Enums.TaskType;
import Matrix.Matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolApproach extends BaseApproach{
    public ThreadPoolApproach(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix,
                              TaskType taskType, int numberThreads) {
        super(firstMatrix, secondMatrix, resultMatrix, taskType, numberThreads);
    }

    @Override
    public void computeResultMatrix() {
        ExecutorService executorService = Executors.newFixedThreadPool(this.numberThreads);
        this.tasks.forEach(executorService::submit);
        executorService.shutdown();
    }
}
