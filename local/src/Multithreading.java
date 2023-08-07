class Multithreading implements Runnable {
    private Thread t;
    private final String thread;
    private final FileSystem fs;
    private final Calculator calculator;

    Multithreading(String thread, Calculator calculator, FileSystem fs) {
        this.thread = thread;
        this.calculator = calculator;
        this.fs = fs;
    }

    public void run() {
        //загальний час
        long totalTime = 0;
        // масив з часом виконання кожної з 10 ітерацій
        long[] loopTime = new long[10];

        for (int i = 1; i < 11; i++) {
            //початок
            long beginning = System.nanoTime();

            //запускаємо обчислення
            this.calculator.calculate(thread);

            //1 поток рахує матрицю Е, другий МА, записуємо відповідні результати
            if (thread == "First Thread") {
                fs.write("E", this.calculator.getMatrix("E").get(), "results1");
            } else {
                fs.write("MA", this.calculator.getMatrix("MA").get(), "results1");
            }

            //кінець
            long end = System.nanoTime();

            //час виконання ітерації
            long duration = (end - beginning) / 1000000;
            loopTime[i - 1] = duration;

            // загальний час
            totalTime += duration;
        }

        // Записуємо масив з часом виконання в окремий файл
        fs.writeResults(loopTime, "timeLocal");

        System.out.println("Total time " + totalTime);
    }

    //запускаэмо потоки
    public Thread start() {
        t = new Thread(this, thread);
        t.start();
        return t;
    }
}
