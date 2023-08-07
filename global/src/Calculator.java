import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Calculator {
    private Matrix E;
    private Matrix MA;

    private final Matrix B;
    private final Matrix C;
    private final Matrix a;
    private final Matrix MD;
    private final Matrix MT;
    private final Matrix MZ;
    private final Matrix MB;

    Calculator(Matrix B, Matrix C, Matrix a, Matrix MD, Matrix MT, Matrix MZ, Matrix MB) {
        this.B = B;
        this.C = C;
        this.a = a;
        this.MD = MD;
        this.MT = MT;
        this.MZ = MZ;
        this.MB = MB;
    }

    // повертаємо результати
    public Matrix getMatrix(String name) {
        if (name == "E")
            return this.E;
        else
            return this.MA;
    }

    // обчислюємо Е
    // E = B * MD + C * MT * a
    public void calculateE() {
        Matrix BMD = this.B.multiply(this.MD);
        Matrix CMTa = this.C.multiply(MT).multiply(a);
        this.E = BMD.add(CMTa);
    }

    // обчислюємо МА
    // MA = max(B + C) * MD * MT + MZ * MB
    public void calculateMA() {
        double maxBC = this.B.add(C).getMax();
        Matrix MDMT = this.MD.multiply(MT);
        Matrix maxBCMDMT = MDMT.multiplyBy(maxBC);
        Matrix MZMB = this.MZ.multiply(MB);
        this.MA = maxBCMDMT.add(MZMB);
    }

    // механізм синхронизації lock
    private final Lock lock = new ReentrantLock();
    // умови, за яких один з потоків має доступ до змінної
    private final Condition isOdd = lock.newCondition();
    private final Condition isEven = lock.newCondition();
    // змінна, до якої будемо "ділити" доступ
    int counter = 1;

    // Обчислюємо непарні ітерації
    public void calculateEvenCycle() {
        lock.lock();
        try {
            while (counter < 1000) {
                while (counter % 2 == 0) {
                        //чекаємо виклику .signal або переривання
                        isEven.await();
                    }

                    this.calculateE();
                    this.calculateMA();

                    counter = counter + 1;
                    //викликаємо поток, що чекає
                    isOdd.signal();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            //знімаємо lock
            lock.unlock();
        }
    }

    // Обчислюємо непарні ітерації
    public void calculateOddCycle() {
        lock.lock();
        try {
            while (counter < 1000) {
                while (counter % 2 == 1) {
                        isOdd.await();
                    }

                    this.calculateE();
                    this.calculateMA();

                    counter = counter + 1;
                    isEven.signal();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
