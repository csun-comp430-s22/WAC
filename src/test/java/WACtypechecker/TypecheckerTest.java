package WACtypechecker;

import WACparser.*;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TypecheckerTest {
	
	
	//tests getClass(2 params) method for normal circumstance
	@Test
	public void testGetClass2ParamsNormal() throws TypeErrorException {
		//takes a Classname and a Map<Classname, ClassDefinition>
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final ClassDefinition expected = new ClassDefinition(new Classname("Dog"), new Classname("Animal"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		map.put(classname, expected);
		final ClassDefinition received = typechecker.getClass(classname, map);
		assertEquals(expected, received);
	}
	
	//tests getClass(2 params) method for base class
	@Test
	public void testGetClass2ParamsBaseClass() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final ClassDefinition expected = new ClassDefinition(new Classname("Object"), new Classname(""), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Object");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		map.put(classname, expected);
		final ClassDefinition received = typechecker.getClass(classname, map);
		assertEquals(null, received);
	}
	
	//tests getClass(2 params) method for class not in map
	@Test (expected = TypeErrorException.class)
	public void testGetClass2ParamsClassNotInMap() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname classname = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final ClassDefinition received = typechecker.getClass(classname, map);
	}
	
	//tests getClass(1 param) method
	@Test
	public void testGetClass1Param() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		typechecker.classes.put(classname, classDef);
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		map.put(classname, classDef);
		final ClassDefinition received = typechecker.getClass(classname);
		assertEquals(classDef, received);
	}
	
	//tests getParent(1 param) method
	@Test
	public void testGetParent1Param() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Animal"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		typechecker.classes.put(classname, classDef);
		final Classname parentClassname = new Classname("Animal");
		final ClassDefinition parentClassDef = new ClassDefinition(new Classname("Animal"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		typechecker.classes.put(parentClassname, parentClassDef);
		final ClassDefinition received = typechecker.getParent(classname);
		assertEquals(parentClassDef, received);
	}
	
	//tests assertInheritanceNonCyclicalForClass method for one level inheritance
	//doesn't have an assert because we are just testing that it doesn't throw an exception
	@Test
	public void testAssertInheritanceNonCyclicalForClassOneLevelInheritance() throws TypeErrorException {
		//takes in a ClassDefinition, Map<Classname, ClassDefinition>
		//returns nothing
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Animal"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		typechecker.classes.put(classname, classDef);
		map.put(classname, classDef);
		final Classname parentClassname = new Classname("Animal");
		final ClassDefinition parentClassDef = new ClassDefinition(new Classname("Animal"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		typechecker.classes.put(parentClassname, parentClassDef);
		map.put(parentClassname, parentClassDef);
		typechecker.assertInheritanceNonCyclicalForClass(classDef, map);
	}
	
	//tests assertInheritanceNonCyclicalForClass for cyclical inheritance
	//expects an exception to be thrown
	@Test (expected = TypeErrorException.class)
	public void testAssertInheritanceNonCyclicalForClassCyclicalCase() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Animal"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		typechecker.classes.put(classname, classDef);
		map.put(classname, classDef);
		final Classname parentClassname = new Classname("Animal");
		final ClassDefinition parentClassDef = new ClassDefinition(new Classname("Animal"), new Classname("Dog"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		typechecker.classes.put(parentClassname, parentClassDef);
		map.put(parentClassname, parentClassDef);
		typechecker.assertInheritanceNonCyclicalForClass(classDef, map);
	}
	
	//tests methodsForClass method for class with one method
	@Test
	public void testMethodsForClassWithOneMethod() throws TypeErrorException {
		//takes in Classname and Map<Classname, ClassDefinition>
		//returns Map<Methodname, MethodDefinition>
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams, new ExpStmt(new IntegerExp(0)));
		final List<MethodDefinition> methodDefs = new ArrayList<MethodDefinition>();
		methodDefs.add(methodDef);
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), methodDefs);
		map.put(className, classDef);
		//now to make expected output:
		final Map<Methodname, MethodDefinition> expected = new HashMap<Methodname, MethodDefinition>();
		expected.put(methodDef.methodname, methodDef);
		//now to actually test
		final Map<Methodname, MethodDefinition> received = typechecker.methodsForClass(className, map);
		assertEquals(expected, received);
	}
	
	//tests methodsForClass method for class with two methods that have same name but diff num of params
	@Test
	public void testMethodsForClassWithTwoMethodsSameNameDiffNumOfParams() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		final List<Parameter> methodParams2 = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		methodParams2.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		methodParams2.add(new Parameter(new IntType(), new VariableExp(new Variable("y"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams, new ExpStmt(new IntegerExp(0)));
		final MethodDefinition methodDef2 = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams2, new ExpStmt(new IntegerExp(0)));
		final List<MethodDefinition> methodDefs = new ArrayList<MethodDefinition>();
		methodDefs.add(methodDef);
		methodDefs.add(methodDef2);
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), methodDefs);
		map.put(className, classDef);
		//now to make expected output:
		final Map<Methodname, MethodDefinition> expected = new HashMap<Methodname, MethodDefinition>();
		expected.put(methodDef.methodname, methodDef);
		expected.put(methodDef2.methodname, methodDef2);
		//now to actually test
		final Map<Methodname, MethodDefinition> received = typechecker.methodsForClass(className, map);
		assertEquals(expected, received);
	}
	
	//tests methodsForClass method for class with two methods that have same name and same num of params
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testMethodsForClassWithTwoMethodsSameNameAndSameNumOfParams() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		final List<Parameter> methodParams2 = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		methodParams2.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams, new ExpStmt(new IntegerExp(0)));
		final MethodDefinition methodDef2 = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams2, new ExpStmt(new IntegerExp(0)));
		final List<MethodDefinition> methodDefs = new ArrayList<MethodDefinition>();
		methodDefs.add(methodDef);
		methodDefs.add(methodDef2);
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), methodDefs);
		map.put(className, classDef);
		typechecker.methodsForClass(className, map);
	}
	
	//tests makeMethodMap method for class with one method
	@Test
	public void testMakeMethodMapOneClassWithOneMethod() throws TypeErrorException {
	//takes in Map<Classname, ClassDefinition>
	//returns Map<Classname, Map<Methodname, MethodDefinition>>
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams, new ExpStmt(new IntegerExp(0)));
		final List<MethodDefinition> methodDefs = new ArrayList<MethodDefinition>();
		methodDefs.add(methodDef);
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), methodDefs);
		map.put(className, classDef);
		//now to make the expected value
		final Map<Classname, Map<Methodname, MethodDefinition>> expected = new HashMap<Classname, Map<Methodname, MethodDefinition>>();
		final Map<Methodname, MethodDefinition> map2 = new HashMap<Methodname, MethodDefinition>();
		map2.put(new Methodname("findNum"), methodDef);
		expected.put(className, map2);
		//now to actually test
		final Map<Classname, Map<Methodname, MethodDefinition>> received = typechecker.makeMethodMap(map);
		assertEquals(expected, received);
	}
	
	//tests makeClassMap method for normal circumstance
	@Test
	public void testMakeClassMapNormal() throws TypeErrorException {
		//takes a List<ClassDefinition>, returns a Map<Classname, ClassDefinition>
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final List<ClassDefinition> classDefs = new ArrayList<ClassDefinition>();
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		classDefs.add(classDef);
		final Classname classname = new Classname("Dog");
		final Map<Classname, ClassDefinition> expected = new HashMap<Classname, ClassDefinition>();
		expected.put(classname, classDef);
		final Map<Classname, ClassDefinition> received = typechecker.makeClassMap(classDefs);
		assertEquals(expected, received);
	}
	
	//tests makeClassMap method for duplicate class names
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testMakeClassMapForDuplicateClassNames() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final List<ClassDefinition> classDefs = new ArrayList<ClassDefinition>();
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		final ClassDefinition classDef2 = new ClassDefinition(new Classname("Dog"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		classDefs.add(classDef);
		classDefs.add(classDef2);
		typechecker.makeClassMap(classDefs);
	}
	
	//tests typeOfVariable method for variable in scope
	@Test
	public void testVariableInScope() throws TypeErrorException {
 		final ExpStmt expStmt = new ExpStmt(new IntegerExp(0));
		final List<Stmt> stmts = new ArrayList<Stmt>();
		stmts.add(expStmt);
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), stmts));
		final Type expectedType = new IntType();
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("x"), new IntType());
		final Type receivedType = typechecker.typeOfVariable(new VariableExp(new Variable("x")), typeEnvironment);
		assertEquals(expectedType, receivedType);
	}
	
	
	//test typeOfVariable method for variable out of scope
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testVariableOutOfScope() throws TypeErrorException {
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		// x is not in the typeEnvironment
		typechecker.typeOfVariable(new VariableExp(new Variable("x")), typeEnvironment);
	}
	
	//test typeOfOp method for MultiplicationOp
	@Test
	public void testTypeOfOpForMultiplicationOp() throws TypeErrorException {
		//takes in: OpExp, Map<Variable, Type>, Classname
		//returns: Type
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(0), new MultiplicationOp(), new IntegerExp(1));
		final Type expected = new IntType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
		assertEquals(expected, received);
	}
	
	//test typeOfOp method for DivisionOp
	@Test
	public void testTypeOfOpForDivisionOp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(2), new DivisionOp(), new IntegerExp(1));
		final Type expected = new IntType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
		assertEquals(expected, received);
	}
	
	//test typeOfOp method for PlusOp
	@Test
	public void testTypeOfOpForPlusOp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(2), new PlusOp(), new IntegerExp(1));
		final Type expected = new IntType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
		assertEquals(expected, received);
	}
	
	//test typeOfOp method for MinusOp
	@Test
	public void testTypeOfOpForMinusOp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(2), new MinusOp(), new IntegerExp(1));
		final Type expected = new IntType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
		assertEquals(expected, received);
	}
	
	//test typeOfOp method for Incorrect Types for IntType operation
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testTypeOfOpForMinusOpWithIncorrectTypesTwoNonInts() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new StrExp("boo"), new MinusOp(), new TrueExp());
		typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
	}
	
	//test typeOfOp method for one incorrect type for IntType operation
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testTypeOfOpForMinusOpWithIncorrectTypeOneNonInt() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(1), new MinusOp(), new TrueExp());
		typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
	}
	
	//test typeOfOp method for LessThanOp
	@Test
	public void testTypeOfOpForLessThanOp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(1), new LessThanOp(), new IntegerExp(1));
		final Type expected = new BooleanType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
		assertEquals(expected, received);
	}
	
	//test typeOfOp method for GreaterThanOp
	@Test
	public void testTypeOfOpForGreaterThanOp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(1), new GreaterThanOp(), new IntegerExp(1));
		final Type expected = new BooleanType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
		assertEquals(expected, received);
	}
	
	//test typeOfOp method for EqualEqualsOp
	@Test
	public void testTypeOfOpForEqualEqualsOp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(1), new EqualEqualsOp(), new IntegerExp(1));
		final Type expected = new BooleanType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
		assertEquals(expected, received);
	}
	
	//test typeOfOp method for NotEqualsOp
	@Test
	public void testTypeOfOpForNotEqualsOp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(1), new NotEqualsOp(), new IntegerExp(1));
		final Type expected = new BooleanType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
		assertEquals(expected, received);
	}
	
	//test typeOfOp method for one incorrect type for BooleanType operation
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testTypeOfOpForNotEqualsOpWithIncorrectTypeOneNonInt() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(1), new NotEqualsOp(), new TrueExp());
		typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
	}
	
	//test typeOfOp method for incorrect types for BooleanType operation both non int
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testTypeOfOpForNotEqualsOpWithIncorrectTypesTwoNonInts() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new StrExp("hi"), new NotEqualsOp(), new TrueExp());
		typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
	}
	
	//tests assertEqualOrSubtypeOf method for the same two types
	//void method so we are just making sure no exception is thrown
	@Test
	public void testAssertEqualsOrSubtypeOfTwoSameTypes() throws TypeErrorException {
		//takes in: Type, Type
		//returns: nothing
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		typechecker.assertEqualOrSubtypeOf(new IntType(), new IntType());
	}
	
	//tests assertEqualOrSubtypeOf method for first is subtype of second
	//void method so we are just making sure no exception is thrown
	@Test
	public void testAssertEqualsOrSubtypeOfWhereFirstIsSubtypeOfSecond() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Animal"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		typechecker.classes.put(classname, classDef);
		final Classname parentClassname = new Classname("Animal");
		final ClassDefinition parentClassDef = new ClassDefinition(new Classname("Animal"), new Classname("Object"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		typechecker.classes.put(parentClassname, parentClassDef);
		typechecker.assertEqualOrSubtypeOf(new ClassnameType(classname), new ClassnameType(parentClassname));
	}
	
	//tests assertEqualOrSubtypeOf method for incompatible types: one ClassnameType, one not
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testAssertEqualsOrSubtypeOfIncompatibleTypesOneClassnameTypeOneNot() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		typechecker.assertEqualOrSubtypeOf(new ClassnameType(new Classname("doesn't matter")), new IntType());
	}
	
	//tests assertEqualOrSubtypeOf method for incompatible types: neither are ClassnameTypes
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testAssertEqualsOrSubtypeOfIncompatibleTypesNeitherClassnameTypes() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		typechecker.assertEqualOrSubtypeOf(new IntType(), new StringType());
	}
	
