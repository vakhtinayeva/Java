//import java.util.Arrays;

//MA = max(B + C) * MD * MT + MZ * MB
//E = B * MD + C * MT * a

public class App {
    public static void main(String[] args) throws InterruptedException {
        //файлова система - генерація значеннь, запис та зчитування даних
        FileSystem fs = new FileSystem();

        //генеруємо значення матриць
        double [][] vectorB = fs.generateValues(1, 100, "vector");
        double [][] vectorC = fs.generateValues(1, 100, "vector");
        double [][] scalarA = fs.generateValues(100, 100, "scalar");
        double [][] matrixMD = fs.generateValues(100, 100, "matrix");
        double [][] matrixMT = fs.generateValues(100, 100, "matrix");
        double [][] matrixMZ = fs.generateValues(100, 100, "matrix");
        double [][] matrixMB = fs.generateValues(100, 100, "matrix");

        //записуємо їх у файл
        fs.write("B", vectorB, "data");
        fs.write("C", vectorC, "data");
        fs.write("a", scalarA, "data");
        fs.write("MD", matrixMD, "data");
        fs.write("MT", matrixMT, "data");
        fs.write("MZ", matrixMZ, "data");
        fs.write("MB", matrixMB, "data");

        //зчитуємо створенні значення
        Matrix B = new Matrix(fs.read("B", 1, 100));
        Matrix C = new Matrix(fs.read("C",  1, 100));
        Matrix a = new Matrix(fs.read("a",  100, 100));
        Matrix MD = new Matrix(fs.read("MD", 100, 100));
        Matrix MT = new Matrix(fs.read("MT",100, 100));
        Matrix MZ = new Matrix(fs.read("MZ",100, 100));
        Matrix MB = new Matrix(fs.read("MB",100, 100));

        //за необхідності вивід матриць на екран
        // System.out.println(Arrays.deepToString(fs.write("MD", matrixMD)));
        // System.out.println(Arrays.deepToString(fs.read("MD", 10, 10)));

        //обчислення рівнянь
        Calculator calculator = new Calculator(B, C, a, MD, MT, MZ, MB);

        //створення потоків
        Multithreading thread1 = new Multithreading("First Thread", calculator, fs);
        Multithreading thread2 = new Multithreading("Second Thread", calculator, fs);

        Thread t1 = thread1.start();
        Thread t2 = thread2.start();

        t1.join();
        t2.join();  
    }
}