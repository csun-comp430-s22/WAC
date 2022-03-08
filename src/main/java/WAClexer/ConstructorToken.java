package WAClexer;

public class ConstructorToken implements Token{
	public boolean eqauls(final Object other) {
		return other instanceof ConstructorToken;
	}
	
	public int hashCode() {
		return 18;
	}
	
	public String toString() {
		return "constructor";
	}
}
