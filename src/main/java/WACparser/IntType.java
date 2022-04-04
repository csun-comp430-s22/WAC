package WACparser;

public class IntType implements Type {

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "Int";
    }

    public boolean equals(final Object other) {
        return other instanceof IntType;
    }
}
