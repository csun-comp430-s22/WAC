package WAClexer;

public class IntegerToken implements Token {
	public final int value;
	
	public IntegerToken(final int value) {
		this.value = value;
	}	
	
	public boolean equals(final Object other) {
		if (other instanceof IntegerToken) {
			final IntegerToken asInt = (IntegerToken)other;
			return value == asInt.value;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return value;
	}
	
	public String toString() {
		return "IntegerToken(" + value + ")";
	}
}