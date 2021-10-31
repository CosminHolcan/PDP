package Tasks;

import Matrix.Matrix;

public class KthElementTask extends BaseTask{
    private final int k;
    private final int indexTask;

    public KthElementTask(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix,
                          int k, int indexTask) {
        super(firstMatrix, secondMatrix, resultMatrix);
        this.k = k;
        this.indexTask = indexTask;
    }

    @Override
    public void run() {
        int currentElementRow = this.indexTask  / this.resultMatrix.getNumberColumns();
        int currentElementColumn = this.indexTask % this.resultMatrix.getNumberColumns();
        while (currentElementRow < this.resultMatrix.getNumberRows())
        {
            int currentElementValue = computeElementByPosition(currentElementRow, currentElementColumn);
            this.resultMatrix.setElement(currentElementRow, currentElementColumn, currentElementValue);
            if (currentElementColumn + this.k >= this.resultMatrix.getNumberColumns())
            {
                currentElementRow ++;
            }
            currentElementColumn = (currentElementColumn + this.k) % this.resultMatrix.getNumberColumns();
        }
    }
}
