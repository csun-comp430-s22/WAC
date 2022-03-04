public class SuperToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof SuperToken;
	}
	
	public int hashCode() {
		return 8;
	}
	
	public String toString() {
		return "super";
	}
}