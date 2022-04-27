package WACparser;

public class ClassnameExp implements Exp {
	//public final Exp classname;
	public final Classname classname;

	//public ClassnameExp(final Exp classname) {
	public ClassnameExp(final Classname classname) {
		this.classname = classname;
	}

	public boolean equals(final Object other) {
		return (other instanceof ClassnameExp && classname.equals(((ClassnameExp) other).classname));
	}

	public int hashCode() {
		return classname.hashCode();
	}

	public String toString() {
		return "ClassnameExp(" + classname + ")";
	}
}