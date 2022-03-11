package WAClexer;

public class DollarSignToken implements Token{
	public boolean equals(final Object other) {
		return other instanceof DollarSignToken;
	}
	
	public int hashCode() {
		return 18;
	}
	
	public String toString() {
		return "$";
	}
}
