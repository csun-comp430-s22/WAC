package main.java.lexer;

public class equalEqualToken {

    public boolean equals(final Object other) {
        return other instanceof equalEqualToken;
    }

    public int hashCode() {
        return 29;
    }

    public String toString() {
        return "==";
    }
}
