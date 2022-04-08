package WACparser;

public class BreakStmt implements Stmt {
	public final String Break;
	
	public final String Semicol;

	public BreakStmt(final String Break, final  String Semicol) {
		this.Break = Break;
		this.Semicol = Semicol;
	}
	
	public boolean equals(final Object other) {
		if( other instanceof BreakStmt) 
		{
		    final BreakStmt otherBreakStmt = (BreakStmt)other;
		    return(Break.equals(otherBreakStmt.Break) && Semicol.equals(otherBreakStmt.Semicol));
		}
		else 
		{
			return false;
		}
		
	}
	
	
	public int hashCode() {
        return (Break.hashCode() + Semicol.hashCode());
    }
	public String toString() {
        return ( Break + Semicol );
    }

}
