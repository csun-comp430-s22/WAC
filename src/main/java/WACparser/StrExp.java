package WACparser;

public class StrExp implements Exp {
    public final String value;

    public StrExp(final String value) {
        this.value = value;
    }

    public int hashCode() {
        return 26;
    }

    public String toString() {
        return "StringExp(" + value + ")";
    }

    public boolean equals(final Object other) {
        return (other instanceof StrExp && value.equals(((StrExp) other).value));
    }
}
