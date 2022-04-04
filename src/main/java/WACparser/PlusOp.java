package WACparser;

public class PlusOp implements  Op{
	public boolean equals(final Object other) {
		return other instanceof PlusOp;
	}
	
	public int hashCode() {
		return 4;
	}
	
	public String toString() {
		return "PlusOp";
	}
}
