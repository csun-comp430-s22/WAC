package main.java.lexer;

public class greaterThanToken {

    public boolean equals(final Object other) {
        return other instanceof greaterThanToken;
    }

    public int hashCode() {
        return 28;
    }

    public String toString() {
        return ">";
    }
}
