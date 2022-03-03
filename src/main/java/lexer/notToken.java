package main.java.lexer;

public class notToken {

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
