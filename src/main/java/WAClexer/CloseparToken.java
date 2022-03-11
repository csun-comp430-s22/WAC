package WAClexer;

public class CloseparToken implements Token {
	public boolean equals(final Object other) {
		return other instanceof CloseparToken;
	}
	
	public int hashCode() {
		return 11;
	}
	
	public String toString() {
		return ")";
	}
}

