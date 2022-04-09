package WACparser;

import java.util.List;

public class PrintlnStmt implements Stmt {
	public final Exp stmts;
	
	
	public PrintlnStmt(final Exp stmts) {
		this.stmts = stmts;
		
	}
	
	public boolean equals (final Object other) {
		if (other instanceof PrintlnStmt) {
			final PrintlnStmt otherPrintlnStmt = (PrintlnStmt)other;
			return (stmts.equals(otherPrintlnStmt.stmts));
		} else {
			return false;
		}
	}
	
	
	public String toString() {
		return ("PrintlnStmt(" + stmts.toString());
	}
}