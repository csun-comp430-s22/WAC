package WACtypechecker;

import WACparser.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;

public class Typechecker {

	public static final String BASE_CLASS_NAME = "Object";
	public final Map<Classname, ClassDefinition> classes;
	// includes inherited methods
	public final Map<Classname, DuplicateMap<Methodname, MethodDefinition>> methods;
	public final Program program;

	// throws an exception if the class doesn't exist
	// returns null is it's Object
	public static ClassDefinition getClass(final Classname className, final Map<Classname, ClassDefinition> classes)
			throws TypeErrorException {
		if (className.name.equals(BASE_CLASS_NAME)) {
			return null;
		} else {
			final ClassDefinition classDef = classes.get(className);
			if (classDef == null) {
				throw new TypeErrorException("no such class: " + className);
			} else {
				return classDef;
			}
		}
	}

	public ClassDefinition getClass(final Classname className) throws TypeErrorException {
		return getClass(className, classes);
	}

	public static ClassDefinition getParent(final Classname className, final Map<Classname, ClassDefinition> classes)
			throws TypeErrorException {
		final ClassDefinition classDef = getClass(className, classes);
		return getClass(classDef.extendsClassname, classes);
	}

	public ClassDefinition getParent(final Classname className) throws TypeErrorException {
		return getParent(className, classes);
	}

	public static void assertInheritanceNonCyclicalForClass(final ClassDefinition classDef,
			final Map<Classname, ClassDefinition> classes) throws TypeErrorException {
		final Set<Classname> seenClasses = new HashSet<Classname>();
		seenClasses.add(classDef.classname);
		ClassDefinition parentClassDef = getParent(classDef.classname, classes);
		while (parentClassDef != null) {
			final Classname parentClassName = parentClassDef.classname;
			if (seenClasses.contains(parentClassName)) {
				throw new TypeErrorException("Cyclic inheritance involving: " + parentClassName);
			}
			seenClasses.add(parentClassName);
			parentClassDef = getParent(parentClassName, classes);
		}
	}

	public static void assertInheritanceNonCyclical(final Map<Classname, ClassDefinition> classes)
			throws TypeErrorException {
		for (final ClassDefinition classDef : classes.values()) {
			assertInheritanceNonCyclicalForClass(classDef, classes);
		}
	}

	// includes inherited methods
	// allows method overloading with diff # of params
	// but currently not same # of params with diff types
	public static DuplicateMap<Methodname, MethodDefinition> methodsForClass(final Classname className,
			final Map<Classname, ClassDefinition> classes) throws TypeErrorException {
		final ClassDefinition classDef = getClass(className, classes);
		if (classDef == null) {
			return new DuplicateMap<Methodname, MethodDefinition>();
		} else {
			final DuplicateMap<Methodname, MethodDefinition> retval = methodsForClass(classDef.extendsClassname,
					classes);
			final Map<Methodname, Integer> methodsOnThisClass = new HashMap<Methodname, Integer>();
			for (final MethodDefinition methodDef : classDef.methoddefs) {
				final Methodname methodName = methodDef.methodname;
				if (methodsOnThisClass.containsKey(methodName)
						&& methodsOnThisClass.containsValue(methodDef.params.size())) {
					throw new TypeErrorException("duplicate method: " + methodName);
				}
				methodsOnThisClass.put(methodName, methodDef.params.size());
				retval.put(methodName, methodDef);
			}
			return retval;
		}
	}

	public static Map<Classname, DuplicateMap<Methodname, MethodDefinition>> makeMethodMap(
			final Map<Classname, ClassDefinition> classes) throws TypeErrorException {
		final Map<Classname, DuplicateMap<Methodname, MethodDefinition>> retval = new HashMap<Classname, DuplicateMap<Methodname, MethodDefinition>>();
		for (final Classname className : classes.keySet()) {
			retval.put(className, methodsForClass(className, classes));
		}
		return retval;
	}

	// also makes sure inheritance hierarchies aren't cyclical
	public static Map<Classname, ClassDefinition> makeClassMap(final List<ClassDefinition> classes)
			throws TypeErrorException {
		final Map<Classname, ClassDefinition> retval = new HashMap<Classname, ClassDefinition>();
		for (final ClassDefinition classDef : classes) {
			final Classname className = classDef.classname;
			if (retval.containsKey(classDef.classname)) {
				throw new TypeErrorException("Duplicate class name: " + className);
			} else {
				retval.put(className, classDef);
			}
		}
		assertInheritanceNonCyclical(retval);
		return retval;
	}

