package WACparser;

public class NewExp implements Exp {
    public boolean equals(final Object other) {
        return other instanceof NewExp;
    }

    public int hashCode() {
        return 13;
    }

    public String toString() {
        return "NewExp";
    }
}