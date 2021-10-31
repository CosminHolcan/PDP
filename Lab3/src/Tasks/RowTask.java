package Tasks;

import Matrix.Matrix;

public class RowTask extends BaseTask {
    private final int numberElementsToCompute;
    private final int firstElementRow;
    private final int firstElementColumn;

    public RowTask(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix,
                   int numberElementsToCompute, int firstElementRow, int firstElementColumn) {
        super(firstMatrix, secondMatrix, resultMatrix);
        this.numberElementsToCompute = numberElementsToCompute;
        this.firstElementRow = firstElementRow;
        this.firstElementColumn = firstElementColumn;
    }

    @Override
    public void run() {
        int currentElementRow = this.firstElementRow;
        int currentElementColumn = this.firstElementColumn;
        for (int i=0; i< this.numberElementsToCompute; i++)
        {
            int currentElementValue = computeElementByPosition(currentElementRow, currentElementColumn);
            this.resultMatrix.setElement(currentElementRow, currentElementColumn, currentElementValue);
            if (currentElementColumn + 1 >= this.resultMatrix.getNumberColumns())
                currentElementRow++;
            currentElementColumn = (currentElementColumn + 1) % this.resultMatrix.getNumberColumns();
        }
    }
}