	public Typechecker(final Program program) throws TypeErrorException {
		this.program = program;
		classes = makeClassMap(program.classes);
		methods = makeMethodMap(classes);
	}

	public static Type typeOfVariable(final VariableExp exp, final Map<Variable, Type> typeEnvironment)
			throws TypeErrorException {
		final Type mapType = typeEnvironment.get(exp.variable);
		if (mapType == null) {
			throw new TypeErrorException("Used variable not in scope: " + exp.variable.name);
		} else {
			return mapType;
		}
	}

	public Type typeOfOp(final OpExp exp, final Map<Variable, Type> typeEnvironment, final Classname classWeAreIn)
			throws TypeErrorException {
		final Type leftType = typeOf(exp.left, typeEnvironment, classWeAreIn);
		final Type rightType = typeOf(exp.right, typeEnvironment, classWeAreIn);
		if ((exp.op instanceof MultiplicationOp) || (exp.op instanceof DivisionOp) || (exp.op instanceof PlusOp)
				|| (exp.op instanceof MinusOp)) {
			if (leftType instanceof IntType && rightType instanceof IntType) {
				return new IntType();
			} else {
				throw new TypeErrorException("Only integer operands allowed for arithmetic operations. Given: "
						+ leftType + " and " + rightType);
			}
		} else if ((exp.op instanceof LessThanOp) || (exp.op instanceof GreaterThanOp)
				|| (exp.op instanceof EqualEqualsOp) || (exp.op instanceof NotEqualsOp)) {
			if (leftType instanceof IntType && rightType instanceof IntType) {
				return new BooleanType();
			} else {
				throw new TypeErrorException("Only integer operands allowed for comparison operations. Given: "
						+ leftType + " and " + rightType);
			}
		} else {
			throw new TypeErrorException(
					"Since this already exhaustively checks for every single Op we have in our language this code is UNREACHABLE, therefore UNTESTABLE."
							+ "but Java doesn't understand this and thinks we still need an else. Ruining my code coverage >:(");
		}
	}

	public MethodDefinition getMethodDef(final Classname className, final Methodname methodName, final int numOfParams)
			throws TypeErrorException {
		final DuplicateMap<Methodname, MethodDefinition> methodMap = methods.get(className);
		if (methodMap == null) {
			throw new TypeErrorException("Unknown class name: " + className);
		} else {
			List<MethodDefinition> defs = methodMap.get(methodName);
			if (defs == null) {
				throw new TypeErrorException("Unknown method name: " + methodName + " for class " + className + " with "
						+ numOfParams + " params");
			}
			for (final MethodDefinition def : defs) {
				if (def.params.size() == numOfParams) {
					return def;
				}
			}
			throw new TypeErrorException("Unknown method name: " + methodName + " for class " + className + " with "
					+ numOfParams + " params");
		}
	}

	// helper method for typeOfMethodCall
	public Type expectedReturnTypeForClassAndMethod(final Classname className, final Methodname methodName,
			final int numOfParams)
			throws TypeErrorException {
		return getMethodDef(className, methodName, numOfParams).type;
	}

	// helper method for typeOfMethodCall
	public List<Type> expectedParameterTypesForClassAndMethod(final Classname className, final Methodname methodName,
			final int numOfParams) throws TypeErrorException {
		final MethodDefinition methodDef = getMethodDef(className, methodName, numOfParams);
		final List<Type> retval = new ArrayList<Type>();
		for (final Parameter param : methodDef.params) {
			retval.add(param.parameterType);
		}
		return retval;
	}

	public void assertEqualOrSubtypeOf(final Type first, final Type second) throws TypeErrorException {
		if (first.equals(second)) {
			return;
		} else if (first instanceof ClassnameType && second instanceof ClassnameType) {
			final ClassDefinition parentClassDef = getParent(((ClassnameType) first).classname);
			assertEqualOrSubtypeOf(new ClassnameType(parentClassDef.classname), second);
		} else {
			throw new TypeErrorException("incompatible types: " + first + ", " + second);
		}
	}

