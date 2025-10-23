import calc.Calculator;

public class App {
    public static void main(String[] args) {
        long x = new Calculator(5) // value = 5
                .plus(3)           // value = 5 + 3 = 8
                .plus(4)           // value = 8 + 4 = 12
                .minus(2)          // value = 12 - 2 = 10
                .times(10)         // value = 10 * 10 = 100
                .rollback()        // value = 10
                .div(5)            // value = 10 / 5 = 2
                .result();

        System.out.println(x); // x = 2
    }
}
