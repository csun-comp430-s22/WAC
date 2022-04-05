package WACparser;

public class ClassnameType implements Type {
	public final VariableExp classname;
	
	public ClassnameType (final VariableExp classname) {
		this.classname = classname;
	}
	
	public boolean equals(final Object other) {
		return (other instanceof ClassnameType && classname.equals(((ClassnameType)other).classname));
	}
	
	public int hashCode() {
		return classname.hashCode();
	}
	
	public String toString() {
		return "ClassnameType(" + classname.name + ")";
	}
}

/* public class VariableExp implements Exp {
	public final String name;
	
	public VariableExp(final String name) {
		this.name = name;
	}
	
	public boolean equals(final Object other) {
		return (other instanceof VariableExp && name.equals(((VariableExp)other).name));
	}
	
	public int hashCode() {
		return name.hashCode();
	}
	
	public String toString() {
		return "VariableExp(" + name + ")";
	}
} */