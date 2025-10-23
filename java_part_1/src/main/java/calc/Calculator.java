package calc;

import java.util.ArrayList;

public class Calculator {
    private ArrayList<Long> oldValues = new ArrayList<>();
    private long value;

    public Calculator(long value) {
        this.value = value;
    }

    public Calculator(int value) {
        this(Long.valueOf(value));
    }

    public Calculator plus(long x) {
        this.updateValue(value + x);

        return this;
    }

    public Calculator minus(long x) {
        this.updateValue(value - x);

        return this;
    }

    public Calculator times(long x) {
        this.updateValue(value * x);

        return this;
    }

    public Calculator div(long x) throws ArithmeticException {
        this.updateValue(value / x);

        return this;
    }

    public Calculator rollback()
            throws IllegalArgumentException, UnsupportedOperationException {
        return this.rollback(1);
    }

    public Calculator rollback(int step)
            throws IllegalArgumentException, UnsupportedOperationException {
        if (step <= 0) {
            throw new IllegalArgumentException("step must be greater than 0");
        }

        if (step > this.oldValues.size()) {
            throw new UnsupportedOperationException("You cannot rollback by that many steps.");
        }

        int oldIndex = this.oldValues.size() - step;

        this.value = this.oldValues.get(oldIndex);
        this.oldValues.subList(oldIndex, this.oldValues.size()).clear();

        return this;
    }

    public long result() {
        return this.value;
    }

    private void updateValue(long newValue) {
        this.oldValues.add(this.value);
        this.value = newValue;
    }
}
