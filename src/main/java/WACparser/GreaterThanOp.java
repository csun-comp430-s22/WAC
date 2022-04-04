package WACparser;

public class GreaterThanOp implements Op {
	public boolean equals(final Object other) {
		return other instanceof GreaterThanOp;
	}
	
	public int hashCode() {
		return 28;
	}
	
	public String toString() {
		return "GreaterThanOp";
	}
}
