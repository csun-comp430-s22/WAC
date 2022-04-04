package WACparser;

public class StringType implements Type {

    public int hashCode() {
        return 3;
    }

    public String toString() {
        return "String";
    }

    public boolean equals(final Object other) {
        return other instanceof StringType;
    }
}
