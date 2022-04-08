package WACparser;

public class MethodNameExp implements Exp {
	public final Methodname name;
	
	public MethodNameExp(final Methodname name) {
		this.name = name;
	}
	
	public boolean equals(final Object other) {
		return (other instanceof MethodNameExp && name.equals(((MethodNameExp)other).name));
	}
	
	public int hashCode() {
		return name.hashCode();
	}
	
	public String toString() {
		return "MethodNameExp(" + name + ")";
	}
}