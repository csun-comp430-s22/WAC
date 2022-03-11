package WAClexer;

public class IfToken implements Token{
	public boolean equals(final Object other) {
		return other instanceof IfToken;
	}
	
	public int hashCode() {
		return 21;
	}
	
	public String toString() {
		return "if";
	}
}

