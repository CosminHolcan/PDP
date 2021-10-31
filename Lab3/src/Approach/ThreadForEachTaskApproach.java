package Approach;

import Enums.TaskType;
import Matrix.Matrix;

import java.util.ArrayList;
import java.util.List;

public class ThreadForEachTaskApproach extends BaseApproach {
    public ThreadForEachTaskApproach(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix,
                                     TaskType taskType, int numberThreads) {
        super(firstMatrix, secondMatrix, resultMatrix, taskType, numberThreads);
    }

    @Override
    public void computeResultMatrix() {
        List<Thread> threads = new ArrayList<>();
        this.tasks.forEach(task -> threads.add(new Thread(task)));
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
