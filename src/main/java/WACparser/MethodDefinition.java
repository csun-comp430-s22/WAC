package WACparser;

import java.util.List;

public class MethodDefinition {
	public final Type type;
	public final Methodname methodname;
	public final List<Parameter> params;	
	public final Stmt stmt;
	
	//public MethodDefinition(final Type type, final Exp methodname, final List<Param> params, final Stmt stmt) {
	public MethodDefinition(final Type type, final Methodname methodname, final List<Parameter> params, final Stmt stmt) {
		this.type = type;
		this.methodname = methodname;
		this.params = params;
		this.stmt = stmt;
	}
	
	public boolean equals(final Object other) {
		if (other instanceof MethodDefinition) {
			final MethodDefinition otherMD = (MethodDefinition)other;
			return (type.equals(otherMD.type) && methodname.equals(otherMD.methodname) && params.equals(otherMD.params) && stmt.equals(otherMD.stmt));
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return (type.hashCode() + methodname.hashCode() + params.hashCode() + stmt.hashCode());
	}
	
	public String toString() {
		return ("Methoddef(" + type.toString() + "," + methodname.toString() + "," + params.toString() + "," + stmt.toString());
	}
}