	// var.methodname(primary_exp*) in grammar
	// varName.methodName(inParens) in VarMethodCall.java
	// 1) varName should be a class
	// 2) varName needs to have the methodName method
	// 3) need to know the expected parameter types for the method
	public Type typeOfMethodCall(final VarMethodCall exp, final Map<Variable, Type> typeEnvironment,
			final Classname classWeAreIn) throws TypeErrorException {
		final Type varNameType = typeOf(exp.varName, typeEnvironment, classWeAreIn);
		if (varNameType instanceof ClassnameType) {
			final Classname className = ((ClassnameType) varNameType).classname;
			// final List<Type> expectedTypes =
			// expectedParameterTypesForClassAndMethod(className, exp.methodName.name);
			final List<Type> expectedTypes = expectedParameterTypesForClassAndMethod(className, exp.methodName.name,
					exp.inParens.size());
			expressionsOk(expectedTypes, exp.inParens, typeEnvironment, classWeAreIn);
			return expectedReturnTypeForClassAndMethod(className, exp.methodName.name, exp.inParens.size());
		} else {
			throw new TypeErrorException("Called method on non-class type: " + varNameType);
		}
	}

	public void expressionsOk(final List<Type> expectedTypes, final List<Exp> receivedExpressions,
			final Map<Variable, Type> typeEnvironment, final Classname classWeAreIn) throws TypeErrorException {
		if (expectedTypes.size() != receivedExpressions.size()) {
			throw new TypeErrorException("Wrong number of parameters for call: ");
		}
		for (int index = 0; index < expectedTypes.size(); index++) {
			final Type paramType = typeOf(receivedExpressions.get(index), typeEnvironment, classWeAreIn);
			final Type expectedType = expectedTypes.get(index);
			assertEqualOrSubtypeOf(paramType, expectedType);
		}
	}

	// helper method for typeOfNew
	public List<Type> expectedConstructorTypesForClass(final Classname className) throws TypeErrorException {
		final ClassDefinition classDef = getClass(className);
		final List<Type> retval = new ArrayList<Type>();
		if (classDef == null) {
			return retval;
		} else {
			for (final Parameter param : classDef.parameters) {
				retval.add(param.parameterType);
			}
			return retval;
		}
	}

	// new classname(exp*) in grammar
	// new className(inParens) in NewClassExp.java
	public Type typeOfNew(final NewClassExp exp, final Map<Variable, Type> typeEnvironment,
			final Classname classWeAreIn) throws TypeErrorException {
		// need to know what the constructor arguments for this class are
		final List<Type> expectedTypes = expectedConstructorTypesForClass(exp.className.classname);
		expressionsOk(expectedTypes, exp.inParens, typeEnvironment, classWeAreIn);
		return new ClassnameType(exp.className.classname);
	}

