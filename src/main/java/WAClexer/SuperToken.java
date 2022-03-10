package WAClexer;

public class SuperToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof SuperToken;
	}
	
	public int hashCode() {
		return 9;
	}
	
	public String toString() {
		return "super";
	}
}