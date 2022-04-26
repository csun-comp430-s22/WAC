package WACparser;

import java.util.List;

public class Program {
	//old code
/* 	public final Stmt stmt;
	
	public Program(final Stmt stmt) {
		this.stmt = stmt;
	} */
	
	//new code
	public final List<Classdef> classes;
	public final List<Stmt> stmts;
	
	public Program(final List<Classdef> classes, final List<Stmt> stmts) {
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