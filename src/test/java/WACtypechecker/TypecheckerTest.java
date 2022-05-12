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
	
	//tests typeOfVariable method
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
	
	
	//test typeOfVariable method
	@Test (expected = TypeErrorException.class)
	public void testVariableOutOfScope() throws TypeErrorException {
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), new ArrayList<Stmt>()));
		// x is not in the typeEnvironment
		typechecker.typeOfVariable(new VariableExp(new Variable("x")), typeEnvironment);
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
}