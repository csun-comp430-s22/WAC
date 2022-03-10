package WAClexer;

public class PlusToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof PlusToken;
	}
	
	public int hashCode() {
		return 4;
	}
	
	public String toString() {
		return "+";
	}
}
