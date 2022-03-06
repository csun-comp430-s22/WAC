package WAClexcer;

public class ElseToken implements Token{
	public boolean eqauls(final Object other) {
		return other instanceof ElseToken;
	}
	
	public int hashCode() {
		return 22;
	}
	
	public String toString() {
		return "else";
	}
}