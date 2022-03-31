package WAClexer;

public class PeriodToken implements Token {
	public boolean equals(final Object other) {
		return other instanceof PeriodToken;
	}
	
	public int hashCode() {
		return 32;
	}
	
	public String toString() {
		return ".";
	}
}
