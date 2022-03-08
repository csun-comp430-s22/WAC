package WAClexer;

public class ThisToken implements Token {
	public boolean equals(final Object other) {
		return other instanceof ThisToken;
	}
	
	public int hashCode() {
		return 8;
	}
	
	public String toString() {
		return "this";
	}
}

