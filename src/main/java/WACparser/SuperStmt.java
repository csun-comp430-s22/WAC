package WACparser;

public class SuperStmt implements Stmt {
 //public final String Super;
 public final Exp variable;
 
	public SuperStmt(final Exp Variable) {
	 //this.Super = Super;
	 this.variable = Variable;
	}
	public boolean equals(final Object other) {
		if( other instanceof SuperStmt) 
		{
		    final SuperStmt otherSuperStmt = (SuperStmt)other;
		    return(variable.equals(otherSuperStmt.variable));
		}
		else 
		{
			return false;
		}
		
	}
	public int hashCode() {
        return (variable.hashCode());
    }
	public String toString() {
        return ("Super: ("+ variable.toString()+")"+ ";" );
    }
}
