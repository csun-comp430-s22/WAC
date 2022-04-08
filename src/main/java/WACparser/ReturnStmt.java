package WACparser;

public class ReturnStmt implements Stmt {
	public final Exp exp;

	public ReturnStmt(final Exp exp) {
		this.exp = exp;
	}
	
	public boolean equals(final Object other) {
		if (other instanceof ReturnStmt) {
			final ReturnStmt otherReturnStmt = (ReturnStmt)other;
			return (exp.equals(otherReturnStmt.exp));
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return exp.hashCode();
	}
	
	public String toString() {
		return exp.toString();
	}

}
