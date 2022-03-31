package WACparser;

public class DivisionOp implements Op {
	public boolean equals(final Object other) {
		return other instanceof DivisionOp;
	}
	
	public int hashCode() {
		return 3;
	}
	
	public String toString() {
		return "DivisionOp";
	}
}