package WAClexer;

public class CommaToken implements Token{
	public boolean equals(final Object other) {
		return other instanceof CommaToken;
	}
	
	public int hashCode() {
		return 100;
	}
	
	public String toString() {
		return ",";
	}
}