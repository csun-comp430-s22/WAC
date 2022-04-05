package WACparser;

import java.util.List;

public class PrintlnStmt implements Stmt {
	public final Exp stmts;
	
	
	public PrintlnStmt(final Exp stmts) {
		this.stmts = stmts;
		
	}
}