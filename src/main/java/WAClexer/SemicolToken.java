package WAClexer;

public class SemicolToken implements Token {
	public boolean equals(final Object other) {
		return other instanceof SemicolToken;
	}
	
	public int hashCode() {
		return 12;
	}
	
	public String toString() {
		return ";";
	}
}