	// classWeAreIn is null if we are in (one of the entry points?) the entry point.
	public Type typeOf(final Exp exp, final Map<Variable, Type> typeEnvironment, final Classname classWeAreIn)
			throws TypeErrorException {
		if (exp instanceof TrueExp) {
			return new BooleanType();
		} else if (exp instanceof FalseExp) {
			return new BooleanType();
		} else if (exp instanceof IntegerExp) {
			return new IntType();
		} else if (exp instanceof StrExp) {
			return new StringType();
		} else if (exp instanceof ClassnameExp) {
			return new ClassnameType(((ClassnameExp) exp).classname);
		} else if (exp instanceof VariableExp) {
			return typeOfVariable((VariableExp) exp, typeEnvironment);
		} else if (exp instanceof OpExp) {
			return typeOfOp((OpExp) exp, typeEnvironment, classWeAreIn);
		} else if (exp instanceof VarMethodCall) {
			return typeOfMethodCall((VarMethodCall) exp, typeEnvironment, classWeAreIn);
		} else if (exp instanceof NewClassExp) {
			return typeOfNew((NewClassExp) exp, typeEnvironment, classWeAreIn);
		} else {
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
	} // addToMap

	// vardec
	public Map<Variable, Type> isWellTypedVar(final VariableDeclaration stmt,
			final Map<Variable, Type> typeEnvironment,
			final Classname classWeAreIn) throws TypeErrorException {
		final Type expType = typeOf(stmt.value, typeEnvironment, classWeAreIn);
		assertEqualOrSubtypeOf(expType, stmt.type);
		// return addToMap(typeEnvironment, (Variable) stmt.variable, stmt.type);
		// if above doesn't work I think this might work:
		return addToMap(typeEnvironment, ((VariableExp) stmt.variable).variable, stmt.type);
	} // isWellTypedVar

	// var = exp;
	public Map<Variable, Type> isWellTypedValueChange(final VariableValueChange stmt,
			final Map<Variable, Type> typeEnvironment,
			final Classname classWeAreIn) throws TypeErrorException {
		final Type varType = typeOf(stmt.variable, typeEnvironment, classWeAreIn);
		final Type expType = typeOf(stmt.exp, typeEnvironment, classWeAreIn);
		assertEqualOrSubtypeOf(expType, varType);
		return typeEnvironment; // correct since we are just changing the value of that variable but the type
								// remains the same
	} // isWellTypedValueChange

	// while (exp) stmt
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
	} // isWellTypedWhile

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
	} // isWellTypedIf

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
			assertEqualOrSubtypeOf(receivedType, functionReturnType);
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

	// helper method for super(var): For super's parameter types and var type
	// comparison match result.
	public boolean isWellTypedSuperParametersToVarType(ClassDefinition superclass, Exp variable,
			Map<Variable, Type> typeEnviornment, Classname classWeAreIn) throws TypeErrorException {
		Type varType = typeOf(variable, typeEnviornment, classWeAreIn);
		boolean typeIsAMatch = false;
		for (Parameter parameterType : superclass.parameters) {
			if (parameterType.parameterType.equals(varType)) {
				typeIsAMatch = true;
			}
		}

		return typeIsAMatch;
	}

	// super(var);
	public Map<Variable, Type> isWellTypedSuper(final SuperStmt stmt,
			final Map<Variable, Type> typeEnviornment,
			final Classname classWeAreIn,
			final Type functionReturnType) throws TypeErrorException {
		boolean hasSuper = false;
		ClassDefinition superClass = null;
		// Use the class map to check if the parent of the class exists
		if (getParent(classWeAreIn, classes) != null) {
			superClass = getParent(classWeAreIn, classes);
			hasSuper = true;
		}
		if ((hasSuper)
				&& (isWellTypedSuperParametersToVarType(superClass, stmt.variable, typeEnviornment, classWeAreIn))) {
			return typeEnviornment;
		} else {
			throw new TypeErrorException("Class " + classWeAreIn.name
					+ " does not have a parent class or var does match parameters in Type");
		}
	}

	// cesar's method for this.var=var;
	// helper method for isWellTypedStmt
	public Map<Variable, Type> isWellTypedThis(final ThisStmt var, final Map<Variable, Type> typeEnvironment,
			final Classname classWeAreIn, final Type ReturnType) throws TypeErrorException {
		if ((typeEnvironment.containsKey(var.ThisVar.variable)) && (typeEnvironment.containsKey(var.Var.variable))) {
			Type LeftSideType = typeEnvironment.get(var.ThisVar.variable);
			Type RightSideType = typeEnvironment.get(var.Var.variable);
			if (LeftSideType.equals(RightSideType) == true) {
				return typeEnvironment;
			} else {
				throw new TypeErrorException("this.variable type does not match other variable type");
			}

		} else {
			throw new TypeErrorException("Variable used with ' this. ' does not exist in class");
		}

	}

	// vardec |
	// var = exp; |
	// while (exp) stmt |
	// break; | //still needs to be done ??? I don't think so. Doesn't need to check
	// anything
	// if (exp) stmt else stmt |
	// return exp; |
	// {stmt*} |
	// println(exp*); |
	// super(var); | //still needs to be fixed
	// this.var = var; |
	// exp;
	public Map<Variable, Type> isWellTypedStmt(final Stmt stmt,
			final Map<Variable, Type> typeEnvironment,
			final Classname classWeAreIn,
			final Type functionReturnType) throws TypeErrorException {
		if (stmt instanceof VardecStmt) {
			final VardecStmt tempStmt = (VardecStmt) (stmt);
			final VariableDeclaration vDeclaration = tempStmt.variableDec.result;
			return isWellTypedVar(vDeclaration, typeEnvironment, classWeAreIn);
		} else if (stmt instanceof VariableValueChange) {
			return isWellTypedValueChange((VariableValueChange) stmt, typeEnvironment, classWeAreIn);
		} else if (stmt instanceof WhileStmt) {
			return isWellTypedWhile((WhileStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
		} else if (stmt instanceof IfStmt) {
			return isWellTypedIf((IfStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
		} else if (stmt instanceof ReturnStmt) {
			return isWellTypedReturn((ReturnStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
		} else if (stmt instanceof BlockStmt) {
			return isWellTypedBlock((BlockStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
		} else if (stmt instanceof PrintlnStmt) {
			return isWellTypedPrint((PrintlnStmt) stmt, typeEnvironment, classWeAreIn);
		} else if (stmt instanceof ExpStmt) {
			typeOf(((ExpStmt) stmt).exp, typeEnvironment, classWeAreIn);
			return typeEnvironment;
		} else if (stmt instanceof SuperStmt) {
			return isWellTypedSuper((SuperStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
		} else if (stmt instanceof ThisStmt) {
			return isWellTypedThis((ThisStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
		} else {
			throw new TypeErrorException("Unsupported statement: " + stmt);
		}
	} // isWellTypedStmt

	// type methodname(param*) stmt
	public void isWellTypedMethodDef(final MethodDefinition method,
			Map<Variable, Type> typeEnvironment,
			final Classname classWeAreIn) throws TypeErrorException {
		final Set<Variable> variablesInMethod = new HashSet<Variable>();
		for (final Parameter param : method.params) {
			final VariableExp variableExp = ((VariableExp) param.variable);
			final Variable variable = variableExp.variable;
			if (variablesInMethod.contains(variable)) {
				throw new TypeErrorException("Duplicate variable in method definition: " + variable);
			}
			variablesInMethod.add(variable);
			// odd semantics: last variable declaration shadows prior one
			typeEnvironment = addToMap(typeEnvironment, ((VariableExp) param.variable).variable, param.parameterType);
		}
		isWellTypedStmt(method.stmt, typeEnvironment, classWeAreIn, method.type);
	}

	// puts all instance variable in scope for the class
	// includes parent classes
	// throws exception if there are any duplicate names in the chain
	public Map<Variable, Type> baseTypeEnvironmentForClass(final Classname className) throws TypeErrorException {
		final ClassDefinition classDef = getClass(className);
		if (classDef == null) {
			return new HashMap<Variable, Type>();
		} else {
			final Map<Variable, Type> retval = baseTypeEnvironmentForClass(classDef.extendsClassname);
			for (final VariableDeclaration instanceVariable : classDef.classVariables) {
				final VariableExp variableExp = ((VariableExp) instanceVariable.variable);
				final Variable variable = variableExp.variable;
				// final Variable variable = (VariableExp(instanceVariable.variable)).variable;
				if (retval.containsKey(variable)) {
					throw new TypeErrorException("Duplicate instance variable (possible inherited): " + variable);
				}
				retval.put(variable, instanceVariable.type);
			}
			return retval;
		}
	}

	// checks constructor
	// checks methods
	// class classname extends classname {
	// vardec*
	// constructor(param*) stmt
	// methoddef*
	// }
	public void isWellTypedClassDef(final ClassDefinition classDef) throws TypeErrorException {
		final Map<Variable, Type> typeEnvironment = baseTypeEnvironmentForClass(classDef.classname);
		// check constructor
		Map<Variable, Type> constructorTypeEnvironment = typeEnvironment;
		final Set<Variable> variablesInConstructor = new HashSet<Variable>();
		for (final Parameter param : classDef.parameters) {
			final VariableExp variableExp = ((VariableExp) param.variable);
			final Variable variable = variableExp.variable;
			// final Variable variable = (VariableExp(param.variable)).variable;
			if (variablesInConstructor.contains(variable)) {
				throw new TypeErrorException("Duplicate variable in constructor param: " + variable);
			}
			variablesInConstructor.add(variable);
			constructorTypeEnvironment = addToMap(constructorTypeEnvironment, variable, param.parameterType);
		}
		// check body of constructor
		isWellTypedStmt(classDef.stmt, constructorTypeEnvironment, classDef.classname, null); // i put null because he
																								// used void but we
																								// don't have void idk
		// check methods
		for (final MethodDefinition method : classDef.methoddefs) {
			isWellTypedMethodDef(method, typeEnvironment, classDef.classname);
		}
	}

	// classdef* stmt*
	public void isWellTypedProgram() throws TypeErrorException {
		for (final ClassDefinition classDef : program.classes) {
			isWellTypedClassDef(classDef);
		}
		for (final Stmt stmt : program.stmts) {
			isWellTypedStmt(stmt, new HashMap<Variable, Type>(), null, null);
		}
	}

}