package WAClexer;

public class SuperToken implements Token {
	public boolean equals(final Object other) {
		return other instanceof SuperToken;
	}
	
	public int hashCode() {
		return 9;
	}
	
	public String toString() {
		return "super";
	}
}