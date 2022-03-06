package WAClexcer;

public class ReturnToken implements Token {
	public boolean eqauls(final Object other) {
		return other instanceof ReturnToken;
	}
	
	public int hashCode() {
		return 23;
	}
	
	public String toString() {
		return "return";
	}
}
