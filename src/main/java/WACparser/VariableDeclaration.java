package WACparser;

public class VariableDeclaration {
	public final Type type;
	public final Exp variable;
	public final Exp value;
	
	public VariableDeclaration(final Type type, final Exp variable, final Exp value) {
		this.type = type;
		this.variable = variable;
		this.value = value;
	}
	
	public boolean equals(final Object other) {
		if (other instanceof VariableDeclaration) {
			final VariableDeclaration otherVD = (VariableDeclaration)other;
			return (type.equals(otherVD.type) && variable.equals(otherVD.variable));
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return (type.hashCode() + variable.hashCode());
	}
	
	public String toString() {
		return ("VariableDeclaration(" + type.toString() + ", " + variable.toString() + ")");
	}
}