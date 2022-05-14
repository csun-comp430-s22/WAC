package WACtypechecker;

import WACparser.*;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.List;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TypecheckerTest {

	// tests getClass(2 params) method for normal circumstance
	@Test
	public void testGetClass2ParamsNormal() throws TypeErrorException {
		// takes a Classname and a Map<Classname, ClassDefinition>
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final ClassDefinition expected = new ClassDefinition(new Classname("Dog"), new Classname("Animal"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		map.put(classname, expected);
		final ClassDefinition received = typechecker.getClass(classname, map);
		assertEquals(expected, received);
	}

	// tests getClass(2 params) method for base class
	@Test
	public void testGetClass2ParamsBaseClass() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final ClassDefinition expected = new ClassDefinition(new Classname("Object"), new Classname(""),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Object");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		map.put(classname, expected);
		final ClassDefinition received = typechecker.getClass(classname, map);
		assertEquals(null, received);
	}

	// tests getClass(2 params) method for class not in map
	@Test(expected = TypeErrorException.class)
	public void testGetClass2ParamsClassNotInMap() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname classname = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final ClassDefinition received = typechecker.getClass(classname, map);
	}

	// tests getClass(1 param) method
	@Test
	public void testGetClass1Param() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		typechecker.classes.put(classname, classDef);
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		map.put(classname, classDef);
		final ClassDefinition received = typechecker.getClass(classname);
		assertEquals(classDef, received);
	}

	// tests getParent(1 param) method
	@Test
	public void testGetParent1Param() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Animal"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		typechecker.classes.put(classname, classDef);
		final Classname parentClassname = new Classname("Animal");
		final ClassDefinition parentClassDef = new ClassDefinition(new Classname("Animal"), new Classname("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		typechecker.classes.put(parentClassname, parentClassDef);
		final ClassDefinition received = typechecker.getParent(classname);
		assertEquals(parentClassDef, received);
	}

	// tests assertInheritanceNonCyclicalForClass method for one level inheritance
	// doesn't have an assert because we are just testing that it doesn't throw an
	// exception
	@Test
	public void testAssertInheritanceNonCyclicalForClassOneLevelInheritance() throws TypeErrorException {
		// takes in a ClassDefinition, Map<Classname, ClassDefinition>
		// returns nothing
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Animal"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		typechecker.classes.put(classname, classDef);
		map.put(classname, classDef);
		final Classname parentClassname = new Classname("Animal");
		final ClassDefinition parentClassDef = new ClassDefinition(new Classname("Animal"), new Classname("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		typechecker.classes.put(parentClassname, parentClassDef);
		map.put(parentClassname, parentClassDef);
		typechecker.assertInheritanceNonCyclicalForClass(classDef, map);
	}

	// tests assertInheritanceNonCyclicalForClass for cyclical inheritance
	// expects an exception to be thrown
	@Test(expected = TypeErrorException.class)
	public void testAssertInheritanceNonCyclicalForClassCyclicalCase() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Animal"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		final Classname classname = new Classname("Dog");
		typechecker.classes.put(classname, classDef);
		map.put(classname, classDef);
		final Classname parentClassname = new Classname("Animal");
		final ClassDefinition parentClassDef = new ClassDefinition(new Classname("Animal"), new Classname("Dog"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		typechecker.classes.put(parentClassname, parentClassDef);
		map.put(parentClassname, parentClassDef);
		typechecker.assertInheritanceNonCyclicalForClass(classDef, map);
	}

	// tests methodsForClass method for class with one method
	@Test
	public void testMethodsForClassWithOneMethod() throws TypeErrorException {
		// takes in Classname and Map<Classname, ClassDefinition>
		// returns DuplicateMap<Methodname, MethodDefinition>
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams,
				new ExpStmt(new IntegerExp(0)));
		final List<MethodDefinition> methodDefs = new ArrayList<MethodDefinition>();
		methodDefs.add(methodDef);
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				methodDefs);
		map.put(className, classDef);
		// now to make expected output:
		final DuplicateMap<Methodname, MethodDefinition> expected = new DuplicateMap<Methodname, MethodDefinition>();
		expected.put(methodDef.methodname, methodDef);
		// now to actually test
		final DuplicateMap<Methodname, MethodDefinition> received = typechecker.methodsForClass(className, map);
		assertEquals(expected, received);
	}

	// tests methodsForClass method for class with two methods that have same name
	// but diff num of params
	@Test
	public void testMethodsForClassWithTwoMethodsSameNameDiffNumOfParams() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		final List<Parameter> methodParams2 = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		methodParams2.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		methodParams2.add(new Parameter(new IntType(), new VariableExp(new Variable("y"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams,
				new ExpStmt(new IntegerExp(0)));
		final MethodDefinition methodDef2 = new MethodDefinition(new IntType(), new Methodname("findNum"),
				methodParams2, new ExpStmt(new IntegerExp(0)));
		final List<MethodDefinition> methodDefs = new ArrayList<MethodDefinition>();
		methodDefs.add(methodDef);
		methodDefs.add(methodDef2);
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				methodDefs);
		map.put(className, classDef);
		// now to make expected output:
		final DuplicateMap<Methodname, MethodDefinition> expected = new DuplicateMap<Methodname, MethodDefinition>();
		expected.put(methodDef.methodname, methodDef);
		expected.put(methodDef2.methodname, methodDef2);
		// now to actually test
		final DuplicateMap<Methodname, MethodDefinition> received = typechecker.methodsForClass(className, map);
		assertEquals(expected, received);
	}

	// tests methodsForClass method for class with two methods that have same name
	// and same num of params
	// expecting an exception
	@Test(expected = TypeErrorException.class)
	public void testMethodsForClassWithTwoMethodsSameNameAndSameNumOfParams() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		final List<Parameter> methodParams2 = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		methodParams2.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams,
				new ExpStmt(new IntegerExp(0)));
		final MethodDefinition methodDef2 = new MethodDefinition(new IntType(), new Methodname("findNum"),
				methodParams2, new ExpStmt(new IntegerExp(0)));
		final List<MethodDefinition> methodDefs = new ArrayList<MethodDefinition>();
		methodDefs.add(methodDef);
		methodDefs.add(methodDef2);
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				methodDefs);
		map.put(className, classDef);
		typechecker.methodsForClass(className, map);
	}

	// tests makeMethodMap method for class with one method
	@Test
	public void testMakeMethodMapOneClassWithOneMethod() throws TypeErrorException {
		// takes in Map<Classname, ClassDefinition>
		// returns Map<Classname, DuplicateMap<Methodname, MethodDefinition>>
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams,
				new ExpStmt(new IntegerExp(0)));
		final List<MethodDefinition> methodDefs = new ArrayList<MethodDefinition>();
		methodDefs.add(methodDef);
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				methodDefs);
		map.put(className, classDef);
		// now to make the expected value
		final Map<Classname, DuplicateMap<Methodname, MethodDefinition>> expected = new HashMap<Classname, DuplicateMap<Methodname, MethodDefinition>>();
		final DuplicateMap<Methodname, MethodDefinition> map2 = new DuplicateMap<Methodname, MethodDefinition>();
		map2.put(new Methodname("findNum"), methodDef);
		expected.put(className, map2);
		// now to actually test
		final Map<Classname, DuplicateMap<Methodname, MethodDefinition>> received = typechecker.makeMethodMap(map);
		assertEquals(expected, received);
	}

	// tests makeClassMap method for normal circumstance
	@Test
	public void testMakeClassMapNormal() throws TypeErrorException {
		// takes a List<ClassDefinition>, returns a Map<Classname, ClassDefinition>
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final List<ClassDefinition> classDefs = new ArrayList<ClassDefinition>();
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		classDefs.add(classDef);
		final Classname classname = new Classname("Dog");
		final Map<Classname, ClassDefinition> expected = new HashMap<Classname, ClassDefinition>();
		expected.put(classname, classDef);
		final Map<Classname, ClassDefinition> received = typechecker.makeClassMap(classDefs);
		assertEquals(expected, received);
	}

	// tests makeClassMap method for duplicate class names
	// expecting an exception
	@Test(expected = TypeErrorException.class)
	public void testMakeClassMapForDuplicateClassNames() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final List<ClassDefinition> classDefs = new ArrayList<ClassDefinition>();
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		final ClassDefinition classDef2 = new ClassDefinition(new Classname("Dog"), new Classname("Object"),
				new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)),
				new ArrayList<MethodDefinition>());
		classDefs.add(classDef);
		classDefs.add(classDef2);
		typechecker.makeClassMap(classDefs);
	}

	// tests typeOfVariable method for variable in scope
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

	// test typeOfVariable method for variable out of scope
	// expecting an exception
	@Test(expected = TypeErrorException.class)
	public void testVariableOutOfScope() throws TypeErrorException {
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		// x is not in the typeEnvironment
		typechecker.typeOfVariable(new VariableExp(new Variable("x")), typeEnvironment);
	}

	// test typeOfOp method for MultiplicationOp
	@Test
	public void testTypeOfOpForMultiplicationOp() throws TypeErrorException {
		// takes in: OpExp, Map<Variable, Type>, Classname
		// returns: Type
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(0), new MultiplicationOp(), new IntegerExp(1));
		final Type expected = new IntType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
		assertEquals(expected, received);
	}

	// test typeOfOp method for DivisionOp
	@Test
	public void testTypeOfOpForDivisionOp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final OpExp exp = new OpExp(new IntegerExp(2), new DivisionOp(), new IntegerExp(1));
		final Type expected = new IntType();
		final Type received = typechecker.typeOfOp(exp, new HashMap<Variable, Type>(), new Classname("doesn't matter"));
		assertEquals(expected, received);
	}

	// test typeOfOp method for PlusOp
	@Test
	public void testTypeOfOpForPlusOp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
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
	
	//tests getMethodDef for class with one method that matches the specified methodname and num of params
	@Test
	public void testGetMethodDefNormal() throws TypeErrorException {
		// Takes in Classname and Methodname and Int(num of params)
		// returns MethodDefinition
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("dog");
		final Methodname methodName = new Methodname("findNum");
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams,
				new ExpStmt(new IntegerExp(0)));
		final DuplicateMap<Methodname, MethodDefinition> methodsForClass = new DuplicateMap<Methodname, MethodDefinition>();
		methodsForClass.put(methodName, methodDef);
		typechecker.methods.put(className, methodsForClass);
		// Now we make the expected output
		final MethodDefinition expected = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams,
				new ExpStmt(new IntegerExp(0)));
		// Now we actually test
		final MethodDefinition received = typechecker.getMethodDef(className, methodName, methodParams.size());
		assertEquals(expected, received);
	}
	
	//tests getMethodDef for class that doesn't exist
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testGetMethodDefForClassDoesNotExist() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		typechecker.getMethodDef(new Classname("iDontExist"), null, 0);
	}
	
	//tests getMethodDef for class with two methods with the same name but diff num of params
	//should receive the second method with the specified name
	@Test
	public void testGetMethodDefMethodOverloading() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("dog");
		final Methodname methodName = new Methodname("findNum");
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), methodName, methodParams, new ExpStmt(new IntegerExp(0)));
		final DuplicateMap<Methodname, MethodDefinition> methodsForClass = new DuplicateMap<Methodname, MethodDefinition>();
		methodsForClass.put(methodName, methodDef);
		final List<Parameter> method2Params = new ArrayList<Parameter>();
		method2Params.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		method2Params.add(new Parameter(new BooleanType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef2 = new MethodDefinition(new IntType(), methodName, method2Params, new ExpStmt(new IntegerExp(0)));
		methodsForClass.put(methodName, methodDef2);
		typechecker.methods.put(className, methodsForClass);
		// Now we make the expected output
		final MethodDefinition expected = methodDef;
		// Now we actually test
		final MethodDefinition received = typechecker.getMethodDef(className, methodName, methodParams.size());
		assertEquals(expected, received);
	}
	
	//tests getMethodDef for class with one method that matches the numOfParams but not the name
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testGetMethodDefOneMethodWithDiffName() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("dog");
		final Methodname methodName = new Methodname("findNum");
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), methodName, methodParams, new ExpStmt(new IntegerExp(0)));
		final DuplicateMap<Methodname, MethodDefinition> methodsForClass = new DuplicateMap<Methodname, MethodDefinition>();
		methodsForClass.put(methodName, methodDef);
		typechecker.methods.put(className, methodsForClass);
		typechecker.getMethodDef(className, new Methodname("imDifferent"), 1);
	}
	
	//tests getMethodDef for class with one method that has specified name but not the right num of params
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testGetMethodDefOneMethodWithSameNameButDiffNumOfParams() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("dog");
		final Methodname methodName = new Methodname("findNum");
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), methodName, methodParams, new ExpStmt(new IntegerExp(0)));
		final DuplicateMap<Methodname, MethodDefinition> methodsForClass = new DuplicateMap<Methodname, MethodDefinition>();
		methodsForClass.put(methodName, methodDef);
		typechecker.methods.put(className, methodsForClass);
		typechecker.getMethodDef(className, methodName, 2);
	}

	//tests expectedReturnTypeForClassAndMethod for int type method
	@Test
	public void testExpectedReturnTypeForClassAndMethod() throws TypeErrorException {
		// calls getDefMethod
		// takes in: Clasname, Methodname, int(num of params)
		// returns: Type
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("dog");
		final Methodname methodName = new Methodname("findNum");
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams,
				new ExpStmt(new IntegerExp(0)));
		final DuplicateMap<Methodname, MethodDefinition> methodsForClass = new DuplicateMap<Methodname, MethodDefinition>();
		methodsForClass.put(methodName, methodDef);
		typechecker.methods.put(className, methodsForClass);
		final Type expected = methodDef.type;
		final Type received = typechecker.expectedReturnTypeForClassAndMethod(className, methodName, methodParams.size());
		assertEquals(expected, received);
	}

	//tests expectedParameterTypesForClassAndMethod for class with one method of that name
	@Test
	public void testExpectedParameterTypesForClassAndMethod() throws TypeErrorException {
		// calls getDefMethod 
		// takes in: Classname, Methodname, int(num of params)
		// returns: List<Type>
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Classname className = new Classname("dog");
		final Methodname methodName = new Methodname("findNum");
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		methodParams.add(new Parameter(new IntType(), new VariableExp(new Variable("x"))));
		final MethodDefinition methodDef = new MethodDefinition(new IntType(), new Methodname("findNum"), methodParams,
				new ExpStmt(new IntegerExp(0)));
		final DuplicateMap<Methodname, MethodDefinition> methodsForClass = new DuplicateMap<Methodname, MethodDefinition>();
		methodsForClass.put(methodName, methodDef);
		typechecker.methods.put(className, methodsForClass);
		// Here we create our expected
		final List<Type> expected = new ArrayList<Type>();
		expected.add(methodParams.get(0).parameterType);

		final List<Type> received = typechecker.expectedParameterTypesForClassAndMethod(className, methodName, methodParams.size());
		assertEquals(expected, received);
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
	
	//tests typeOfMethodCall for a class with one method and that method has a string return type
	@Test
	public void testTypeOfMethodCallStringReturnTypeMethod() throws TypeErrorException {
		//takes in: VarMethodCall, Map<Variable, Type> Classname
		//returns: Type
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final List<MethodDefinition> methodDefs = new ArrayList<MethodDefinition>();
		final MethodDefinition methodDef = new MethodDefinition(new StringType(), new Methodname("bark"), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)));
		methodDefs.add(methodDef);
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Animal"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), methodDefs);
		final Classname classname = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		map.put(classname, classDef);
		typechecker.classes.put(classname, classDef);
		final DuplicateMap<Methodname, MethodDefinition> theMethods = new DuplicateMap<Methodname, MethodDefinition>();
		theMethods.put(new Methodname("bark"), methodDef);
		typechecker.methods.put(classname, theMethods);
		final List<Exp> inParens = new ArrayList<Exp>();
		final VarMethodCall methodCall = new VarMethodCall(new ClassnameExp(new Classname("Dog")), new MethodNameExp(new Methodname("bark")), inParens);
		final Type expected = new StringType();
		final Type received = typechecker.typeOfMethodCall(methodCall, new HashMap<Variable, Type>(), new Classname("Dog"));
		assertEquals(expected, received);
	}
	
	//tests typeOfMethodCall for a var(such as in: var.methodname(primary_exp*)) that is not a classname
	//expecting an exception
	@Test (expected = TypeErrorException.class)
	public void testTypeOfMethodCallUnhappyPath() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final List<Exp> inParens = new ArrayList<Exp>();
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("Dog"), new StringType());
		final VarMethodCall methodCall = new VarMethodCall(new VariableExp(new Variable("Dog")), new MethodNameExp(new Methodname("bark")), inParens);
		typechecker.typeOfMethodCall(methodCall, typeEnvironment, new Classname("Idk"));
	}
	
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
	
	// test of typeOfNew()
	// using class that has one parameter
	@Test
	public void testTypeOfNewForClassWithOneConstructorParameter() throws TypeErrorException {
		//takes in: NewClassExp, Map<Variable, Type>, Classname
		//returns: Type
 		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final Classname className = new Classname("Dog");
		final List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter(new StringType(), new VariableExp(new Variable("name"))));
		final ClassDefinition classDef = new ClassDefinition(className, new Classname("Object"), new ArrayList<VariableDeclaration>(), params, new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		typechecker.classes.put(className, classDef);
		final Type expected = new ClassnameType(className);
		final List<Exp> newClassParams = new ArrayList<Exp>();
		newClassParams.add(new StrExp("Orange"));
		final NewClassExp newClassExp = new NewClassExp(new ClassnameExp(className), newClassParams);
		final Type received = typechecker.typeOfNew(newClassExp, typeEnvironment, className);
		assertEquals(expected, received);
	}
	
	// test of typeOfNew()
	// using class that has no parameters
	@Test
	public void testTypeOfNewForClassWithEmptyConstructorParameter() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final Classname className = new Classname("Dog");
		final List<Parameter> params = new ArrayList<Parameter>();
		final ClassDefinition classDef = new ClassDefinition(className, new Classname("Object"), new ArrayList<VariableDeclaration>(), params, new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		typechecker.classes.put(className, classDef);
		final Type expected = new ClassnameType(className);
		final List<Exp> newClassParams = new ArrayList<Exp>();
		final NewClassExp newClassExp = new NewClassExp(new ClassnameExp(className), newClassParams);
		final Type received = typechecker.typeOfNew(newClassExp, typeEnvironment, className);
		assertEquals(expected, received);
	}
	
	// test of typeOfNew()
	// creating new object with incorrect parameters
	// expecting and exception
	@Test(expected = TypeErrorException.class)
	public void testTypeOfNewWithWrongConstructorParameter() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final Classname className = new Classname("Dog");
		final List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter(new StringType(), new VariableExp(new Variable("name"))));
		final ClassDefinition classDef = new ClassDefinition(className, new Classname("Object"), new ArrayList<VariableDeclaration>(), params, new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		typechecker.classes.put(className, classDef);
		final List<Exp> newClassParams = new ArrayList<Exp>();
		newClassParams.add(new IntegerExp(0));
		final NewClassExp newClassExp = new NewClassExp(new ClassnameExp(className), newClassParams);
		typechecker.typeOfNew(newClassExp, typeEnvironment, className);
	}

	//test typeOf method for a True expression
	@Test
	public void testTypeOfForTrueExp() throws TypeErrorException {
		// takes in: Exp, Map<Variable, Type>, Classname
		// returns: Type
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Type expected = new BooleanType();
		final Type received = typechecker.typeOf(new TrueExp(), null, null);
		assertEquals(expected, received);
	}

	// test typeOf method for a False expression
	@Test
	public void testTypeOfForFalseExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Type expected = new BooleanType();
		final Type received = typechecker.typeOf(new FalseExp(), null, null);
		assertEquals(expected, received);
	}

	// test typeOf method for an Integer expression
	@Test
	public void testTypeOfForIntegerExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Type expected = new IntType();
		final Type received = typechecker.typeOf(new IntegerExp(0), null, null);
		assertEquals(expected, received);
	}

	// test typeOf method for a String expression
	@Test
	public void testTypeOfForStrExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Type expected = new StringType();
		final Type received = typechecker.typeOf(new StrExp("hi"), null, null);
		assertEquals(expected, received);
	}

	// test typeOf method for a Classname expression
	@Test
	public void testTypeOfForClassnameExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Type expected = new ClassnameType(new Classname("Dog"));
		final Type received = typechecker.typeOf(new ClassnameExp(new Classname("Dog")), null, null);
		assertEquals(expected, received);
	}

	// test typeOf method for a Variable expression
	@Test
	public void testTypeOfForVariableExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(
				new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("x"), new IntType());
		final Type expected = new IntType();
		final Type received = typechecker.typeOf(new VariableExp(new Variable("x")), typeEnvironment, null);
		assertEquals(expected, received);
	}
	
	//test typeOf method for an OpExp expression
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
	
	//tests typeOf method for a VarMethodCall
	@Test
	public void testTypeOfForVarMethodCall() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final List<MethodDefinition> methodDefs = new ArrayList<MethodDefinition>();
		final MethodDefinition methodDef = new MethodDefinition(new StringType(), new Methodname("bark"), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)));
		methodDefs.add(methodDef);
		final ClassDefinition classDef = new ClassDefinition(new Classname("Dog"), new Classname("Animal"), new ArrayList<VariableDeclaration>(), new ArrayList<Parameter>(), new ExpStmt(new IntegerExp(0)), methodDefs);
		final Classname classname = new Classname("Dog");
		final Map<Classname, ClassDefinition> map = new HashMap<Classname, ClassDefinition>();
		map.put(classname, classDef);
		typechecker.classes.put(classname, classDef);
		final DuplicateMap<Methodname, MethodDefinition> theMethods = new DuplicateMap<Methodname, MethodDefinition>();
		theMethods.put(new Methodname("bark"), methodDef);
		typechecker.methods.put(classname, theMethods);
		final List<Exp> inParens = new ArrayList<Exp>();
		final VarMethodCall methodCall = new VarMethodCall(new ClassnameExp(new Classname("Dog")), new MethodNameExp(new Methodname("bark")), inParens);
		final Type expected = new StringType();
		final Type received = typechecker.typeOf(methodCall, new HashMap<Variable, Type>(), new Classname("Dog"));
		assertEquals(expected, received);
	}
	
	//tests typeOf method for a NewClassExp
	@Test
	public void testTypeOfForNewClassExp() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final Classname className = new Classname("Dog");
		final List<Parameter> params = new ArrayList<Parameter>();
		final ClassDefinition classDef = new ClassDefinition(className, new Classname("Object"), new ArrayList<VariableDeclaration>(), params, new ExpStmt(new IntegerExp(0)), new ArrayList<MethodDefinition>());
		typechecker.classes.put(className, classDef);
		final Type expected = new ClassnameType(className);
		final List<Exp> newClassParams = new ArrayList<Exp>();
		final NewClassExp newClassExp = new NewClassExp(new ClassnameExp(className), newClassParams);
		final Type received = typechecker.typeOf(newClassExp, typeEnvironment, className);
		assertEquals(expected, received);
	}
	
	//tests typeOf method for a non-existent expression
	@Test(expected = TypeErrorException.class)
	public void testTypeOfUnhappyPath() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		typechecker.typeOf(new MethodNameExp(new Methodname("iDontMatter")), null, null);
	}
	
	//tests addToMap method
	@Test
	public void testAddToMap() throws TypeErrorException {
		//takes in: Map<Variable, Type>, Variable, Type
		//returns: Map<Variable, Type>
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> originalTypeEnvironment = new HashMap<Variable, Type>();
		originalTypeEnvironment.put(new Variable("x"), new IntType());
		final Map<Variable, Type> received = typechecker.addToMap(originalTypeEnvironment, new Variable("y"), new StringType());
		originalTypeEnvironment.put(new Variable("y"), new StringType());
		final Map<Variable, Type> expected = originalTypeEnvironment;
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
	
	//test isWellTypedThis method for statement this.var = var . Statement also has different variable names . This.var is x . var is y  
	@Test
	public void testStatementThisDifferentVar() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("x"), new IntType());
		typeEnvironment.put(new Variable("y"), new IntType());
		final Classname classname = new Classname("doesn't matter");
		final Map<Variable, Type> expected = typeEnvironment;
		final Map<Variable, Type> received = typechecker.isWellTypedThis(new ThisStmt(new VariableExp(new Variable("x")), new VariableExp(new Variable("y"))) , typeEnvironment, classname, null);
		assertEquals(expected, received);
	}

	// test isWellTypedVar
	// int X = 0;
	@Test
	public void testIsWellTypedVarIntegerDeclaration() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final VariableDeclaration vardec = new VariableDeclaration(new IntType(), new VariableExp(new Variable("X")), new IntegerExp(0));
		final Map<Variable, Type> expected = new HashMap<Variable, Type>();
		expected.put(new Variable("X"), new IntType());
		final Map<Variable, Type> received = typechecker.isWellTypedVar(vardec, typeEnvironment, new Classname("DeweyIsCool"));
		assertEquals(received, expected);
	}

	// test isWellTypedVar
	// String X = "dog"
	@Test
	public void testIsWellTypedVarStringDeclaration() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final VariableDeclaration vardec = new VariableDeclaration(new StringType(), new VariableExp(new Variable("X")), new StrExp("dog"));
		final Map<Variable, Type> expected = new HashMap<Variable, Type>();
		expected.put(new Variable("X"), new StringType());
		final Map<Variable, Type> received = typechecker.isWellTypedVar(vardec, typeEnvironment, new Classname("DeweyIsCool"));
		assertEquals(received, expected);
	}

	// test for isWellTypedValueChange
	@Test
	public void testIsWellTypedValueChange() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final VariableValueChange valChange = new VariableValueChange(new VariableExp(new Variable("X")), new IntegerExp(0));
		typeEnvironment.put(new Variable("X"), new IntType());
		final Map<Variable, Type> received = typechecker.isWellTypedValueChange(valChange, typeEnvironment, new Classname("DogClass"));
		assertEquals(received, typeEnvironment);
	}

	// test for isWellTypedValueChange
	// incorrect type
	@Test(expected = TypeErrorException.class)
	public void testIsWellTypedValueChangeWrongType() throws TypeErrorException {
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final VariableValueChange valChange = new VariableValueChange(new VariableExp(new Variable("X")), new StrExp("cat"));
		typeEnvironment.put(new Variable("X"), new IntType());
		final Map<Variable, Type> expected = typeEnvironment;
		typechecker.isWellTypedValueChange(valChange, expected, new Classname("DogClass"));
	}
}