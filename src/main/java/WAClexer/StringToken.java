package WAClexer;

public class StringToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof StringToken;
	}
	
	public int hashCode() {
		return 2;
	}
	
	public String toString() {
		return "String";
	}
}
