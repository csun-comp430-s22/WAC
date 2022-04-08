package WACparser;

import WAClexer.*;

import java.util.*;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ParserTest {

	// Kyle's test
	@Test
	public void testEqualsOpExp() {
		// 1 + 1 == 1 = 1
		final OpExp first = new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(1));
		final OpExp second = new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(1));
		assertEquals(first, second);
	}

	@Test
	public void testTypeInt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken()));
		assertEquals(new ParseResult<Type>(new IntType(), 1), parser.parseType(0));
	}

	@Test
	public void testTypeBoolean() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new BooleanToken()));
		assertEquals(new ParseResult<Type>(new BooleanType(), 1), parser.parseType(0));
	}

	@Test
	public void testTypeString() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new StringToken()));
		assertEquals(new ParseResult<Type>(new StringType(), 1), parser.parseType(0));
	}

	@Test
	public void testTypeClassname() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x")));
		assertEquals(new ParseResult<Type>(new ClassnameType(new Classname("x")), 1),
				parser.parseType(0));
	}

	// new code will come back to this after parsing methods are finished
	@Test
	public void testPrimaryInteger() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
		assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1), parser.parsePrimaryExp(0));
	}

	@Test
	public void testPrimaryVariable() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x")));
		assertEquals(new ParseResult<Exp>(new VariableExp(new Variable("x")), 1),
				parser.parsePrimaryExp(0));
	}

	@Test
	public void testPrimaryString() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new strToken("String")));
		assertEquals(new ParseResult<Exp>(new StrExp("String"), 1),
				parser.parsePrimaryExp(0));
	}

	@Test
	public void testPrimaryTrue() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new trueToken()));
		assertEquals(new ParseResult<Exp>(new TrueExp(), 1),
				parser.parsePrimaryExp(0));
	}

	@Test
	public void testPrimaryFalse() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new falseToken()));
		assertEquals(new ParseResult<Exp>(new FalseExp(), 1),
				parser.parsePrimaryExp(0));
	}

	@Test
	public void testMultiplicativeOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new MultiplicationToken()));
		assertEquals(new ParseResult<Op>(new MultiplicationOp(), 1), parser.parseMultiplicativeOp(0));
	}

	@Test
	public void testDivisionOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new DivisionToken()));
		assertEquals(new ParseResult<Op>(new DivisionOp(), 1), parser.parseMultiplicativeOp(0));
	}

	@Test
	public void testMultiplicativeExpOnlyPrimary() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
		assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1), parser.parseMultiplicativeExp(0));
	}

	@Test
	public void testMultiplicativeExpSingleOperator() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
				new MultiplicationToken(),
				new IntegerToken(2)));
		assertEquals(new ParseResult<Exp>(new OpExp(new IntegerExp(1),
				new MultiplicationOp(),
				new IntegerExp(2)),
				3),
				parser.parseMultiplicativeExp(0));
	}

	@Test
	public void testMultiplicativeExpMultiOperator() throws ParseException {
		// 1 + 2 - 3 ==> (1 + 2) - 3
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
				new MultiplicationToken(),
				new IntegerToken(2),
				new DivisionToken(),
				new IntegerToken(3)));
		final Exp expected = new OpExp(new OpExp(new IntegerExp(1),
				new MultiplicationOp(),
				new IntegerExp(2)),
				new DivisionOp(),
				new IntegerExp(3));
		assertEquals(new ParseResult<Exp>(expected, 5),
				parser.parseMultiplicativeExp(0));
	}

	@Test
	public void testPlusOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new PlusToken()));
		assertEquals(new ParseResult<Op>(new PlusOp(), 1), parser.parseAdditiveOp(0));
	}

	@Test
	public void testMinusOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new MinusToken()));
		assertEquals(new ParseResult<Op>(new MinusOp(), 1), parser.parseAdditiveOp(0));
	}

	@Test
	public void testAdditiveExpOnlyPrimary() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
		assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1), parser.parseAdditiveExp(0));
	}

	@Test
	public void testAdditiveExpSingleOperator() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
				new PlusToken(),
				new IntegerToken(2)));
		assertEquals(new ParseResult<Exp>(new OpExp(new IntegerExp(1),
				new PlusOp(),
				new IntegerExp(2)),
				3),
				parser.parseAdditiveExp(0));
	}

	@Test
	public void testAdditiveExpMultiOperator() throws ParseException {
		// 1 + 2 - 3 ==> (1 + 2) - 3
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
				new PlusToken(),
				new IntegerToken(2),
				new MinusToken(),
				new IntegerToken(3)));
		final Exp expected = new OpExp(new OpExp(new IntegerExp(1),
				new PlusOp(),
				new IntegerExp(2)),
				new MinusOp(),
				new IntegerExp(3));
		assertEquals(new ParseResult<Exp>(expected, 5),
				parser.parseAdditiveExp(0));
	}

	@Test
	public void testLessThanOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new lessThanToken()));
		assertEquals(new ParseResult<Op>(new LessThanOp(), 1), parser.parseComparisonOp(0));
	}

	@Test
	public void testGreaterThanOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new greaterThanToken()));
		assertEquals(new ParseResult<Op>(new GreaterThanOp(), 1), parser.parseComparisonOp(0));
	}

	@Test
	public void testEqualEqualsOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new equalEqualToken()));
		assertEquals(new ParseResult<Op>(new EqualEqualsOp(), 1), parser.parseComparisonOp(0));
	}

	@Test
	public void testNotEqualsOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new notEqualToken()));
		assertEquals(new ParseResult<Op>(new NotEqualsOp(), 1), parser.parseComparisonOp(0));
	}

	@Test
	public void testComparisonExpOnlyPrimary() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
		assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1), parser.parseComparisonExp(0));
	}

	@Test
	public void testComparisonExpSingleOperator() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
				new lessThanToken(),
				new IntegerToken(2)));
		assertEquals(new ParseResult<Exp>(new OpExp(new IntegerExp(1),

				new LessThanOp(),
				new IntegerExp(2)),
				3),
				parser.parseComparisonExp(0));
	}

	// 1 < 2
	@Test
	public void testParseExpForComparison() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(1), new lessThanToken(), new IntegerToken(2)));
		assertEquals(new ParseResult<Exp>(new OpExp(new IntegerExp(1), new LessThanOp(), new IntegerExp(2)), 3),
				parser.parseExp(0));
	}

	// new classname(exp*)
	// new Dog(12)
	@Test
	public void testParseNewClassExp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new NewToken(), new VariableToken("Dog"), new OpenparToken(),
				new IntegerToken(12), new CloseparToken()));
		List<Exp> inside = new ArrayList();
		inside.add(new IntegerExp(12));
		final ParseResult<Exp> expected = new ParseResult<Exp>(
				new NewClassExp(new VariableExp(new Variable("Dog")), inside), 5);
		assertEquals(expected, parser.parseNewClassExp(0));
	}

	// x.get()
	@Test
	public void testVarMethodCall() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x"), new PeriodToken(),
				new VariableToken("get"), new OpenparToken(), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp name = new VariableExp(new Variable("get"));
		final List<Exp> inside = new ArrayList();
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 5), parser.parseVarMethodCall(0));
	}

	// x.get(hi)
	@Test
	public void testVarMethodCallWithParams() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new VariableToken("hi"), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp name = new VariableExp(new Variable("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new VariableExp(new Variable("hi")), 1);
		inside.add(param.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 6), parser.parseVarMethodCall(0));
	}

	// new classname(exp*)
	// new Dog(12)
	@Test
	public void testParseExpForNewClassExp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new NewToken(), new VariableToken("Dog"), new OpenparToken(),
				new IntegerToken(12), new CloseparToken()));
		List<Exp> inside = new ArrayList();
		inside.add(new IntegerExp(12));
		final ParseResult<Exp> expected = new ParseResult<Exp>(
				new NewClassExp(new VariableExp(new Variable("Dog")), inside), 5);
		assertEquals(expected, parser.parseExp(0));
	}

	// x.get()
	// Starting point at ParseExp
	@Test
	public void testParseExpToVarMethodCall() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x"), new PeriodToken(),
				new VariableToken("get"), new OpenparToken(), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp name = new VariableExp(new Variable("get"));
		final List<Exp> inside = new ArrayList();
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 5), parser.parseExp(0));
	}

	// int x = 3;
	@Test
	public void testparseVardec() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("x"), new EqualToken(),
				new IntegerToken(3), new SemicolToken()));
		final Type type = new IntType();
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp exp = new VariableExp(new Variable("3"));
		assertEquals(new ParseResult<Vardec>(new VariableDeclaration(type, variable, exp), 5), parser.parseVardec(0));
		;
	}

	// new classname(exp*)
	// Starting point at ParseExp
	@Test
	public void testParseExpToNewClassExp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new NewToken(), new VariableToken("Dog"), new OpenparToken(),
				new IntegerToken(12), new CloseparToken()));
		List<Exp> inside = new ArrayList();
		inside.add(new IntegerExp(12));
		final ParseResult<Exp> expected = new ParseResult<Exp>(
				new NewClassExp(new VariableExp(new Variable("Dog")), inside), 5);
		assertEquals(expected, parser.parseExp(0));
	}

	@Test
	public void testParam() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("x")));
		final ParseResult<Param> expected = new ParseResult<Param>(
				new Parameter(new IntType(), new VariableExp(new Variable("x"))), 2);

		assertEquals(expected, parser.parseParam(0));
	}
	
	
	@Test
	public void testParseVardecForStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("x"), new EqualToken(), new IntegerToken(3), new SemicolToken()));
		final Type type = new IntType();
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp exp = new VariableExp(new Variable("3"));
		final ParseResult<Vardec> variableDec = new ParseResult<Vardec>(new VariableDeclaration(type, variable, exp), 5);
		assertEquals(new ParseResult<Stmt>(new VardecStmt(variableDec), 5), parser.parseStmt(0));
	}
	
	
	// break;
	@Test
	public void testBreakStatment() throws ParseException{
		final Parser parser = new Parser(Arrays.asList(new BreakToken(), new SemicolToken()));
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BreakStmt("break",";" ),2);
		assertEquals(expected, parser.parseBreakStmt(0));
		
	}
	
	// x = 5;
	@Test
	public void testVariableValueChangeThroughStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x"), new EqualToken(), new IntegerToken(5), new SemicolToken()));
		final ParseResult<Exp> variable =  new ParseResult<Exp>(new VariableExp(new Variable("x")), 1);
		final ParseResult<Exp> exp = new ParseResult<Exp>(new IntegerExp(5), 1);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new VariableValueChange(variable.result, exp.result), 4);
		assertEquals(expected, parser.parseStmt(0));
	}


}

/*
 * //just a sanity check test
 * 
 * @Test
 * public void testTest() throws ParseException {
 * final Parser parser = new Parser(Arrays.asList(new VariableToken("hi")));
 * final ParseResult<Exp> oop = parser.parsePrimaryExp(0);
 * List<Exp> test = new ArrayList();
 * test.add(oop.result);
 * 
 * List<Exp> test2 = new ArrayList();
 * final ParseResult<Exp> oop2 = new ParseResult(new VariableExp(new
 * Variable("hi")), 1);
 * test2.add(oop2.result);
 * 
 * assertEquals(test, test2);
 * }
 */
