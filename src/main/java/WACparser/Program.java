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
}