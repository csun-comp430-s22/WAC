package WACparser;

public class VariableValueChange implements Stmt {
	public final Exp variable;
	public final Exp exp;

	public VariableValueChange(final Exp variable, final Exp exp) {
		this.variable = variable;
		this.exp = exp;
	}
	
	public boolean equals (final Object other) {
		if (other instanceof VariableValueChange) {
			final VariableValueChange otherVVC = (VariableValueChange)other;
			return (variable.equals(otherVVC.variable) && exp.equals(otherVVC.exp));
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return (variable.hashCode() + exp.hashCode());
	}
	
	public String toString() {
		return ("VariableValueChange(" + variable.toString() + "," + exp.toString());
	}

}
