package WAClexer;

public class IntToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof IntToken;
	}
	
	public int hashCode() {
		return 0;
	}
	
	public String toString() {
		return "Int";
	}
}
