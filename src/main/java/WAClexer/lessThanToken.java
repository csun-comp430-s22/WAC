package WAClexer;

public class lessThanToken {

    public boolean equals(final Object other) {
        return other instanceof lessThanToken;
    }

    public int hashCode() {
        return 27;
    }

    public String toString() {
        return "<";
    }
}
