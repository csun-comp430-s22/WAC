package WACparser;

import java.util.List;

// made for the 'exp ::= var.methodname(exp*)' situation
public class VarMethodCall implements Exp {
    public final Exp varName;
    //public final Exp methodName;
	public final MethodNameExp methodName;
	public final List<Exp> inParens;
	

    //public VarMethodCall(final Exp varName, final Exp methodName, final List<Exp> inParens) {
    public VarMethodCall(final Exp varName, final MethodNameExp methodName, final List<Exp> inParens) { 
		this.varName = varName;
		this.methodName = methodName;
        this.inParens = inParens;
    }

    public boolean equals(final Object other) {
        if (other instanceof VarMethodCall) {
            final VarMethodCall otherVarMethodCall = (VarMethodCall)other;
            return (varName.equals(otherVarMethodCall.varName) && methodName.equals(otherVarMethodCall.methodName) && inParens.equals(otherVarMethodCall.inParens));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (varName.hashCode() + methodName.hashCode() + inParens.hashCode());
    }

    public String toString() {
        return ("NewClassExp(" + varName.toString() + ", " + methodName.toString() + ", " + inParens.toString() + ")");
    }

}