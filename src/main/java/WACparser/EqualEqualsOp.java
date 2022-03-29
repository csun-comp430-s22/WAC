package WACparser;

public class EqualEqualsOp implements Op {
	public boolean equals(final Object other) {
		return other instanceof EqualEqualsOp;
	}
	
	public int hashCode() {
		return 4;
	}
	
	public String toString() {
		return "EqualsOp";
	}
}