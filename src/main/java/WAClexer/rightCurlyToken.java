package WAClexer;

public class rightCurlyToken {
    public boolean equals(final Object other) {
        return other instanceof rightCurlyToken;
    }

    public int hashCode() {
        return 24;
    }

    public String toString() {
        return "}";
    }
}
