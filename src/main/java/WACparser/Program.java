package WACparser;

import java.util.List;

public class Program {
	public final List<ClassDefinition> classes;
	public final List<Stmt> stmts;
	
	public Program(final List<ClassDefinition> classes, final List<Stmt> stmts) {
		this.classes = classes;
		this.stmts = stmts;
	}
	
	public boolean equals(final Object other) {
		if (other instanceof Program) {
			final Program otherProgram = (Program)other;
			return (classes.equals(otherProgram.classes) && stmts.equals(otherProgram.stmts));
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return (classes.hashCode() + stmts.hashCode());
	}
	
	 public String toString() {
		return ("Program(" + classes.toString() + "," + stmts.toString() + ")");
	 }
}