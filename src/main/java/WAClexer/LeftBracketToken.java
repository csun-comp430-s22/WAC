package WAClexer;

public class LeftBracketToken implements Token{
	public boolean eqauls(final Object other) {
		return other instanceof LeftBracketToken;
	}
	
	public int hashCode() {
		return 24;
	}
	
	public String toString() {
		return "{";
	}
}

