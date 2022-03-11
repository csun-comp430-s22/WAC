package WAClexer;

public class BreakToken implements Token{
	public boolean eqauls(final Object other) {
		return other instanceof BreakToken;
	}
	
	public int hashCode() {
		return 20;
	}
	
	public String toString() {
		return "break";
	}
}