package WACparser;

public class LessThanOp implements Op {
	public boolean equals(final Object other) {
		return other instanceof LessThanOp;
	}
	
	public int hashCode() {
		return 27;
	}
	
	public String toString() {
		return "LessThanOp";
	}
}