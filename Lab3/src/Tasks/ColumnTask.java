package Tasks;

import Matrix.Matrix;

public class ColumnTask extends BaseTask {
    private final int numberElementsToCompute;
    private final int firstElementRow;
    private final int firstElementColumn;

    public ColumnTask(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix,
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
            if (currentElementRow + 1 >= this.resultMatrix.getNumberRows())
                currentElementColumn++;
            currentElementRow = (currentElementRow + 1) % this.resultMatrix.getNumberRows();
        }
    }
}
