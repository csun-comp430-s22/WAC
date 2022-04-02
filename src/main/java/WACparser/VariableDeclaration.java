package WACparser;

public class VariableDeclaration implements Vardec {
	public final Type type;
	public final Exp variable;
	
	public VariableDeclaration(final Type type, final Exp variable) {
		this.type = type;
		this.variable = variable;
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