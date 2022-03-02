public class MultiplicationToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof MultiplicationToken;
	}
	
	public int hashCode() {
		return 5;
	}
	
	public String toString() {
		return "*";
	}
}
