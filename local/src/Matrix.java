//файл з усіма діями можливими над матрицею
public class Matrix {
    private final double[][] matrix;

    Matrix(double[][] matrix) {
        this.matrix = matrix;
    }

    // поверає поточну матрицю
    public double[][] get() {
        return this.matrix;
    }

    // знаходить максимальне значення
    public double getMax() {
        int rows = this.matrix.length;
        int columns = this.matrix[0].length;
        double max = matrix[0][0];

        // ітеруємося матрицею, якщо знаходимо значення більше за поточне, робимо
        // поточним його
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] > max) {
                    max = matrix[i][j];
                }
            }
        }

        return max;
    }

    // сума
    public Matrix add(Matrix m) {
        int rows = this.matrix.length;
        int columns = this.matrix[0].length;
        double[][] added = m.get();
        double[][] res = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                res[i][j] = matrix[i][j] + added[i][j];
            }
        }

        // повертаємо матрицю, щоб можна було продовжити викликати вже її методи
        return new Matrix(res);
    }

    // множення на число
    public Matrix multiplyBy(double num) {
        int rows = this.matrix.length;
        int columns = this.matrix[0].length;
        double[][] res = new double[rows][columns];

        // кожний елемент множимо на задане число
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                res[i][j] = matrix[i][j] * num;
            }
        }

        return new Matrix(res);
    }

    // множення матриць
    public Matrix multiply(Matrix m) {
        int rows = this.matrix.length;
        int columns = this.matrix[0].length;
        double[][] multiplier = m.get();
        double[][] res = new double[rows][columns];

        // ітеруємося матрицями і множемо значення за агоритмом
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double[] value = new double[multiplier.length];
                for (int k = 0; k < multiplier.length; k++) {
                    value[k] = matrix[i][k] * multiplier[k][j];
                }
                // викликаємо алгоритм Kahan для отримання фінального значення
                res[i][j] = this.kahanAlgorithm(value);
            }
        }

        return new Matrix(res);
    }

    // стандартна імплементація алгоритму
    private double kahanAlgorithm(double[] input) {
        double sum = 0;
        double c = 0;

        for (double el : input) {
            double t = sum + el;
            if (Math.abs(sum) >= Math.abs(el)) {
                c += sum - t + el;
            } else {
                c += el - t + sum;
            }
            sum = t;
        }
        return sum + c;
    }
}
