package WAClexer;

public class falseToken {

    public boolean equals(final Object other) {
        return other instanceof falseToken;
    }

    public int hashCode() {
        return 26;
    }

    public String toString() {
        return "false";
    }
}
