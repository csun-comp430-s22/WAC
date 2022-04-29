package WACtypechecker;

import WACparser.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Typechecker {
	
	//this might be changed later ???
	//public final List<Classdef> classes;
	public final List<ClassDefinition> classes;
	
	// recommended: we should make a map of Classname -> All methods on the class
	// recommended: Classname -> ParentClass
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
	public Type expectedReturnTypeForClassAndMethod(final Classname className, final Methodname methodName) {
		// WRONG: needs to find the given class and method, and return the expected return type for this
		return null;
	}
	
	// helper method for typeOfMethodCall
	// this currently doesn't handle inheritance since it was adapted from Kyle's original asynch videos
	// to add inheritance:
	// - Methods on that class
	// - Methods on the parent of that class
	public List<Type> expectedParameterTypesForClassAndMethod(final Classname className, final Methodname methodName) throws TypeErrorException {
		for (final ClassDefinition candidateClass : classes) {
			if (candidateClass.classname.equals(className)) {
				for (final MethodDefinition candidateMethod : candidateClass.methoddefs) {
					if (candidateMethod.methodname.equals(methodName)) {
						final List<Type> expectedTypes = new ArrayList<Type>();
						for (final Parameter param : candidateMethod.params) {
							expectedTypes.add(param.parameterType);
						}
						return expectedTypes;
					}
				}
			}
		}
		throw new TypeErrorException("No method named" + methodName + " on class " + className);
	}
	
	// helper method for isEqualOrSubtypeOf
	public boolean isSubtypeOf(final Type first, final Type second) throws TypeErrorException {
		// WRONG: needs to check this
		return true;
	}
	
	// helper method for typeOfMethodCall
	public void isEqualOrSubtypeOf(final Type first, final Type second) throws TypeErrorException {
		if (!(first.equals(second) || isSubtypeOf(first, second))) {
			throw new TypeErrorException("types incompatible: " + first + "," + second);
		}
	}
	
	// var.methodname(primary_exp*) in grammar
	// varName.methodName(inParens) in VarMethodCall.java
	// 1) varName should be a class
	// 2) varName needs to have the methodName method
	// 3) need to know the expected parameter types for the method
	public Type typeOfMethodCall(final VarMethodCall exp, final Map<Variable, Type> typeEnvironment, final Classname classWeAreIn) throws TypeErrorException {
		final Type varNameType = typeOf(exp.varName, typeEnvironment, classWeAreIn);
		if (varNameType instanceof ClassnameType) {
			final Classname className = ((ClassnameType)varNameType).classname;
			final List<Type> expectedTypes = expectedParameterTypesForClassAndMethod(className, exp.methodName.name);
			if (expectedTypes.size() != exp.inParens.size()) {
				throw new TypeErrorException("Wrong number of parameters for call: " + exp);
			}
			for (int index = 0; index < expectedTypes.size(); index++) {
				final Type paramType = typeOf(exp.inParens.get(index), typeEnvironment, classWeAreIn);
				final Type expectedType = expectedTypes.get(index);
				isEqualOrSubtypeOf(paramType, expectedType);
			}
			return expectedReturnTypeForClassAndMethod(className, exp.methodName.name);
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
			return typeOfMethodCall((VarMethodCall)exp, typeEnvironment, classWeAreIn);
		}
		else {
			throw new TypeErrorException("");
		}
	}
}