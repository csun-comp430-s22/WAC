package WAClexer;

public class OpenparToken implements Token {
	public boolean equals(final Object other) {
		return other instanceof OpenparToken;
	}
	
	public int hashCode() {
		return 10;
	}
	
	public String toString() {
		return "(";
	}
}

