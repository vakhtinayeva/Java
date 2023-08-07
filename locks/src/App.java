// //import java.util.Arrays;

//MA = max(B + C) * MD * MT + MZ * MB
//E = B * MD + C * MT * a
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class App {
    public static void main(String argvs[]) throws Exception {

        // файлова система - генерація значеннь, запис та зчитування даних
        FileSystem fs = new FileSystem();

        // створюємо і записуємо матриці в попередній частині
        double [][] vectorB = fs.generateValues(1, 10, "vector");
        double [][] vectorC = fs.generateValues(1, 10, "vector");
        double [][] scalarA = fs.generateValues(10, 10, "scalar");
        double [][] matrixMD = fs.generateValues(10, 10, "matrix");
        double [][] matrixMT = fs.generateValues(10, 10, "matrix");
        double [][] matrixMZ = fs.generateValues(10, 10, "matrix");
        double [][] matrixMB = fs.generateValues(10, 10, "matrix");

        fs.write("B", vectorB, "data");
        fs.write("C", vectorC, "data");
        fs.write("a", scalarA, "data");
        fs.write("MD", matrixMD, "data");
        fs.write("MT", matrixMT, "data");
        fs.write("MZ", matrixMZ, "data");
        fs.write("MB", matrixMB, "data");

        // зчитуємо вже створені значення
        Matrix B = new Matrix(fs.read("B", 1, 100));
        Matrix C = new Matrix(fs.read("C", 1, 100));
        Matrix a = new Matrix(fs.read("a", 100, 100));
        Matrix MD = new Matrix(fs.read("MD", 100, 100));
        Matrix MT = new Matrix(fs.read("MT", 100, 100));
        Matrix MZ = new Matrix(fs.read("MZ", 100, 100));
        Matrix MB = new Matrix(fs.read("MB", 100, 100));

        // System.out.println(Arrays.deepToString(fs.write("MD", matrixMD)));
        // System.out.println(" ");
        // System.out.println(Arrays.deepToString(fs.read("MD", 100, 100)));

        // обчислення рівнянь
        Calculator calculator = new Calculator(B, C, a, MD, MT, MZ, MB);

        // реалізація багатопотоковості із механізмом Callable, що використовується в парі з FutureTask
        FutureTask futureTask1 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                // запис часу
                long totalTime = 0;
                long beginning = System.nanoTime();

                // виклик парної функції обчислення матриці
                calculator.calculateEvenCycle();

                //запис результатів
                fs.write("E", calculator.getMatrix("E").get(), "resultsOn");
                fs.write("MA", calculator.getMatrix("MA").get(), "resultsOn");

                long end = System.nanoTime();
                long duration = (end - beginning) / 1000000;
                totalTime += duration;

                return Long.valueOf(totalTime);
            }
        });

        FutureTask futureTask2 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                long totalTime = 0;

                long beginning = System.nanoTime();

                // виклик непарної функції обчислення матриці
                calculator.calculateOddCycle();

                fs.write("E", calculator.getMatrix("E").get(), "resultsOn");
                fs.write("MA", calculator.getMatrix("MA").get(), "resultsOn");

                long end = System.nanoTime();
                long duration = (end - beginning) / 1000000;
                totalTime += duration;
                //можна замінити type на tsx-off для іншої версії
                fs.writeResults(totalTime, "tsx-on");

                return Long.valueOf(totalTime);
            }
        });

        Thread th1 = new Thread(futureTask1);
        Thread th2 = new Thread(futureTask2);
        th1.start();
        th2.start();

        // отримуємо результат із загальним часом після завершення роботи потоків
        Object obj1 = futureTask1.get();
        Object obj2 = futureTask2.get();

        // виводимо результат в консоль
        System.out.println("Total time " + obj1);
        System.out.println("Total time " + obj2);
    }
}
