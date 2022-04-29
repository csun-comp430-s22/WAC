package WACparser;

public class VardecStmt implements Stmt {
//    public final Vardec variableDec;
    public final ParseResult<VariableDeclaration> variableDec;

    public VardecStmt (final ParseResult<VariableDeclaration> variableDec) {
        this.variableDec = variableDec;
    }

    public boolean equals(final Object other) {
        if (other instanceof VardecStmt) {
            final VardecStmt otherVStmt = (VardecStmt)other;
            return (variableDec.equals(otherVStmt.variableDec));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (variableDec.hashCode());
    }

    public String toString() {
        return ("VardecStmt(" + variableDec.toString() + ")");
    }
}