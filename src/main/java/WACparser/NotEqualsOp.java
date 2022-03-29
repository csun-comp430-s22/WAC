package WACparser;

public class NotEqualsOp implements Op {
	public boolean equals(final Object other) {
		return other instanceof NotEqualsOp;
	}
	
	public int hashCode() {
		return 7;
	}
	
	public String toString() {
		return "NotEqualsOp";
	}
}