package WAClexer;

public class lessThanToken implements Token {

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
