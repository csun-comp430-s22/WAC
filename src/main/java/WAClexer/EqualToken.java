package WAClexer;

public class EqualToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof EqualToken;
	}
	
	public int hashCode() {
		return 8;
	}
	
	public String toString() {
		return "=";
	}
}
