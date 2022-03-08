package WAClexer;

public class NewToken implements Token {
	public boolean equals(final Object other) {
		return other instanceof NewToken;
	}
	
	public int hashCode() {
		return 13;
	}
	
	public String toString() {
		return "new";
	}
}

