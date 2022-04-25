package WACparser;

public class ExpStmt implements Stmt {
	public final Exp exp;
	
	public ExpStmt(final Exp exp) {
		this.exp = exp;
	}
	
	public boolean equals(final Object other) {
		if (other instanceof ExpStmt) {
			final ExpStmt otherExpStmt = (ExpStmt)other;
			return (exp.equals(otherExpStmt.exp));
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