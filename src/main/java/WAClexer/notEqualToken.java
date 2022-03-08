package WAClexer;

public class notEqualToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof notEqualToken;
    }

    public int hashCode() {
        return 30;
    }

    public String toString() {
        return "!=";
    }
}
