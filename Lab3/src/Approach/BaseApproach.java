package Approach;

import Enums.TaskType;
import Matrix.Matrix;
import Tasks.BaseTask;
import Tasks.ColumnTask;
import Tasks.KthElementTask;
import Tasks.RowTask;

import java.util.ArrayList;

public abstract class BaseApproach {
    protected Matrix firstMatrix;
    protected Matrix secondMatrix;
    protected Matrix resultMatrix;
    protected TaskType taskType;
    protected int numberThreads;
    protected ArrayList<BaseTask> tasks;

    public BaseApproach(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix, TaskType taskType, int numberThreads) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
        this.taskType = taskType;
        this.numberThreads = numberThreads;
        this.initialiseThreads();
    }

    private void initialiseThreads() {
        this.tasks = new ArrayList<>();
        switch (this.taskType) {
            case ROW -> {
                int numberElementsToCompute = this.resultMatrix.getNumberColumns() * this.resultMatrix.getNumberRows() / this.numberThreads;
                for (int i = 0; i < this.numberThreads; i++) {
                    int currentElementRow = i * numberElementsToCompute / this.resultMatrix.getNumberColumns();
                    int currentElementColumn = i * numberElementsToCompute % this.resultMatrix.getNumberColumns();
                    if (i == this.numberThreads - 1)
                        numberElementsToCompute = this.resultMatrix.getNumberRows() * this.resultMatrix.getNumberColumns() -
                                i * (numberElementsToCompute);
                    this.tasks.add(new RowTask(firstMatrix, secondMatrix,
                            resultMatrix, numberElementsToCompute, currentElementRow, currentElementColumn));
                }
            }
            case COLUMN -> {
                int numberElementsToCompute = this.resultMatrix.getNumberColumns() * this.resultMatrix.getNumberRows() / this.numberThreads;
                for (int i = 0; i < this.numberThreads; i++) {
                    int currentElementRow = i * numberElementsToCompute % this.resultMatrix.getNumberRows();
                    int currentElementColumn = i * numberElementsToCompute / this.resultMatrix.getNumberRows();
                    if (i == this.numberThreads - 1)
                        numberElementsToCompute = this.resultMatrix.getNumberRows() * this.resultMatrix.getNumberColumns() -
                                i * (numberElementsToCompute);
                    this.tasks.add(new ColumnTask(firstMatrix, secondMatrix,
                            resultMatrix, numberElementsToCompute, currentElementRow, currentElementColumn));
                }
            }
            case KTH_ELEMENT -> {
                for (int i = 0; i< this.numberThreads; i++)
                    this.tasks.add(new KthElementTask(firstMatrix, secondMatrix, resultMatrix, this.numberThreads, i));
            }
        }
    }

    public abstract void computeResultMatrix();
}
