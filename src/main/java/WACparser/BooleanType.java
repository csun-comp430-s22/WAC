package WACparser;

public class BooleanType implements Type {

    public int hashCode() {
        return 2;
    }

    public String toString() {
        return "Boolean";
    }

    public boolean equals(final Object other) {
        return other instanceof BooleanType;
    }
}
