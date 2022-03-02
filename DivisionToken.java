public class DivisionToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof DivisionToken;
	}
	
	public int hashCode() {
		return 6;
	}
	
	public String toString() {
		return "/";
	}
}
