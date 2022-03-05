package WAClexer;

public class BooleanToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof BooleanToken;
	}
	
	public int hashCode() {
		return 1;
	}
	
	public String toString() {
		return "Boolean";
	}
}
