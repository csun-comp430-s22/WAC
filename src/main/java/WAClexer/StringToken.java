package WAClexer;

public class StringToken implements Token {
	public boolean equals(final Object other) {
		return other instanceof StringToken;
	}
	
	public int hashCode() {
		return 3;
	}
	
	public String toString() {
		return "String";
	}
}
