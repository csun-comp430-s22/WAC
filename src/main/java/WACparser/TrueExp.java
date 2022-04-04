package WACparser;

public class TrueExp implements Exp {

    public int hashCode() {
        return 25;
    }

    public String toString() {
        return "true";
    }

    public boolean equals(final Object other) {
        return other instanceof TrueExp;
    }
}
