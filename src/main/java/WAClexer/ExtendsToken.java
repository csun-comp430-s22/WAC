package WAClexer;

public class ExtendsToken implements Token {
	public boolean equals(final Object other) {
		return other instanceof ExtendsToken;
	}
	
	public int hashCode() {
		return 15;
	}
	
	public String toString() {
		return "extends";
	}
}

