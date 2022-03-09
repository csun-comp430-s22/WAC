package WAClexer;

public class MinusToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof MinusToken;
	}
	
	public int hashCode() {
		return 5;
	}
	
	public String toString() {
		return "-";
	}
}
