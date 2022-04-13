package WACparser;

import java.util.List;

public class PrintlnStmt implements Stmt {
	public final List<Exp> exps;
	
	
	public PrintlnStmt(final List<Exp> exps) {
		this.exps = exps;
		
	}
	
	public boolean equals (final Object other) {
		if (other instanceof PrintlnStmt) {
			final PrintlnStmt otherPrintlnStmt = (PrintlnStmt)other;
			return (exps.equals(otherPrintlnStmt.exps));
		} else {
			return false;
		}
	}
	
	
	public String toString() {
		return ("PrintlnStmt(" + exps.toString());
	}
}