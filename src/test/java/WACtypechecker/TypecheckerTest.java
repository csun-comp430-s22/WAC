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
}