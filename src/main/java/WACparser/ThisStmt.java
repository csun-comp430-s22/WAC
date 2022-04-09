package WACparser;

public class ThisStmt implements Stmt {

	public final VariableExp ThisVar;
	public final VariableExp Var;
<<<<<<< HEAD
	
	public ThisStmt(final VariableExp ThisVar ,final VariableExp Var) {
		
		this.Var = Var;
		this.ThisVar = ThisVar;
	}
	public boolean equals(final Object other) {
		if( other instanceof ThisStmt) 
		{
		    final ThisStmt otherThisStmt = (ThisStmt)other;
		    return(Var.equals(otherThisStmt.Var) && ThisVar.equals(otherThisStmt.ThisVar));
		}
		else 
		{
			return false;
		}
		
	}
=======

	public ThisStmt(final VariableExp ThisVar ,final VariableExp Var) {

		this.Var = Var;
		this.ThisVar = ThisVar;
	}
	public boolean equals(final Object other) {
		if( other instanceof ThisStmt) 
		{
		    final ThisStmt otherThisStmt = (ThisStmt)other;
		    return(Var.equals(otherThisStmt.Var) && ThisVar.equals(otherThisStmt.ThisVar));
		}
		else 
		{
			return false;
		}

	}
>>>>>>> origin/QA_testing
	public int hashCode() {
        return (ThisVar.hashCode() + Var.hashCode());
    }
	public String toString() {
        return ( "this"+"."+ ThisVar.variable.name +"="+ Var.variable.name + ";" );
    }

}
