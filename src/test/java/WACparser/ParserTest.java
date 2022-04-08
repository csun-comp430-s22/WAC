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
		assertEquals(new ParseResult<Exp>(new OpExp(new IntegerExp(1), new LessThanOp(), new IntegerExp(2)), 3), parser.parseExp(0));
	}


	// new classname(exp*)
	// new Dog(12)
	@Test
	public void testParseNewClassExp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new NewToken(), new VariableToken("Dog"), new OpenparToken(), new IntegerToken(12), new CloseparToken()));
		List<Exp> inside = new ArrayList();
		inside.add(new IntegerExp(12));
		final ParseResult<Exp> expected = new ParseResult<Exp>(new NewClassExp(new VariableExp(new Variable("Dog")), inside), 5);
		assertEquals(expected, parser.parseNewClassExp(0));
	}


/* 	// x.get(hi)
	@Test
	public void testVarMethodCall () throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
											new VariableToken("hi"), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		inside.add(new VariableExp(new Variable("hi")));
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 7), parser.parseVarMethodCall(0));
	} */

}