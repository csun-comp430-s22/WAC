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
			expressionsOk(expectedTypes, exp.inParens, typeEnvironment, classWeAreIn);
			return expectedReturnTypeForClassAndMethod(className, exp.methodName.name);
		} else {
			throw new TypeErrorException("Called method on non-class type: " + varNameType);
		}
	}
	
	
	public void expressionsOk(final List<Type> expectedTypes, final List<Exp> receivedExpressions, final Map<Variable,Type> typeEnvironment, final Classname classWeAreIn) throws TypeErrorException {
		if (expectedTypes.size() != receivedExpressions.size()) {
			throw new TypeErrorException("Wrong number of parameters for call: ");
		}
		for (int index = 0; index < expectedTypes.size(); index++) {
			final Type paramType = typeOf(receivedExpressions.get(index), typeEnvironment, classWeAreIn);
			final Type expectedType = expectedTypes.get(index);
			isEqualOrSubtypeOf(paramType, expectedType);
		}
	}
	
	
	// helper method for typeOfNew 
	public List<Type> expectedConstructorTypesForClass(final Classname className) throws TypeErrorException {
		// WRONG - needs to grab the expected constructor types for this class
		// throws an expception if this class doesn't exist
		return null;
	}
	
	
	// new classname(exp*) in grammar
	// new className(inParens) in NewClassExp.java
	public Type typeOfNew(final NewClassExp exp, final Map<Variable, Type> typeEnvironment, final Classname classWeAreIn) throws TypeErrorException {
		//need to know what the constructor arguments for this class are
		final List<Type> expectedTypes = expectedConstructorTypesForClass(exp.className.classname);
		expressionsOk(expectedTypes, exp.inParens, typeEnvironment, classWeAreIn);
		return new ClassnameType(exp.className.classname);
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
		} else if (exp instanceof NewClassExp) {
			return typeOfNew((NewClassExp)exp, typeEnvironment, classWeAreIn);
		}
		else {
			throw new TypeErrorException("Unrecognized expression: " + exp);
		}
	}


	// Add to map helper method method
	public static Map<Variable, Type> addToMap(final Map<Variable, Type> map,
											   final Variable variable,
											   final Type type) {
		final Map<Variable, Type> result = new HashMap<Variable, Type>();
		result.putAll(map);
		result.put(variable, type);
		return result;
	}	// addToMap


	// vardec
	public Map<Variable, Type> isWellTypedVar(final VariableDeclaration stmt,
											  final Map<Variable, Type> typeEnvironment,
											  final Classname classWeAreIn) throws TypeErrorException {
		final Type expType = typeOf(stmt.value, typeEnvironment, classWeAreIn);
		isEqualOrSubtypeOf(expType, stmt.type);
		return addToMap(typeEnvironment, (Variable)stmt.variable, stmt.type);
		// if above doesn't work I think this might work:
		// return addToMap(typeEnvironment, (((VariableExp)stmt.variable).variable, stmt.type);
	}	// isWellTypedVar


	// var = exp;
	public Map<Variable, Type> isWellTypedValueChange( final VariableValueChange stmt,
													   final Map<Variable, Type> typeEnvironment,
													   final Classname classWeAreIn) throws TypeErrorException {
		final Type varType = typeOf(stmt.variable, typeEnvironment, classWeAreIn);
		final Type expType = typeOf(stmt.exp, typeEnvironment, classWeAreIn);
		isEqualOrSubtypeOf(expType, varType);
		return typeEnvironment;	// correct since we are just changing the value of that variable but the type remains the same
	}	// isWellTypedValueChange


	// while (exp)  stmt
	public Map<Variable, Type> isWellTypedWhile(final WhileStmt stmt,
												final Map<Variable, Type> typeEnvironment,
												final Classname classWeAreIn,
												final Type functionReturnType) throws TypeErrorException {
		if (typeOf(stmt.exp, typeEnvironment, classWeAreIn) instanceof BooleanType) {
			isWellTypedStmt(stmt.stmt, typeEnvironment, classWeAreIn, functionReturnType);
			return typeEnvironment;
		} else {
			throw new TypeErrorException("guard on while is not a boolean: " + stmt);
		}
	}	// isWellTypedWhile
	
	
 	public Map<Variable, Type> isWellTypedIf(final IfStmt stmt,
											 final Map<Variable, Type> typeEnvironment,
											 final Classname classWeAreIn,
											 final Type functionReturnType) throws TypeErrorException {
		if (typeOf(stmt.guard, typeEnvironment, classWeAreIn) instanceof BooleanType) {
			isWellTypedStmt(stmt.trueBranch, typeEnvironment, classWeAreIn, functionReturnType);
			isWellTypedStmt(stmt.falseBranch, typeEnvironment, classWeAreIn, functionReturnType);
			return typeEnvironment;
		} else {
			throw new TypeErrorException("guard of if is not a boolean: " + stmt);
		}
	}	// isWellTypedIf
	
	
	// helper method for isWellTypedStmt
	// return exp;
	public Map<Variable, Type> isWellTypedReturn(final ReturnStmt stmt,
												 final Map<Variable, Type> typeEnvironment,
												 final Classname classWeAreIn,
												 final Type functionReturnType) throws TypeErrorException {
		if (functionReturnType == null) {
			throw new TypeErrorException("return in program entry point");
		} else {
			final Type receivedType = typeOf(stmt.exp, typeEnvironment, classWeAreIn);
			isEqualOrSubtypeOf(receivedType, functionReturnType);
			return typeEnvironment;
		}
	}
	
	
	// helper method for isWellTypedStmt
	// {stmt*}
	public Map<Variable, Type> isWellTypedBlock(final BlockStmt stmt,
												Map<Variable, Type> typeEnvironment,
												final Classname classWeAreIn,
												final Type functionReturnType) throws TypeErrorException {
		for (final Stmt bodyStmt : stmt.stmts) {
			typeEnvironment = isWellTypedStmt(bodyStmt, typeEnvironment, classWeAreIn, functionReturnType);
		}
		return typeEnvironment;
	}
	
	
	// helper method for isWellTypedStmt
	// println(exp*);
	public Map<Variable, Type> isWellTypedPrint(final PrintlnStmt stmt,
												final Map<Variable, Type> typeEnvironment,
												final Classname classWeAreIn) throws TypeErrorException {
		for (final Exp printExp : stmt.exps) {
			typeOf(printExp, typeEnvironment, classWeAreIn);
		}
		return typeEnvironment;
	}


	// Staments
	//	vardec |
	//	var = exp; |
	//	while (exp)  stmt |
	//	break; |					//still needs to be done
	//	if (exp) stmt else stmt |
	//	return exp; |
	//	{stmt*}
	//	println(exp*); |			//still needs to be done
	//	super(var); |				//still needs to be done
	//	this.var = var; |			//still needs to be done
	//	exp;
	public Map<Variable, Type> isWellTypedStmt(final Stmt stmt,
											   final Map<Variable, Type> typeEnvironment,
											   final Classname classWeAreIn,
											   final Type functionReturnType) throws TypeErrorException {
		if (stmt instanceof VariableDeclaration) {
			return isWellTypedVar((VariableDeclaration)stmt, typeEnvironment, classWeAreIn);
		} else if (stmt instanceof VariableValueChange) {
			return isWellTypedValueChange((VariableValueChange)stmt, typeEnvironment, classWeAreIn);
		} else if (stmt instanceof WhileStmt) {
			return isWellTypedWhile((WhileStmt)stmt, typeEnvironment, classWeAreIn, functionReturnType);
		} else if (stmt instanceof IfStmt) {
			return isWellTypedIf((IfStmt)stmt, typeEnvironment, classWeAreIn, functionReturnType);
		} else if(stmt instanceof ReturnStmt) {
			return isWellTypedReturn((ReturnStmt)stmt, typeEnvironment, classWeAreIn, functionReturnType);
		} else if(stmt instanceof BlockStmt) {
			return isWellTypedBlock((BlockStmt)stmt, typeEnvironment, classWeAreIn, functionReturnType);
		} else if(stmt instanceof PrintlnStmt) {
			return isWellTypedPrint((PrintlnStmt)stmt, typeEnvironment, classWeAreIn);
		} else if (stmt instanceof ExpStmt) {
			typeOf(((ExpStmt)stmt).exp, typeEnvironment, classWeAreIn);
			return typeEnvironment;
		} else {
			throw new TypeErrorException("Unsupported statement: " + stmt);
		}
	}	// isWellTypedStmt

}