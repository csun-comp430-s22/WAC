package WACparser;

public class WhileStmt implements Stmt {
	public final Exp exp;
	public final Stmt stmt;

	public WhileStmt(final Exp exp, final Stmt stmt) {
		this.exp = exp;
		this.stmt = stmt;
	}
	
	public boolean equals(final Object other) {
		if (other instanceof WhileStmt) {
			final WhileStmt otherWS = (WhileStmt)other;
			return (exp.equals(otherWS.exp) && stmt.equals(otherWS.stmt));
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return (exp.hashCode() + stmt.hashCode());
	}
	
	public String toString() {
		return ("While Statement(" + exp.toString() + "," + stmt.toString());
	}

}
