package WACparser;

import java.util.List;

public class ClassDefinition implements Classdef {
	public final Exp classname;
	public final Exp extendsClassname;
	public final List<Vardec> classVariables;
	public final List<Param> parameters;
	public final Stmt stmt;
	public final List<Methoddef> methoddefs;
	
	
	//with extends and secondary classname; null will be passed in when there is no secondary classname
	public ClassDefinition(final Exp classname, final Exp extendsClassname, final List<Vardec> classVariables, final List<Param> parameters,
							final Stmt stmt, final List<Methoddef> methoddefs) {
		this.classname = classname;
		this.extendsClassname = extendsClassname;
		this.classVariables = classVariables;
		this.parameters = parameters;
		this.stmt = stmt;
		this.methoddefs = methoddefs;
	}
	
	public boolean equals(final Object other) {
		if (other instanceof ClassDefinition) {
			final ClassDefinition otherCD = (ClassDefinition)other;
			return (classname.equals(otherCD.classname) && extendsClassname.equals(otherCD.extendsClassname) && classVariables.equals(otherCD.classVariables)
					&& parameters.equals(otherCD.parameters) && stmt.equals(otherCD.stmt) && methoddefs.equals(otherCD.methoddefs));
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return (classname.hashCode() + extendsClassname.hashCode() + classVariables.hashCode() + parameters.hashCode()
				+ stmt.hashCode() + methoddefs.hashCode());
	}
	
	public String toString() {
		return ("Class Definition(" + classname.toString() + "," + extendsClassname.toString() + "," + classVariables.toString() + 
				"," + parameters.toString() + "," + stmt.toString() + "," + methoddefs.toString());
	}
}