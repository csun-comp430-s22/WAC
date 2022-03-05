package WAClexer;

public class EqualToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof EqualToken;
	}
	
	public int hashCode() {
		return 7;
	}
	
	public String toString() {
		return "=";
	}
}
