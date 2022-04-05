package WACparser;

public class ClassnameExp implements Exp {
	public final VariableExp classname;
	
	public ClassnameExp (final VariableExp classname) {
		this.classname = classname;
	}
	
	public boolean equals(final Object other) {
		return (other instanceof ClassnameType && classname.equals(((ClassnameType)other).classname));
	}
	
	public int hashCode() {
		return classname.hashCode();
	}
	
	public String toString() {
		return "ClassnameExp(" + classname.name + ")";
	}
}