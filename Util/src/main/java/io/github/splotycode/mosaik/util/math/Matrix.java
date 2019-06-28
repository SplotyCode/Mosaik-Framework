package io.github.splotycode.mosaik.util.math;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Matrix {

    @Getter private final int rows, cols;
    @Getter private final double[][] values;

    public Matrix(double[][] values) {
        this.values = values;
        this.rows = values.length;
        this.cols = values[0].length;
    }

    public Matrix(int rows, int cols) {
        this(new double[rows][cols]);
    }

    public Matrix(Vector... cols) {
        this(cols[0].getSize(), cols.length);
        for (int j = 0; j < this.cols; j++) {
            double[] b = cols[j].valueCopy();
            for (int i = 0; i < rows; i++) {
                values[i][j] = b[i];
            }
        }
    }

    public Matrix transpose() {
        Matrix C = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                C.values[j][i] = values[i][j];
            }
        }
        return C;
    }

    /**
     * @deprecated use {@link Matrix#add(Vector)}
     */
    @Deprecated
    public Matrix addVec(Vector b) {
        return add(b);
    }

    public Matrix add(Vector b) {
        Matrix C = new Matrix(rows, cols);
        double[] ba = b.valueCopy();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                C.values[i][j] = values[i][j] + ba[i];
            }
        }
        return C;
    }

    public Matrix add(Matrix b) {
        checkEqualDimensions(b);

        Matrix C = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                C.values[i][j] = values[i][j] + b.values[i][j];
            }
        }

        return C;
    }

    public Matrix subtract(Matrix B) {
        checkEqualDimensions(B);

        Matrix C = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                C.values[i][j] = values[i][j] - B.values[i][j];
            }
        }

        return C;
    }

    public Matrix multiply(Matrix B) {
        checkMulDimensions(B);

        Matrix C = new Matrix(rows, B.cols);
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < B.rows; k++) {
                for (int j = 0; j < B.cols; j++) {
                    C.values[i][j] += values[i][k] * B.values[k][j];
                }
            }
        }

        return C;
    }

    public Matrix hadamardMat(Matrix B) {
        checkEqualDimensions(B);
        Matrix C = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                C.values[i][j] = values[i][j] * B.values[i][j];
            }
        }
        return C;
    }

    public Vector multiply(Vector b) {
        checkVectorDimensions(b);
        double[] c = new double[rows];
        double[] ba = b.valueCopy();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                c[i] += values[i][j] * ba[j];
            }
        }
        return new Vector(c);
    }

    public Matrix multiply(double s) {
        Matrix C = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                C.values[i][j] = s * values[i][j];
            }
        }
        return C;
    }

    public Vector sumCols() {
        double[] c = new double[rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                c[i] += values[i][j];
            }
        }
        return new Vector(c);
    }

    public Vector[] getAllCols() {
        Vector[] result = new Vector[cols];
        for (int j = 0; j < cols; j++) {
            double[] v = new double[rows];
            for (int i = 0; i < rows; i++) {
                v[i] = values[i][j];
            }
            result[j] = new Vector(v);
        }
        return result;
    }

    public Vector getColumn(int col) {
        Vector vector = new Vector(rows);
        for (int i = 0; i < rows; i++) {
            vector.set(i, values[i][col]);
        }
        return vector;
    }

    private void checkEqualDimensions(Matrix b) {
        if (rows != b.rows || cols != b.cols) {
            throw new IllegalArgumentException("Wrong Matrix Size");
        }
    }

    private void checkVectorDimensions(Vector b) {
        if (cols != b.getSize()) {
            throw new IllegalArgumentException("Wrong Vector Size");
        }
    }

    private void checkMulDimensions(Matrix b) {
        if (cols != b.rows) {
            throw new RuntimeException("Wrong Matrix Size");
        }
    }

    public double get(int row, int col) {
        return values[row][col];
    }

    public double[][] copyAll() {
        double[][] C = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(values[i], 0, C[i], 0, cols);
        }
        return C;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (double[] mr : values) {
            for (double mi : mr) {
                result.append(mi).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