/* 	//tests typeOfMethodCall
	@Test
	public void testTypeOfMethodCall() throws TypeErrorException {
		//takes in: VarMethodCall, Map<Variable, Type>, Classname
		//returns: Type
		final VarMethodCall exp = new VarMethodCall(new ClassnameExp(new Classname("Dog")), new MethodNameExp(new Methodname("find")), new ArrayList<Exp>());
		//come back to this becasue need to test expectedReturnTypeForClassAndMethod first
	} */
	
	//tests expressionsOk for an unequal length of lists of params
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testExpressionsOkUnEqualLengthOfParams() throws TypeErrorException {
		//takes in: List<Type>, List<Exp>, Map<Variable, Type>, Classname
		//returns: nothing
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final List<Type> expectedTypes = new ArrayList<Type>();
		final List<Exp> receivedExps = new ArrayList<Exp>();
		expectedTypes.add(new IntType());
		receivedExps.add(new IntegerExp(0));
		receivedExps.add(new TrueExp());
		typechecker.expressionsOk(expectedTypes, receivedExps, null, null);
	}
	
	//tests expressionsOk for one param of the same type
	//void method; not using assert; just making sure it doesn't throw any exceptions
	@Test
	public void testExpressionsOkOneParamSameType() throws TypeErrorException {
		//takes in: List<Type>, List<Exp>, Map<Variable, Type>, Classname
		//returns: nothing
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final List<Type> expectedTypes = new ArrayList<Type>();
		final List<Exp> receivedExps = new ArrayList<Exp>();
		expectedTypes.add(new IntType());
		receivedExps.add(new IntegerExp(0));
		typechecker.expressionsOk(expectedTypes, receivedExps, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
	}
	
	//tests expectedConstructorTypesForClass method for base class
	//should return an empty list
	@Test
	public void testExpectedConstructorTypesForClassForBaseClass() throws TypeErrorException {
		//takes in: Classname
		//returns: List<Type>
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("Object");
		final List<Type> expected = new ArrayList<Type>();
		final List<Type> received = typechecker.expectedConstructorTypesForClass(className);
		assertEquals(expected, received);
	}
	
	//tests expectedConstructorTypesForClass method for arbitrary class with one constructor param
	@Test
	public void testExpectedConstructorTypesForClassForArbitraryClassWithOneConstructorParam() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("Dog");
		final List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter(new StringType(), new VariableExp(new Variable("name"))));
		final ClassDefinition classDef = new ClassDefinition(className, new Classname("Object"), new ArrayList<VariableDeclaration>(), params, new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		typechecker.classes.put(className, classDef);
		final List<Type> expected = new ArrayList<Type>();
		expected.add(new StringType());
		final List<Type> received = typechecker.expectedConstructorTypesForClass(className);
		assertEquals(expected, received);
	}
	
	//test typeOf method for a True expression
	@Test
	public void testTypeOfForTrueExp() throws TypeErrorException {
		//takes in: Exp, Map<Variable, Type>, Classname
		//returns: Type
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Type expected = new BooleanType();
		final Type received = typechecker.typeOf(new TrueExp(), null, null);
		assertEquals(expected, received);
	}
	
	//test typeOf method for a False expression
	@Test
	public void testTypeOfForFalseExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Type expected = new BooleanType();
		final Type received = typechecker.typeOf(new FalseExp(), null, null);
		assertEquals(expected, received);
	}
	
	//test typeOf method for an Integer expression
	@Test
	public void testTypeOfForIntegerExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Type expected = new IntType();
		final Type received = typechecker.typeOf(new IntegerExp(0), null, null);
		assertEquals(expected, received);
	}
	
	//test typeOf method for a String expression
	@Test
	public void testTypeOfForStrExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Type expected = new StringType();
		final Type received = typechecker.typeOf(new StrExp("hi"), null, null);
		assertEquals(expected, received);
	}
	
	//test typeOf method for a Classname expression
	@Test
	public void testTypeOfForClassnameExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Type expected = new ClassnameType(new Classname("Dog"));
		final Type received = typechecker.typeOf(new ClassnameExp(new Classname("Dog")), null, null);
		assertEquals(expected, received);
	}
	
	//test typeOf method for a Variable expression
	@Test
	public void testTypeOfForVariableExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("x"), new IntType());
		final Type expected = new IntType();
		final Type received = typechecker.typeOf(new VariableExp(new Variable("x")), typeEnvironment, null);
		assertEquals(expected, received);
	}
	
	//test typeOf method for a OpExp expression
	@Test
	public void testTypeOfForOpExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("x"), new IntType());
		final Classname classname = new Classname("doesn't matter");
		final Type expected = new IntType();
		final Type received = typechecker.typeOf(new OpExp(new IntegerExp(0), new PlusOp(), new IntegerExp(1)), typeEnvironment, classname);
		assertEquals(expected, received);
	}
	
	//test isWellTypedThis method for statement this.var = var   
			@Test
			public void testStatementThis() throws TypeErrorException {
				final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
				final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
				typeEnvironment.put(new Variable("x"), new IntType());
				final Classname classname = new Classname("doesn't matter");
				final Map<Variable, Type> expected = typeEnvironment;
				final Map<Variable, Type> received = typechecker.isWellTypedThis(new ThisStmt(new VariableExp(new Variable("x")), new VariableExp(new Variable("x"))) , typeEnvironment, classname, null);
				assertEquals(expected, received);
			}
	
}