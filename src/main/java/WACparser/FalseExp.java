package WACparser;

public class FalseExp implements Exp {
    public int hashCode() {
        return 26;
    }

    public String toString() {
        return "false";
    }

    public boolean equals(final Object other) {
        return other instanceof FalseExp;
    }
}
