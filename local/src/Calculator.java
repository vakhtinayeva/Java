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

    //повертаємо результати
    public Matrix getMatrix(String name) {
        if (name == "E")
            return this.E;
        else
            return this.MA;
    }

    //обчислюємо Е
    //E = B * MD + C * MT * a
    public void calculateE() {
        Matrix BMD = this.B.multiply(this.MD);
        Matrix CMTa = this.C.multiply(MT).multiply(a); 
        this.E = BMD.add(CMTa);
    }

    //обчислюємо МА
    //MA = max(B + C) * MD * MT + MZ * MB
    public void calculateMA() {
        double maxBC = this.B.add(C).getMax();
        Matrix MDMT = this.MD.multiply(MT);
        Matrix maxBCMDMT = MDMT.multiplyBy(maxBC);
        Matrix MZMB = this.MZ.multiply(MB);
        this.MA = maxBCMDMT.add(MZMB);
    }

    //запускаємо алгоритм обчислення віждповідно до потоків
    public void calculate(String name) {
        for (int j = 0; j < 1000; j++) {
            if (name == "First Thread") {
                this.calculateE();
            } else {
                this.calculateMA();
            }
        }
    }
}

