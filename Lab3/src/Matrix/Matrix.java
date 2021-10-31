package Matrix;

import java.util.Random;

public class Matrix {
    private int numberRows;
    private int numberColumns;
    private int[][] elements;

    public Matrix(int numberRows, int numberColumns) {
        this.numberRows = numberRows;
        this.numberColumns = numberColumns;
        this.elements = new int[numberRows][numberColumns];
    }

    public int getNumberRows() {
        return numberRows;
    }

    public int getNumberColumns() {
        return numberColumns;
    }

    public void randomInitialise() {
        for (int i = 0; i < this.numberRows; i++)
            for (int j = 0; j < this.numberColumns; j++)
                this.elements[i][j] = new Random().nextInt(10);
    }

    public int getElement(int row, int column) {
        return this.elements[row][column];
    }

    public void setElement(int row, int column, int value)
    {
        this.elements[row][column] = value;
    }

    @Override
    public String toString() {
        String toReturn = "";
        for (int i = 0; i < this.numberRows; i++) {
            for (int j = 0; j < this.numberColumns; j++)
                toReturn += this.elements[i][j] + " ";
            toReturn += '\n';
        }

        return toReturn;
    }
}
