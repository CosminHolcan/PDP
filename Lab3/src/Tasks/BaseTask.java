package Tasks;

import Matrix.Matrix;

public abstract class BaseTask implements Runnable {
    protected Matrix firstMatrix;
    protected Matrix secondMatrix;
    protected Matrix resultMatrix;

    public BaseTask(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
    }

    protected int computeElementByPosition(int row, int column)
    {
        int result = 0;
        for (int i=0; i< firstMatrix.getNumberColumns(); i++)
            result += firstMatrix.getElement(row, i) * secondMatrix.getElement(i, column);

        return result;
    }
}
