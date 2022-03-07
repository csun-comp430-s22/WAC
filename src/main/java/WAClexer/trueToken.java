package WAClexer;

public class trueToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof trueToken;
    }

    public int hashCode() {
        return 25;
    }

    public String toString() {
        return "true";
    }
}
