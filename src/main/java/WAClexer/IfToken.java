package WAClexcer;

public class IfToken implements Token{
	public boolean eqauls(final Object other) {
		return other instanceof IfToken;
	}
	
	public int hashCode() {
		return 21;
	}
	
	public String toString() {
		return "if";
	}
}

