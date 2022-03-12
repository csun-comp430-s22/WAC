package WAClexer;

public class strToken implements Token {
	public final String value;
	
	public strToken(final String value) {
		this.value = value;
	}
	
	public int hashCode() {
		return value.hashCode();
	}
	
	public String toString() {
		return "String(" + value + ")";
	}
	
	public boolean equals(final Object other) {
		if (other instanceof strToken) {
			final strToken asStr = (strToken)other;
			return value.equals(asStr.value);
		} else {
			return false;
		}
	}
}
