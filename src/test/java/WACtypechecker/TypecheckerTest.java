package WACtypechecker;

import WACparser.*;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TypecheckerTest {
	public static final ExpStmt expStmt = new ExpStmt(new IntegerExp(0));
	public static final List<Stmt> stmts = new ArrayList<Stmt>(expStmt);
	public static final Typechecker emptyTypechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), stmts));
	
	@Test
	public void testVariableInScope() throws TypeErrorException {
/* 		final ExpStmt expStmt = new ExpStmt(new IntegerExp(0));
		final List<Stmt> stmts = new ArrayList<Stmt>();
		stmts.add(expStmt);
		final Typechecker typechecker = new Typechecker(new Program(new ArrayList<ClassDefinition>(), stmts)); */
		final Type expectedType = new IntType();
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		typeEnvironment.put(new Variable("x"), new IntType());
		final Type receivedType = emptyTypechecker.typeOfVariable(new VariableExp(new Variable("x")), typeEnvironment);
		assertEquals(expectedType, receivedType);
	}
	
	
	@Test (expected = TypeErrorException.class)
	public void testVariableOutOfScop() throws TypeErrorException {
		final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
		// x is not in the typeEnvironment
		emptyTypechecker.typeOfVariable(new VariableExp(new Variable("x")), typeEnvironment);
	}
}