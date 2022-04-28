/* package WACtypechecker;

import WACparser.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Typechecker {
	
	public final List<Classdef> classes;
	
	public Typechecker(final Program program) {
		this.classes = program.classes;
		// TODO: check that class hierarchy is a tree
		// I believe he's talking about checking for cycles in inheritance?
	}
	
	
	public static Type typeOfVariable(final VariableExp exp, final Map<Variable, Type> typeEnvironment) throws TypeErrorException {
		final Type mapType = typeEnvironment.get(exp.variable);
		if (mapType == null) {
			throw new TypeErrorException("Used variable note in scope: " + exp.variable.name);
		} else {
			return mapType;
		}
	}
	
	
	public Type typeofOp(final OpExp exp, final Map<Variable, Type> typeEnvironment, final Classname classWeAreIn) throws TypeErrorException {
		final Type leftType = typeOf(exp.left, typeEnvironment, classWeAreIn);
		final Type rightType = typeOf(exp.right, typeEnvironment, classWeAreIn);
		if ((exp.op instanceof MultiplicationOp) || (exp.op instanceof DivisionOp) || (exp.op instanceof PlusOp) || (exp.op instanceof MinusOp)) {
			if (leftType instanceof IntType && rightType instanceof IntType) {
				return new IntType();
			} else {
				throw new TypeErrorException("Only integer operands allowed for arithmetic operations. Given: " + leftType + " and " + rightType);
			}
		}
		else if ((exp.op instanceof LessThanOp) || (exp.op instanceof GreaterThanOp) || (exp.op instanceof EqualEqualsOp) || (exp.op instanceof NotEqualsOp)) {
			if (leftType instanceof IntType && rightType instanceof IntType) {
				return new BooleanType();
			} else {
				throw new TypeErrorException("Only integer operands allowed for comparison operations. Given: " + leftType + " and " + rightType);
			}
		} else {
			throw new TypeErrorException("Unsupported operator: " + exp.op);
		}
	}
	
	// helper method for typeOfMethodCall
	// this currently doesn't handle inheritance since it was adapted from Kyle's original asynch videos
	public List<Type> expectedParameterTypesForClassAndMethod(final Classname className, final Methodname methodName) throws TypeErrorException {
		for (final Classdef candidateClass : classes) {
			if (candidateClass.classname
	}
	
	// var.methodname(primary_exp*) in grammar
	// varName.methodName(inParens) in VarMethodCall.java
	// 1) varName should be a class
	// 2) varName needs to have the methodName method
	// 3) need to know the expected parameter types for the method
	public Type typeOfMethodCall(final VarMethodCall exp, final Map<Variable, Type> typeEnvironment, final Classname classWeAreIn) throws TypeErrorException {
		final Type varNameType = typeOf(exp.varName, typeEnvironment, classWeAreIn);
		if (varName instanceof ClassnameType) {
			final Classname = className = ((ClassnameType)varNameType).classname;
		} else {
			throw new TypeErrorException("Called method on non-class type: " + varNameType);
		}
	}
	
	//classWeAreIn is null if we are in (one of the entry points?) the entry point.
	public Type typeOf(final Exp exp, final Map<Variable, Type> typeEnvironment,  final Classname classWeAreIn) throws TypeErrorException {
		if (exp instanceof TrueExp) {
			return new BooleanType();
		} else if (exp instanceof FalseExp) {
			return new BooleanType();
		} else if (exp instanceof IntegerExp) {
			return new IntType();
		} else if (exp instanceof StrExp) {
			return new StringType();
		} else if (exp instanceof ClassnameExp) {
			return new ClassnameType(((ClassnameExp)exp).classname);
		} else if (exp instanceof VariableExp) {
			return typeOfVariable((VariableExp)exp, typeEnvironment);
		} else if (exp instanceof OpExp) {
			return typeofOp((OpExp)exp, typeEnvironment, classWeAreIn);
		} else if (exp instanceof VarMethodCall) {
			return typeofMethodCall((VarMethodCall)exp, typeEnvironment, classWeAreIn);
		}
		else {
			throw new TypeErrorException("");
		}
	}
} */