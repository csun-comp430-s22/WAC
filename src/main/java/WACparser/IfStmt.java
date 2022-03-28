package WACparser;

public class IfStmt implements Stmt {
	public final Exp guard;
	public final Stmt trueBranch;
	public final Stmt falseBranch;
	
	public IfStmt(final Exp guard, final Stmt trueBranch, final Stmt falseBranch) {
		this.guard = guard;
		this.trueBranch = trueBranch;
		this.falseBranch = falseBranch;
	}
}