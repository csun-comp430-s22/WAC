package WACparser;

// made for the 'exp ::= var.methodname(exp*)' situation
public class VarMethodCall implements Exp {
    public final Exp left;
    public final Exp varName;
    public final Exp inParens;

    public VarMethodCall(final Exp left, final Exp varName, final Exp inParens) {
        this.left = left;
        this.varName = varName;
        this.inParens = inParens;
    }

    public boolean equals(final Object other) {
        if (other instanceof VarMethodCall) {
            final VarMethodCall otherVarMethodCall = (VarMethodCall) other;
            return (left.equals(otherVarMethodCall.left) && varName.equals(otherVarMethodCall.varName) && inParens.equals(otherVarMethodCall.inParens));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (left.hashCode() + varName.hashCode() + inParens.hashCode());
    }

    public String toString() {
        return ("NewClassExp(" + left.toString() + ", " + varName.toString() + ", " + inParens.toString() + ")");
    }

}