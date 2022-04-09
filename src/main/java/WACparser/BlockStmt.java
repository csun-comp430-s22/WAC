package WACparser;

import java.util.List;

public class BlockStmt implements Stmt {
	public final List<Stmt> stmts;
	
	public BlockStmt(final List<Stmt> stmts) {
		this.stmts = stmts;
	}
	
	public boolean equals(final Object other) {
		if (other instanceof BlockStmt) {
			final BlockStmt otherBS = (BlockStmt)other;
			return stmts.equals(otherBS.stmts);
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return stmts.hashCode();
	}
	
	public String toString() {
		return stmts.toString();
	}
}