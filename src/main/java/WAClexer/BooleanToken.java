package WAClexer;

public class BooleanToken implements Token {
	public boolean equals(final Object other) {
		return other instanceof BooleanToken;
	}
	
	public int hashCode() {
		return 2;
	}
	
	public String toString() {
		return "Boolean";
	}
}
