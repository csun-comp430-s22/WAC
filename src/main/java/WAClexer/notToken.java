package WAClexer;

public class notToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof notToken;
    }

    public int hashCode() {
        return 31;
    }

    public String toString() {
        return "!";
    }
}
