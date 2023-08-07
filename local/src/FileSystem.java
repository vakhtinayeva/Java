import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class FileSystem {

    // генеруэмо значення
    public double[][] generateValues(int rows, int columns, String type) {
        // створюємо матрицю заданого розміру
        double[][] matrix = new double[rows][columns];
        Random num = new Random();

        // для скалярів заповнюємо матрицю 0ми, окрім головної діагоналі
        if (type == "scalar") {
            // обираємо випадкове число для головної діагоналі
            double curNum = num.nextDouble(100);
            BigDecimal bd = new BigDecimal(Double.toString(curNum));
            bd = bd.setScale(num.nextInt(10), RoundingMode.HALF_UP);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (i == j) {
                        matrix[i][j] = bd.doubleValue();
                    } else {
                        matrix[i][j] = 0.0;
                    }
                }
            }
        } else {
            // звичайну матрицю або вектор заповнюємо повністю випадковими числами
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    double curNum = num.nextDouble(100);
                    BigDecimal bd = new BigDecimal(Double.toString(curNum));
                    bd = bd.setScale(num.nextInt(10), RoundingMode.HALF_UP);
                    matrix[i][j] = bd.doubleValue();
                }
            }
        }

        return matrix;
    }

    // запис даних
    public double[][] write(String name, double[][] matrix, String dir) {
        try {
            // визначаємо місце, де зберігаються файли
            // занеобхідністю створюємо нову папку
            String path = System.getProperty("user.dir") + "/" + dir;
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }

            String filename = path + "/" + name + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            // ітеруємося матрицею і записуємо в файл
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    writer.write(matrix[i][j] + "");

                    if (j != matrix[0].length - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }

            writer.flush();
            writer.close();

            return matrix;
        } catch (IOException e) {
            System.out.print("Error occured while writing " + name);
        }

        return null;
    }

    // зчитування даних
    public double[][] read(String name, int rows, int columns) {
        try {
            // визначаємо шлях до файлу
            String filename = System.getProperty("user.dir") + "/data/" + name + ".txt";
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String curLine = reader.readLine();

            double[][] matrix = new double[rows][columns];

            // лінія за лінією зчитуємо файл
            while (curLine != null) {
                for (int i = 0; i < rows; i++) {
                    String[] values = curLine.split(",");
                    for (int j = 0; j < values.length; j++) {
                        matrix[i][j] = Double.parseDouble(values[j]);
                    }
                }
                curLine = reader.readLine();
            }
            reader.close();

            return matrix;
        } catch (IOException e) {
            System.out.print("Error occured while reading " + name);
        }

        return null;
    }

    // запис часу виконання
    public void writeResults(long[] arr, String type) {
        try {
            String path = System.getProperty("user.dir") + "/time";
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }

            String filename = path + "/" + type + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            for (int i = 0; i < arr.length; i++) {
                writer.write(String.valueOf(arr[i]));
                writer.newLine();
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.print("Error occured while writing " + type);
        }
    }
}
