package matrix;

public class FloatMatrix {

    private final int rows, cols;
    private final float[][] data;

    public FloatMatrix(FloatMatrix matrix) {
        this(matrix.data, false);
    }

    public FloatMatrix(float[][] data) {
        this(data, false);
    }

    private FloatMatrix(float[][] data, boolean useReference) {
        this.rows = data.length;
        this.cols = data[0].length;
        if (useReference) {
            this.data = data;
            return;
        }

        this.data = new float[rows][cols];
        for (int r = 0; r < rows; r++) {
            throwIf(data[r].length != cols, "Illegal matrix dimensions");
            this.data[r] = new float[cols];
            System.arraycopy(data[r], 0, this.data[r], 0, cols);
        }
    }

    public FloatMatrix times(FloatMatrix that) {
        throwIf(this.cols != that.rows, "Illegal matrix dimensions");
        float[][] result = new float[this.rows][that.cols];
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < that.cols; c++) {
                for (int s = 0; s < this.cols; s++) {
                    result[r][c] += this.data[r][s] * that.data[s][c];
                }
            }
        }
        return new FloatMatrix(result, true);
    }

    public FloatMatrix apply(FloatFunction function) {
        float[][] result = new float[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = function.apply(data[r][c]);
            }
        }
        return new FloatMatrix(result, true);
    }

    public FloatMatrix transpose() {
        float[][] result = new float[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = data[c][r];
            }
        }
        return new FloatMatrix(result, true);
    }

    public float sum() {
        float result = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result += data[r][c];
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result.append(String.format("%-10f ", data[r][c]));
            }
            result.append("\n");
        }
        return result.toString();
    }

    private static void throwIf(boolean condition, String msg) {
        if (condition)
            throw new IllegalArgumentException(msg);
    }


    public static void main(String[] args) {
        FloatMatrix input = new FloatMatrix(new float[][] {{1f, 1f, 1f}});
        FloatMatrix layer1 = new FloatMatrix(new float[3][4]).apply(x -> (float) Math.random());
        FloatMatrix layer2 = new FloatMatrix(new float[4][1]).apply(x -> (float) Math.random());
        System.out.println(input);
        System.out.println(layer1);
        System.out.println(layer2);
        System.out.println(input.times(layer1).apply(x -> 1f / (1f + (float) Math.exp(-x))));

        System.out.println(input);
        System.out.println(input.apply(x -> x * 3f).sum());
    }
    

}
