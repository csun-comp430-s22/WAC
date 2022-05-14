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
	
	
	// Test to check that assertTokenHereIs method throws an exception when expected to
	@Test(expected = ParseException.class)
	public void testAssertTokenHereIsUnhappyPath() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken()));
		parser.assertTokenHereIs(0, new BooleanToken());
	}


	// Int
	@Test
	public void testTypeInt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken()));
		assertEquals(new ParseResult<Type>(new IntType(), 1), parser.parseType(0));
	}


	// Boolean
	@Test
	public void testTypeBoolean() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new BooleanToken()));
		assertEquals(new ParseResult<Type>(new BooleanType(), 1), parser.parseType(0));
	}

	
	// String
	@Test
	public void testTypeString() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new StringToken()));
		assertEquals(new ParseResult<Type>(new StringType(), 1), parser.parseType(0));
	}


	// x
	@Test
	public void testTypeClassname() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x")));
		assertEquals(new ParseResult<Type>(new ClassnameType(new Classname("x")), 1),
				parser.parseType(0));
	}
	
	
	// Test to check that parseType throws a ParseException when expected to
	@Test(expected = ParseException.class)
	public void testparseTypeUnhappyPath() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new MultiplicationToken()));
		parser.parseType(0);
	}


	// 123
	@Test
	public void testPrimaryInteger() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
		assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1), parser.parsePrimaryExp(0));
	}


	// x
	@Test
	public void testPrimaryVariable() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x")));
		assertEquals(new ParseResult<Exp>(new VariableExp(new Variable("x")), 1),
				parser.parsePrimaryExp(0));
	}


	// "String"
	@Test
	public void testPrimaryString() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new strToken("String")));
		assertEquals(new ParseResult<Exp>(new StrExp("String"), 1),
				parser.parsePrimaryExp(0));
	}


	// true
	@Test
	public void testPrimaryTrue() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new trueToken()));
		assertEquals(new ParseResult<Exp>(new TrueExp(), 1),
				parser.parsePrimaryExp(0));
	}


	// false
	@Test
	public void testPrimaryFalse() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new falseToken()));
		assertEquals(new ParseResult<Exp>(new FalseExp(), 1),
				parser.parsePrimaryExp(0));
	}


	// *
	@Test
	public void testMultiplicativeOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new MultiplicationToken()));
		assertEquals(new ParseResult<Op>(new MultiplicationOp(), 1), parser.parseMultiplicativeOp(0));
	}


	// /
	@Test
	public void testDivisionOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new DivisionToken()));
		assertEquals(new ParseResult<Op>(new DivisionOp(), 1), parser.parseMultiplicativeOp(0));
	}


	// 123
	@Test
	public void testMultiplicativeExpOnlyPrimary() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
		assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1), parser.parseMultiplicativeExp(0));
	}


	// 1 * 2
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


	// 1 + 2 - 3 ==> (1 + 2) - 3
	@Test
	public void testMultiplicativeExpMultiOperator() throws ParseException {
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


	// +
	@Test
	public void testPlusOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new PlusToken()));
		assertEquals(new ParseResult<Op>(new PlusOp(), 1), parser.parseAdditiveOp(0));
	}


	// -
	@Test
	public void testMinusOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new MinusToken()));
		assertEquals(new ParseResult<Op>(new MinusOp(), 1), parser.parseAdditiveOp(0));
	}


	// 123
	@Test
	public void testAdditiveExpOnlyPrimary() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
		assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1), parser.parseAdditiveExp(0));
	}


	// 1 + 2
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


	// 1 + 2 - 3 ==> (1 + 2) - 3
	@Test
	public void testAdditiveExpMultiOperator() throws ParseException {
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


	// -
	@Test
	public void testLessThanOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new lessThanToken()));
		assertEquals(new ParseResult<Op>(new LessThanOp(), 1), parser.parseComparisonOp(0));
	}


	// >
	@Test
	public void testGreaterThanOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new greaterThanToken()));
		assertEquals(new ParseResult<Op>(new GreaterThanOp(), 1), parser.parseComparisonOp(0));
	}


	// ==
	@Test
	public void testEqualEqualsOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new equalEqualToken()));
		assertEquals(new ParseResult<Op>(new EqualEqualsOp(), 1), parser.parseComparisonOp(0));
	}


	// !=
	@Test
	public void testNotEqualsOp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new notEqualToken()));
		assertEquals(new ParseResult<Op>(new NotEqualsOp(), 1), parser.parseComparisonOp(0));
	}


	// Test to check that parseComparisonOp throws an Exception when expected to
	@Test(expected = ParseException.class)
	public void testParseComparisonOpUnhappyPath() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new PlusToken()));
		parser.parseComparisonOp(0);
	}
	
	// 123
	@Test
	public void testComparisonExpOnlyPrimary() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
		assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1), parser.parseComparisonExp(0));
	}
	
	
	// 1 < 2
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
	
	
	// 1 != 2
	@Test
	public void testParseExpForNotEqualsComparison() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(1), new notEqualToken(), new IntegerToken(2)));
		assertEquals(new ParseResult<Exp>(new OpExp(new IntegerExp(1), new NotEqualsOp(), new IntegerExp(2)), 3),
				parser.parseExp(0));
	}
	
	// 1 == 2
	@Test
	public void testParseExpForEqualEqualsComparison() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(1), new equalEqualToken(), new IntegerToken(2)));
		assertEquals(new ParseResult<Exp>(new OpExp(new IntegerExp(1), new EqualEqualsOp(), new IntegerExp(2)), 3),
				parser.parseExp(0));
	}
	
	
	// x
	@Test
	public void testParseExpForSingleVariableShouldFallThru() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x")));
		assertEquals(new ParseResult<Exp>(new VariableExp(new Variable("x")), 1), parser.parseExp(0));
	}


	//
	@Test
	public void testParseClassName() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("Dog")));
		assertEquals(new ParseResult<Exp>(new ClassnameExp(new Classname("Dog")), 1), parser.parseClassName(0));
	}

	// new Node()
	@Test
	public void testParseNewClassExpNoParams() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new NewToken(), new VariableToken("Node"), new OpenparToken(), new CloseparToken()));
		List<Exp> inside = new ArrayList();
		final ParseResult<Exp> expected = new ParseResult<Exp>(new NewClassExp(new ClassnameExp(new Classname("Node")), inside), 4);
		assertEquals(expected, parser.parseNewClassExp(0));
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
				new NewClassExp(new ClassnameExp(new Classname("Dog")), inside), 5);
		assertEquals(expected, parser.parseNewClassExp(0));
	}
	
	
	// new Dog(12, 5)
	@Test
	public void testParseNewClassExpWithTwoParams() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new NewToken(), new VariableToken("Dog"), new OpenparToken(),
														new IntegerToken(12), new CommaToken(), new IntegerToken(5), new CloseparToken()));
		List<Exp> inside = new ArrayList();
		inside.add(new IntegerExp(12));
		inside.add(new IntegerExp(5));
		final ParseResult<Exp> expected = new ParseResult<Exp>(
				new NewClassExp(new ClassnameExp(new Classname("Dog")), inside), 7);
		assertEquals(expected, parser.parseNewClassExp(0));
	}


	// new classname(exp*)
	// new Dog(12)
	@Test
	public void testParseExpForNewClassExpThruExp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new NewToken(), new VariableToken("Dog"), new OpenparToken(),
				new IntegerToken(12), new CloseparToken()));
		List<Exp> inside = new ArrayList();
		inside.add(new IntegerExp(12));
		final ParseResult<Exp> expected = new ParseResult<Exp>(
				new NewClassExp(new ClassnameExp(new Classname("Dog")), inside), 5);
		assertEquals(expected, parser.parseExp(0));
	}
	
	
	// x.get()
	@Test
	public void testVarMethodCall() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x"), new PeriodToken(),
				new VariableToken("get"), new OpenparToken(), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 5), parser.parseVarMethodCall(0));
	}
	
	
	// x.get(hi)
	@Test
	public void testVarMethodCallWithVariableParam() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new VariableToken("hi"), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new VariableExp(new Variable("hi")), 1);
		inside.add(param.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 6), parser.parseVarMethodCall(0));
	}
	

	// x.get(25)
	@Test
	public void testVarMethodCallWithIntegerParam() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new IntegerToken(25), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new IntegerExp(25), 1);
		inside.add(param.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 6), parser.parseVarMethodCall(0));
	}
	
	
	// x.get("hi")
	@Test
	public void testVarMethodCallWithStringParam() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new strToken("hi"), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new StrExp("hi"), 1);
		inside.add(param.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 6), parser.parseVarMethodCall(0));
	}
	
	
	// x.get(true)
	@Test
	public void testVarMethodCallWithTrueParam() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new trueToken(), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new TrueExp(), 1);
		inside.add(param.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 6), parser.parseVarMethodCall(0));
	}
	
	
	// x.get(false)
	@Test
	public void testVarMethodCallWithFalseParam() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new falseToken(), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new FalseExp(), 1);
		inside.add(param.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 6), parser.parseVarMethodCall(0));
	}
	
	
	//x.get(hi , 25)
	@Test
	public void testVarMethodCallWithVarThenIntParams() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new VariableToken("hi"), new CommaToken(), new IntegerToken(25), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new VariableExp(new Variable("hi")), 1);
		final ParseResult<Exp> param2 = new ParseResult<Exp>(new IntegerExp(25), 1);
		inside.add(param.result);
		inside.add(param2.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 8), parser.parseVarMethodCall(0));
	}
	
	
	//x.get(hi , "hi")
	@Test
	public void testVarMethodCallWithVarThenStringParams() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new VariableToken("hi"), new CommaToken(), new strToken("hi"), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new VariableExp(new Variable("hi")), 1);
		final ParseResult<Exp> param2 = new ParseResult<Exp>(new StrExp("hi"), 1);
		inside.add(param.result);
		inside.add(param2.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 8), parser.parseVarMethodCall(0));
	}
	
	
	//x.get(hi , true)
	@Test
	public void testVarMethodCallWithVarThenTrueParams() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new VariableToken("hi"), new CommaToken(), new trueToken(), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new VariableExp(new Variable("hi")), 1);
		final ParseResult<Exp> param2 = new ParseResult<Exp>(new TrueExp(), 1);
		inside.add(param.result);
		inside.add(param2.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 8), parser.parseVarMethodCall(0));
	}
	
	
	//x.get(hi , false)
	@Test
	public void testVarMethodCallWithVarThenFalseParams() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new VariableToken("hi"), new CommaToken(), new falseToken(), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new VariableExp(new Variable("hi")), 1);
		final ParseResult<Exp> param2 = new ParseResult<Exp>(new FalseExp(), 1);
		inside.add(param.result);
		inside.add(param2.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 8), parser.parseVarMethodCall(0));
	}
	
	
	//x.get(hi , x)
	@Test
	public void testVarMethodCallWithVarThenVarParams() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new VariableToken("hi"), new CommaToken(), new VariableToken("x"), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new VariableExp(new Variable("hi")), 1);
		final ParseResult<Exp> param2 = new ParseResult<Exp>(new VariableExp(new Variable("x")), 1);
		inside.add(param.result);
		inside.add(param2.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 8), parser.parseVarMethodCall(0));
	}
	
	
	//x.get(hi , x,  5)
	@Test
	public void testVarMethodCallWithVarThenVarThenIntParams() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new VariableToken("hi"), new CommaToken(), new VariableToken("x"),  new CommaToken(), new IntegerToken(5), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new VariableExp(new Variable("hi")), 1);
		final ParseResult<Exp> param2 = new ParseResult<Exp>(new VariableExp(new Variable("x")), 1);
		final ParseResult<Exp> param3 = new ParseResult<Exp>(new IntegerExp(5), 1);
		inside.add(param.result);
		inside.add(param2.result);
		inside.add(param3.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 10), parser.parseVarMethodCall(0));
	}
	
	
	// Test to assure that we need comma seperation between primary exps
	//x.get(hi < x)
	@Test(expected = ParseException.class)
	public void testVarMethodCallWithErroneousSeperation() throws ParseException {
		final Parser parser = new Parser(
				Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
						new VariableToken("hi"), new lessThanToken(), new VariableToken("x"), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		final ParseResult<Exp> param = new ParseResult<Exp>(new VariableExp(new Variable("hi")), 1);
		final ParseResult<Exp> param2 = new ParseResult<Exp>(new VariableExp(new Variable("x")), 1);
		inside.add(param.result);
		inside.add(param2.result);
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 8), parser.parseVarMethodCall(0));
	}
	
	
	// Test to assure that parseVarMethodCall throws an Exception when expected to
	@Test(expected = ParseException.class)
	public void testVarMethodCallUnhappyPath() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x"), new PeriodToken(),
				new VariableToken("get"), new OpenparToken(), new lessThanToken(), new CloseparToken()));
		parser.parseVarMethodCall(0);
	}


	// x.get()
	@Test
	public void testVarMethodCallThruExp() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x"), new PeriodToken(),
				new VariableToken("get"), new OpenparToken(), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final MethodNameExp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 5), parser.parseExp(0));
	}


	// int x = 3;
	@Test
	public void testParseVardecIntType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("x"), new EqualToken(),
				new IntegerToken(3), new SemicolToken()));
		final Type type = new IntType();
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp exp = new VariableExp(new Variable("3"));
		assertEquals(new ParseResult<VariableDeclaration>(new VariableDeclaration(type, variable, exp), 5), parser.parseVardec(0));
	}
	
	
	// boolean x = true;
	@Test
	public void testParseVardecBooleanType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new BooleanToken(), new VariableToken("x"), new EqualToken(),
														new trueToken(), new SemicolToken()));
		final Type type = new BooleanType();
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp exp = new TrueExp();
		assertEquals(new ParseResult<VariableDeclaration>(new VariableDeclaration(type, variable, exp), 5), parser.parseVardec(0));
	}
	
	
	// Dog x = new Dog();
	@Test
	public void testParseVardecVariableType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("Dog"), new VariableToken("x"), new EqualToken(),	new NewToken(), 
														new VariableToken("Dog"), new OpenparToken(), new CloseparToken(), new SemicolToken()));
		final Type type = new ClassnameType(new Classname("Dog"));
		final Exp variable = new VariableExp(new Variable("x"));
		final List<Exp> emptyParams = new ArrayList<Exp>();
		final Exp exp = new NewClassExp(new ClassnameExp(new Classname("Dog")), emptyParams);
		assertEquals(new ParseResult<VariableDeclaration>(new VariableDeclaration(type, variable, exp), 8), parser.parseVardec(0));
	}
	
	
	// < x = 3;
	@Test(expected = ParseException.class)
	public void testparseVardecUnhappyPath1() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new lessThanToken(), new VariableToken("x"), new EqualToken(),
				new IntegerToken(3), new SemicolToken()));
		parser.parseVardec(0);
	}
	
	
	// int x > 3;
	@Test(expected = ParseException.class)
	public void testparseVardecUnhappyPath2() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("x"), new greaterThanToken(),
				new IntegerToken(3), new SemicolToken()));
		parser.parseVardec(0);
	}


	//int x
	@Test
	public void testParamIntType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("x")));
		final ParseResult<Parameter> expected = new ParseResult<Parameter>(
				new Parameter(new IntType(), new VariableExp(new Variable("x"))), 2);

		assertEquals(expected, parser.parseParam(0));
	}
	
	
	//String x
	@Test
	public void testParamStringType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new StringToken(), new VariableToken("x")));
		final ParseResult<Parameter> expected = new ParseResult<Parameter>(
				new Parameter(new StringType(), new VariableExp(new Variable("x"))), 2);

		assertEquals(expected, parser.parseParam(0));
	}
	
	
	//Dog x
	@Test
	public void testParamVariableType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("Dog"), new VariableToken("x")));
		final ParseResult<Parameter> expected = new ParseResult<Parameter>(
				new Parameter(new ClassnameType(new Classname("Dog")), new VariableExp(new Variable("x"))), 2);

		assertEquals(expected, parser.parseParam(0));
	}
	
	
	// true
	@Test(expected = ParseException.class)
	public void testParseParamUnhappyPath() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new trueToken()));
		parser.parseParam(0);
	}
	
	
	//int x = 3;
	@Test
	public void testParseVardecThruStmtIntType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("x"), new EqualToken(), new IntegerToken(3), new SemicolToken()));
		final Type type = new IntType();
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp exp = new VariableExp(new Variable("3"));
		final ParseResult<VariableDeclaration> variableDec = new ParseResult<VariableDeclaration>(new VariableDeclaration(type, variable, exp), 5);
		assertEquals(new ParseResult<Stmt>(new VardecStmt(variableDec), 5), parser.parseStmt(0));
	}
	
	
	// Boolean x = true;
	@Test
	public void testParseVardecThruStmtBooleanType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new BooleanToken(), new VariableToken("x"), new EqualToken(), new trueToken(), new SemicolToken()));
		final Type type = new BooleanType();
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp exp = new TrueExp();
		final ParseResult<VariableDeclaration> variableDec = new ParseResult<VariableDeclaration>(new VariableDeclaration(type, variable, exp), 5);
		assertEquals(new ParseResult<Stmt>(new VardecStmt(variableDec), 5), parser.parseStmt(0));
	}
	
	
	// String x = "hi";
	@Test
	public void testParseVardecThruStmtStringType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new StringToken(), new VariableToken("x"), new EqualToken(), new strToken("hi"), new SemicolToken()));
		final Type type = new StringType();
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp exp = new StrExp("hi");
		final ParseResult<VariableDeclaration> variableDec = new ParseResult<VariableDeclaration>(new VariableDeclaration(type, variable, exp), 5);
		assertEquals(new ParseResult<Stmt>(new VardecStmt(variableDec), 5), parser.parseStmt(0));
	}
	
	
	// break;
	@Test
	public void testBreakStatement() throws ParseException{
		final Parser parser = new Parser(Arrays.asList(new BreakToken(), new SemicolToken()));
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BreakStmt("break",";" ),2);
		assertEquals(expected, parser.parseBreakStmt(0));
		
	}
	
	
	// break;
	@Test
	public void testBreakStatementThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new BreakToken(), new SemicolToken()));
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BreakStmt("break",";" ),2);
		assertEquals(expected, parser.parseStmt(0));
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
	
	
	// while (x < 5) x = 1;
	@Test
	public void testWhileStmtThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new WhileToken(), new OpenparToken(), new VariableToken("x"), new lessThanToken(), new IntegerToken(5), 
														new CloseparToken(), new VariableToken("x"), new EqualToken(), new IntegerToken(1), new SemicolToken()));
		final ParseResult<Exp> guard = new ParseResult<Exp>(new OpExp(new VariableExp(new Variable("x")), new LessThanOp(), new IntegerExp(5)), 3);
		final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new VariableValueChange(new VariableExp(new Variable("x")), new IntegerExp(1)), 4);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new WhileStmt(guard.result, stmt.result), 10);
		assertEquals(expected, parser.parseStmt(0));
	}

	// if (a > 1) 
	// a = 0; 
	// else 
	// a = 1;
	// this test does not include brackets since it's one stmt each
	@Test
	public void testIfStmtWithOneStmtEachThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IfToken(), new OpenparToken(), new VariableToken("a"), new greaterThanToken(), new IntegerToken(1), new CloseparToken(), new VariableToken("a"),
														new EqualToken(), new IntegerToken(0), new SemicolToken(), new ElseToken(), new VariableToken("a"), new EqualToken(), new IntegerToken(1), new SemicolToken()));
		final ParseResult<Exp> ifGuard = new ParseResult<Exp>(new OpExp(new VariableExp(new Variable("a")), new GreaterThanOp(), new IntegerExp(1)), 3);
		final ParseResult<Stmt> trueBranch = new ParseResult<Stmt>(new VariableValueChange(new VariableExp(new Variable("a")), new IntegerExp(0)), 4);
		final ParseResult<Stmt> falseBranch = new ParseResult<Stmt>(new VariableValueChange(new VariableExp(new Variable("a")), new IntegerExp(1)), 4);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new IfStmt(ifGuard.result, trueBranch.result, falseBranch.result), 15);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	// return y;
	@Test
	public void testReturnStmtThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ReturnToken(), new VariableToken("y"), new SemicolToken()));
		final ParseResult<Exp> exp = new ParseResult<Exp>(new VariableExp(new Variable("y")), 1);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new ReturnStmt(exp.result), 3);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	// {x = y;}
	@Test
	public void testBlockStmtWithOneStmtThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new leftCurlyToken(), new VariableToken("x"), new EqualToken(), new VariableToken("y"), new SemicolToken(), new rightCurlyToken()));
		final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new VariableValueChange(new VariableExp(new Variable("x")), new VariableExp(new Variable("y"))), 3);
		final List<Stmt> stmts = new ArrayList<Stmt>();
		stmts.add(stmt1.result);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BlockStmt(stmts), 6);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	// {println();}
	@Test
	public void testBlockStmtWithOnePrintlnStmtsThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new leftCurlyToken(), new PrintlnToken(), new OpenparToken(), new CloseparToken(), new SemicolToken(), new rightCurlyToken()));
		final List<Exp> emptyPrint = new ArrayList<Exp>();
		final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new PrintlnStmt(emptyPrint), 4);
		final List<Stmt> stmts = new ArrayList<Stmt>();
		stmts.add(stmt1.result);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BlockStmt(stmts), 6);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	// {println(); println();}
	@Test
	public void testBlockStmtWithTwoPrintlnStmtsThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new leftCurlyToken(), new PrintlnToken(), new OpenparToken(), new CloseparToken(), new SemicolToken(), 
														new PrintlnToken(), new OpenparToken(), new CloseparToken(), new SemicolToken(), new rightCurlyToken()));
		final List<Exp> emptyPrint = new ArrayList<Exp>();
		final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new PrintlnStmt(emptyPrint), 4);
		final List<Stmt> stmts = new ArrayList<Stmt>();
		stmts.add(stmt1.result);
		stmts.add(stmt1.result);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BlockStmt(stmts), 10);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	// {x = y; println();}
	@Test
	public void testBlockStmtWithTwoStmtsThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new leftCurlyToken(), new VariableToken("x"), new EqualToken(), new VariableToken("y"), new SemicolToken(), 
														new PrintlnToken(), new OpenparToken(), new CloseparToken(), new SemicolToken(), new rightCurlyToken()));
		final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new VariableValueChange(new VariableExp(new Variable("x")), new VariableExp(new Variable("y"))), 3);
		final List<Exp> emptyPrint = new ArrayList<Exp>();
		final ParseResult<Stmt> stmt2 = new ParseResult<Stmt>(new PrintlnStmt(emptyPrint), 4);
		final List<Stmt> stmts = new ArrayList<Stmt>();
		stmts.add(stmt1.result);
		stmts.add(stmt2.result);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BlockStmt(stmts), 10);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	// {}
	@Test
	public void testEmptyBlockStmtThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new leftCurlyToken(), new rightCurlyToken()));
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
		assertEquals(expected, parser.parseStmt(0));
	}
	

	// super(x);
	@Test
	public void testSuperStatement() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new SuperToken(), new OpenparToken(),new VariableToken("x"),new CloseparToken(),new SemicolToken()));
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new SuperStmt("super",new VariableExp(new Variable("x"))),5);
		assertEquals(expected, parser.parseSuperStmt(0));
	}
	
	
	//super(x);
	@Test
	public void testSuperStatmentThruStmt() throws ParseException{
		final Parser parser = new Parser(Arrays.asList(new SuperToken(), new OpenparToken(),new VariableToken("x"),new CloseparToken(),new SemicolToken()));
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new SuperStmt("super",new VariableExp(new Variable("x"))),5);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	//this.x = y;
	@Test
	public void testThisStatment() throws ParseException{
		final Parser parser= new Parser(Arrays.asList(new ThisToken(), new PeriodToken(), new VariableToken("x"), new EqualToken(),new VariableToken("y"), new SemicolToken()));
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new ThisStmt(new VariableExp(new Variable("x")), new VariableExp(new Variable("y"))),6 );
		assertEquals(expected,parser.parseThisStmt(0));
	}
	
	
	//this.x = y;
	@Test
	public void testThisStatmentThruStmt() throws ParseException{
		final Parser parser= new Parser(Arrays.asList(new ThisToken(), new PeriodToken(), new VariableToken("x"), new EqualToken(),new VariableToken("y"), new SemicolToken()));
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new ThisStmt(new VariableExp(new Variable("x")), new VariableExp(new Variable("y"))),6 );
		assertEquals(expected,parser.parseStmt(0));
	}
	
	
	// y;
	@Test
	public void testFallThroughExpTruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("y"), new SemicolToken()));
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new ExpStmt(new VariableExp(new Variable("y"))), 2);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	// println();
	@Test
	public void testEmptyPrintlnStmtThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new PrintlnToken(), new OpenparToken(), new CloseparToken(), new SemicolToken()));
		final List<Exp> exps = new ArrayList<Exp>();
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new PrintlnStmt(exps), 4);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	//println("hello");
	@Test
	public void testPrintlnStmtWithOneExpThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new PrintlnToken(), new OpenparToken(), new strToken("hello"), new CloseparToken(), new SemicolToken()));
		final ParseResult<Exp> exp1 = new ParseResult<Exp>(new StrExp("hello"), 1);
		final List<Exp> exps = new ArrayList<Exp>();
		exps.add(exp1.result);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new PrintlnStmt(exps), 5);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	//println("hello", "hi");
	@Test
	public void testPrintlnStmtWithTwoExpsThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new PrintlnToken(), new OpenparToken(), new strToken("hello"), new CommaToken(),
														new strToken("hi"), new CloseparToken(), new SemicolToken()));
		final ParseResult<Exp> exp1 = new ParseResult<Exp>(new StrExp("hello"), 1);
		final ParseResult<Exp> exp2 = new ParseResult<Exp>(new StrExp("hi"), 1);
		final List<Exp> exps = new ArrayList<Exp>();
		exps.add(exp1.result);
		exps.add(exp2.result);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new PrintlnStmt(exps), 7);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	//println("hello", "hi", "hola");
	@Test
	public void testPrintlnStmtWithThreeExpsThruStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new PrintlnToken(), new OpenparToken(), new strToken("hello"), new CommaToken(),
														new strToken("hi"), new CommaToken(), new strToken("hola"), new CloseparToken(), new SemicolToken()));
		final ParseResult<Exp> exp1 = new ParseResult<Exp>(new StrExp("hello"), 1);
		final ParseResult<Exp> exp2 = new ParseResult<Exp>(new StrExp("hi"), 1);
		final ParseResult<Exp> exp3 = new ParseResult<Exp>(new StrExp("hola"), 1);
		final List<Exp> exps = new ArrayList<Exp>();
		exps.add(exp1.result);
		exps.add(exp2.result);
		exps.add(exp3.result);
		final ParseResult<Stmt> expected = new ParseResult<Stmt>(new PrintlnStmt(exps), 9);
		assertEquals(expected, parser.parseStmt(0));
	}
	
	
	// Int X() {}
	// similar to above but diff for sanity check
	// will clean up later
	@Test
	public void testParseMethoddefTestIntType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("X"), new OpenparToken(), new CloseparToken(), new leftCurlyToken(), new rightCurlyToken()));
		final ParseResult<Type> type = new ParseResult<Type>(new IntType(), 1);
		final Methodname methodname = new Methodname("X");
		final List<Parameter> params = new ArrayList<Parameter>();
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
		final ParseResult<MethodDefinition> expected = new ParseResult<MethodDefinition>(new MethodDefinition(type.result, methodname, params, stmt.result), 6);
		assertEquals(expected, parser.parseMethodDef(0));
	}
	
	
	// String X() {}
	@Test
	public void testParseMethoddefTestStringType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new StringToken(), new VariableToken("X"), new OpenparToken(), new CloseparToken(), new leftCurlyToken(), new rightCurlyToken()));
		final ParseResult<Type> type = new ParseResult<Type>(new StringType(), 1);
		final Methodname methodname = new Methodname("X");
		final List<Parameter> params = new ArrayList<Parameter>();
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
		final ParseResult<MethodDefinition> expected = new ParseResult<MethodDefinition>(new MethodDefinition(type.result, methodname, params, stmt.result), 6);
		assertEquals(expected, parser.parseMethodDef(0));
	}
	
	
	// Dog X() {}
	@Test
	public void testParseMethoddefTestVariableType() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("Dog"), new VariableToken("X"), new OpenparToken(), new CloseparToken(), new leftCurlyToken(), new rightCurlyToken()));
		final ParseResult<Type> type = new ParseResult<Type>(new ClassnameType(new Classname("Dog")), 1);
		final Methodname methodname = new Methodname("X");
		final List<Parameter> params = new ArrayList<Parameter>();
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
		final ParseResult<MethodDefinition> expected = new ParseResult<MethodDefinition>(new MethodDefinition(type.result, methodname, params, stmt.result), 6);
		assertEquals(expected, parser.parseMethodDef(0));
	}
	
	
	// Int X() {y;}
	@Test
	public void testParseMethoddefWithStmtTest() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("X"), new OpenparToken(), new CloseparToken(), new leftCurlyToken(), new VariableToken("y"), new SemicolToken(), new rightCurlyToken()));
		final ParseResult<Type> type = new ParseResult<Type>(new IntType(), 1);
		final Methodname methodname = new Methodname("X");
		final List<Parameter> params = new ArrayList<Parameter>();
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new ExpStmt(new VariableExp(new Variable("y"))), 1);
		stmts.add(stmt1.result);
		final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
		final ParseResult<MethodDefinition> expected = new ParseResult<MethodDefinition>(new MethodDefinition(type.result, methodname, params, stmt.result), 8);
		assertEquals(expected, parser.parseMethodDef(0));
	}
	
	
	// Int X(boolean a) {y;}
	@Test
	public void testParseMethoddefWithParamAndOneStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("X"), new OpenparToken(), new BooleanToken(), new VariableToken("a"), new CloseparToken(), new leftCurlyToken(), new VariableToken("y"), new SemicolToken(), new rightCurlyToken()));
		final ParseResult<Type> type = new ParseResult<Type>(new IntType(), 1);
		final Methodname methodname = new Methodname("X");
		final List<Parameter> params = new ArrayList<Parameter>();
		final ParseResult<Parameter> param1 = new ParseResult<Parameter>(new Parameter(new BooleanType(), new VariableExp(new Variable("a"))), 2);
		params.add(param1.result);
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new ExpStmt(new VariableExp(new Variable("y"))), 1);
		stmts.add(stmt1.result);
		final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
		final ParseResult<MethodDefinition> expected = new ParseResult<MethodDefinition>(new MethodDefinition(type.result, methodname, params, stmt.result), 10);
		assertEquals(expected, parser.parseMethodDef(0));
	}
	
	
	// Int X(boolean a, int x) {y;}
	@Test
	public void testParseMethoddefWithTwoParamsAndOneStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("X"), new OpenparToken(), new BooleanToken(), new VariableToken("a"), new CommaToken(),
														new IntToken(), new VariableToken("x"), new CloseparToken(), new leftCurlyToken(), new VariableToken("y"), new SemicolToken(), new rightCurlyToken()));
		final ParseResult<Type> type = new ParseResult<Type>(new IntType(), 1);
		final Methodname methodname = new Methodname("X");
		final List<Parameter> params = new ArrayList<Parameter>();
		final ParseResult<Parameter> param1 = new ParseResult<Parameter>(new Parameter(new BooleanType(), new VariableExp(new Variable("a"))), 2);
		final ParseResult<Parameter> param2 = new ParseResult<Parameter>(new Parameter(new IntType(), new VariableExp(new Variable("x"))), 2);
		params.add(param1.result);
		params.add(param2.result);
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new ExpStmt(new VariableExp(new Variable("y"))), 1);
		stmts.add(stmt1.result);
		final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
		final ParseResult<MethodDefinition> expected = new ParseResult<MethodDefinition>(new MethodDefinition(type.result, methodname, params, stmt.result), 13);
		assertEquals(expected, parser.parseMethodDef(0));
	}
	
	
	// Int X(boolean a, int x, boolean b) {y;}
	@Test
	public void testParseMethoddefWithThreeParamsAndOneStmt() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntToken(), new VariableToken("X"), new OpenparToken(), new BooleanToken(), new VariableToken("a"), new CommaToken(),
														new IntToken(), new VariableToken("x"), new CommaToken(), new BooleanToken(), new VariableToken("b"), new CloseparToken(), 
														new leftCurlyToken(), new VariableToken("y"), new SemicolToken(), new rightCurlyToken()));
		final ParseResult<Type> type = new ParseResult<Type>(new IntType(), 1);
		final Methodname methodname = new Methodname("X");
		final List<Parameter> params = new ArrayList<Parameter>();
		final ParseResult<Parameter> param1 = new ParseResult<Parameter>(new Parameter(new BooleanType(), new VariableExp(new Variable("a"))), 2);
		final ParseResult<Parameter> param2 = new ParseResult<Parameter>(new Parameter(new IntType(), new VariableExp(new Variable("x"))), 2);
		final ParseResult<Parameter> param3 = new ParseResult<Parameter>(new Parameter(new BooleanType(), new VariableExp(new Variable("b"))), 2);
		params.add(param1.result);
		params.add(param2.result);
		params.add(param3.result);
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final ParseResult<Stmt> stmt1 = new ParseResult<Stmt>(new ExpStmt(new VariableExp(new Variable("y"))), 1);
		stmts.add(stmt1.result);
		final ParseResult<Stmt> stmt = new ParseResult<Stmt>(new BlockStmt(stmts), 2);
		final ParseResult<MethodDefinition> expected = new ParseResult<MethodDefinition>(new MethodDefinition(type.result, methodname, params, stmt.result), 16);
		assertEquals(expected, parser.parseMethodDef(0));
	}
	
	
	// true
	//testing to make sure parseMethoddef throws an Exception when expected to
	@Test(expected = ParseException.class)
	public void testParseMethoddefUnhappyPath() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new trueToken()));
		parser.parseMethodDef(0);
	}
	
	
	// class Dog { Dog() 1+2; }
	@Test
	public void testParseClassDefNoExtendsNoVardecNoParamsNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new VariableToken("Dog"),	new OpenparToken(), new CloseparToken(), 
														new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, new Classname(""), vardecs, params, stmt, methoddefs), 11);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	// class Dog extends Animal { Dog() 1+2; }
	@Test
	public void testParseClassDefWithExtendsNoVardecNoParamsNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new VariableToken("Dog"),
														new OpenparToken(), new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final Classname extendsClassname = new Classname("Animal");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, extendsClassname, vardecs, params, stmt, methoddefs), 13);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog {
	//	String name = "Steve";
	//	Dog()
	//	1 + 2;
	//	}
	@Test
	public void testParseClassDefNoExtendsOneVardecTypeStringNoParamsNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new StringToken(),
														new VariableToken("name"), new EqualToken(), new strToken("Steve"), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new StringType(), new VariableExp(new Variable("name")), new StrExp("Steve"));
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, new Classname(""), vardecs, params, stmt, methoddefs), 16);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog extends Animal {
	//	String name = "Steve";
	//	Dog()
	//	1 + 2;
	//	}
	@Test
	public void testParseClassDefWithExtendsOneVardecTypeStringNoParamsNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new StringToken(),
														new VariableToken("name"), new EqualToken(), new strToken("Steve"), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final Classname extendsClassname = new Classname("Animal");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new StringType(), new VariableExp(new Variable("name")), new StrExp("Steve"));
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, extendsClassname, vardecs, params, stmt, methoddefs), 18);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog {
	//	Int x = 5;
	//	Dog()
	//	1 + 2;
	//	}
	@Test
	public void testParseClassDefNoExtendsOneVardecTypeIntNoParamsNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new IntToken(),
														new VariableToken("x"), new EqualToken(), new IntegerToken(5), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new IntType(), new VariableExp(new Variable("x")), new IntegerExp(5));
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, new Classname(""), vardecs, params, stmt, methoddefs), 16);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog extends Animal {
	//	Int x = 5;
	//	Dog()
	//	1 + 2;
	//	}
	@Test
	public void testParseClassDefWithExtendsOneVardecTypeIntNoParamsNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new IntToken(),
														new VariableToken("x"), new EqualToken(), new IntegerToken(5), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final Classname extendsClassname = new Classname("Animal");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new IntType(), new VariableExp(new Variable("x")), new IntegerExp(5));
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, extendsClassname, vardecs, params, stmt, methoddefs), 18);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog {
	//	Boolean x = false;
	//	Dog()
	//	1 + 2;
	//	}
	@Test
	public void testParseClassDefNoExtendsOneVardecTypeBooleanNoParamsNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new BooleanToken(),
														new VariableToken("x"), new EqualToken(), new falseToken(), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new BooleanType(), new VariableExp(new Variable("x")), new FalseExp());
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, new Classname(""), vardecs, params, stmt, methoddefs), 16);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog extends Animal {
	//	Boolean x = false;
	//	Dog()
	//	1 + 2;
	//	}
	@Test
	public void testParseClassDefWithExtendsOneVardecTypeBooleanNoParamsNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new BooleanToken(),
														new VariableToken("x"), new EqualToken(), new falseToken(), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final Classname extendsClassname = new Classname("Animal");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new BooleanType(), new VariableExp(new Variable("x")), new FalseExp());
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, extendsClassname, vardecs, params, stmt, methoddefs), 18);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog {
	//	String name = "Steve";
	//	Dog(int x)
	//	1 + 2;
	//	}
	@Test
	public void testParseClassDefNoExtendsOneVardecOneParamNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new StringToken(),
														new VariableToken("name"), new EqualToken(), new strToken("Steve"), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(),
														new IntToken(), new VariableToken("x"),	new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), 
														new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new StringType(), new VariableExp(new Variable("name")), new StrExp("Steve"));
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Parameter param = new Parameter(new IntType(), new VariableExp(new Variable("x")));
		params.add(param);
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, new Classname(""), vardecs, params, stmt, methoddefs), 18);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog extends Animal {
	//	String name = "Steve";
	//	Dog(int x)
	//	1 + 2;
	//	}
	@Test
	public void testParseClassDefWithExtendsOneVardecOneParamNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new StringToken(),
														new VariableToken("name"), new EqualToken(), new strToken("Steve"), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(),
														new IntToken(), new VariableToken("x"),	new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), 
														new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final Classname extendsClassname = new Classname("Animal");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new StringType(), new VariableExp(new Variable("name")), new StrExp("Steve"));
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Parameter param = new Parameter(new IntType(), new VariableExp(new Variable("x")));
		params.add(param);
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, extendsClassname, vardecs, params, stmt, methoddefs), 20);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog extends Animal {
	//	Dog(int x, int y)
	//	1 + 2;
	//	}
	@Test
	public void testParseClassDefWithExtendsNoVardecsTwoParamsNoMethoddefs() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new VariableToken("Dog"), new OpenparToken(),
														new IntToken(), new VariableToken("x"),	new CommaToken(), new IntToken(), new VariableToken("y"), new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), 
														new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final Classname extendsClassname = new Classname("Animal");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final List<Parameter> params = new ArrayList<Parameter>();
		final Parameter param = new Parameter(new IntType(), new VariableExp(new Variable("x")));
		final Parameter param1 = new Parameter(new IntType(), new VariableExp(new Variable("y")));
		params.add(param);
		params.add(param1);
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, extendsClassname, vardecs, params, stmt, methoddefs), 18);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog {
	//	String name = "Steve";
	//	Dog(int x)
	//	1 + 2;
	//  Boolean Cute() {true;}
	//	}
	@Test
	public void testParseClassDefNoExtendsOneVardecOneParamOneMethoddef() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new StringToken(),
														new VariableToken("name"), new EqualToken(), new strToken("Steve"), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(),
														new IntToken(), new VariableToken("x"),	new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), 
														new BooleanToken(), new VariableToken("Cute"), new OpenparToken(), new CloseparToken(), new leftCurlyToken(), new trueToken(), new SemicolToken(),
														new rightCurlyToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new StringType(), new VariableExp(new Variable("name")), new StrExp("Steve"));
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Parameter param = new Parameter(new IntType(), new VariableExp(new Variable("x")));
		params.add(param);
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final Stmt methodStmt = new ExpStmt(new TrueExp());
		stmts.add(methodStmt);
		final Stmt blockStmt = new BlockStmt(stmts);
		final MethodDefinition MethodDefinition = new MethodDefinition(new BooleanType(), new Methodname("Cute"), methodParams, blockStmt);
		methoddefs.add(MethodDefinition);
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, new Classname(""), vardecs, params, stmt, methoddefs), 26);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog {
	//	String name = "Steve";
	//	Dog(int x, int y)
	//	1 + 2;
	//  Boolean Cute() {true;}
	//	}
	@Test
	public void testParseClassDefNoExtendsOneVardecTwoParamsOneMethoddef() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new StringToken(),
														new VariableToken("name"), new EqualToken(), new strToken("Steve"), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(),
														new IntToken(), new VariableToken("x"),	new CommaToken(), new IntToken(), new VariableToken("y"), new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), 
														new BooleanToken(), new VariableToken("Cute"), new OpenparToken(), new CloseparToken(), new leftCurlyToken(), new trueToken(), new SemicolToken(),
														new rightCurlyToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new StringType(), new VariableExp(new Variable("name")), new StrExp("Steve"));
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Parameter param = new Parameter(new IntType(), new VariableExp(new Variable("x")));
		final Parameter param1 = new Parameter(new IntType(), new VariableExp(new Variable("y")));
		params.add(param);
		params.add(param1);
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final Stmt methodStmt = new ExpStmt(new TrueExp());
		stmts.add(methodStmt);
		final Stmt blockStmt = new BlockStmt(stmts);
		final MethodDefinition MethodDefinition = new MethodDefinition(new BooleanType(), new Methodname("Cute"), methodParams, blockStmt);
		methoddefs.add(MethodDefinition);
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, new Classname(""), vardecs, params, stmt, methoddefs), 29);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	//class Dog extends Animal {
	//	String name = "Steve";
	//	Dog(int x)
	//	1 + 2;
	//  Boolean Cute() {true;}
	//	}
	@Test
	public void testParseClassDefWithExtendsOneVardecOneParamOneMethoddef() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new StringToken(),
														new VariableToken("name"), new EqualToken(), new strToken("Steve"), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(),
														new IntToken(), new VariableToken("x"),	new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), 
														new BooleanToken(), new VariableToken("Cute"), new OpenparToken(), new CloseparToken(), new leftCurlyToken(), new trueToken(), new SemicolToken(),
														new rightCurlyToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final Classname extendsClassname = new Classname("Animal");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final VariableDeclaration vardec = new VariableDeclaration(new StringType(), new VariableExp(new Variable("name")), new StrExp("Steve"));
		vardecs.add(vardec);
		final List<Parameter> params = new ArrayList<Parameter>();
		final Parameter param = new Parameter(new IntType(), new VariableExp(new Variable("x")));
		params.add(param);
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final List<Parameter> methodParams = new ArrayList<Parameter>();
		final List<Stmt> stmts = new ArrayList<Stmt>();
		final Stmt methodStmt = new ExpStmt(new TrueExp());
		stmts.add(methodStmt);
		final Stmt blockStmt = new BlockStmt(stmts);
		final MethodDefinition MethodDefinition = new MethodDefinition(new BooleanType(), new Methodname("Cute"), methodParams, blockStmt);
		methoddefs.add(MethodDefinition);
		final ParseResult<ClassDefinition> expected = new ParseResult<ClassDefinition>(new ClassDefinition(classname, extendsClassname, vardecs, params, stmt, methoddefs), 28);
		assertEquals(expected, parser.parseClassdef(0));
	}
	
	
	// class Dog extends Animal { Dog) 1+2; }
	// testing to make sure that parseClassdef throws an Exception when we expect it to
	@Test(expected = ParseException.class)
	//@Test
	public void testParseClassDefUnhappyPathConstructorError1() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new VariableToken("Dog"), new CloseparToken(), 
														new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		parser.parseClassdef(0);
	}
	
	
	// class Dog { Dog) 1+2; }
	// testing to make sure that parseClassdef throws an Exception when we expect it to
	@Test(expected = ParseException.class)
	//@Test
	public void testParseClassDefUnhappyPathConstructorNoExtendsError1() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new VariableToken("Dog"), new CloseparToken(), 
														new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		parser.parseClassdef(0);
	}
	
	
	// class Dog extends Animal { Int() 1+2; }
	// testing to make sure that parseClassdef throws an Exception when we expect it to
	@Test(expected = ParseException.class)
	public void testParseClassDefUnhappyPathConstructorError2() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new IntToken(), new OpenparToken(), new CloseparToken(), 
														new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		parser.parseClassdef(0);
	}
	
	
	// class Dog { Int() 1+2; }
	// testing to make sure that parseClassdef throws an Exception when we expect it to
	@Test(expected = ParseException.class)
	public void testParseClassDefUnhappyPathNoExtendsConstructorError2() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new IntToken(), new OpenparToken(), new CloseparToken(), 
														new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		parser.parseClassdef(0);
	}
	
	
	//class Dog extends Animal {
	//	Int 2 = 5;
	//	Dog()
	//	1 + 2;
	//	}
	// testing to make sure that parseClassdef throws an Exception when we expect it to
	@Test(expected = ParseException.class)
	public void testParseClassDefUnhappyPathVardecError1() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new IntToken(),
														new IntegerToken(2), new EqualToken(), new IntegerToken(5), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		parser.parseClassdef(0);
	}
	
	
	//class Dog {
	//	Int 2 = 5;
	//	Dog()
	//	1 + 2;
	//	}
	// testing to make sure that parseClassdef throws an Exception when we expect it to
	@Test(expected = ParseException.class)
	public void testParseClassDefUnhappyPathNoExtendsVardecError1() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new IntToken(),
														new IntegerToken(2), new EqualToken(), new IntegerToken(5), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		parser.parseClassdef(0);
	}
	
	
	//class Dog extends Animal {
	//	2 x = 5;
	//	Dog()
	//	1 + 2;
	//	}
	// testing to make sure that parseClassdef throws an Exception when we expect it to
	@Test(expected = ParseException.class)
	public void testParseClassDefUnhappyPathVardecError2() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new IntegerToken(2),
														new VariableToken("x"), new EqualToken(), new IntegerToken(5), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		parser.parseClassdef(0);
	}
	
	
	//class Dog {
	//	2 x = 5;
	//	Dog()
	//	1 + 2;
	//	}
	// testing to make sure that parseClassdef throws an Exception when we expect it to
	@Test(expected = ParseException.class)
	public void testParseClassDefUnhappyPathNoExtendsVardecError2() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new leftCurlyToken(), new IntegerToken(2),
														new VariableToken("x"), new EqualToken(), new IntegerToken(5), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		parser.parseClassdef(0);
	}
	
	
	//class Dog
	//	Int x = 5;
	//	Dog()
	//	1 + 2;
	//	}
	// testing to make sure that parseClassdef throws an Exception when we expect it to
	@Test(expected = ParseException.class)
	public void testParseClassDefUnhappyPathMissingOpenBracket() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new IntToken(), new VariableToken("x"), new EqualToken(), new IntegerToken(5), new SemicolToken(), new VariableToken("Dog"), new OpenparToken(), 
														new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		parser.parseClassdef(0);
	}
	
	
	// Will test the program method which we're going to pass in one ClassDefinition and one statment
	// Expected a ParseResult of type Program which contains a list of classdefs and a list of stmts
	// class Dog extends Animal { Dog() 1+2; }
	@Test
	public void testParseProgramWithOneClassdefAndNoStmts () throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new VariableToken("Dog"),
														new OpenparToken(), new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken()));
		final Classname classname = new Classname("Dog");
		final Classname extendsClassname = new Classname("Animal");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ClassDefinition theClass = new ClassDefinition(classname, extendsClassname, vardecs, params, stmt, methoddefs);
		
		final List<ClassDefinition> CDList = new ArrayList<ClassDefinition>();
		CDList.add(theClass);
		final List<Stmt> StmtList = new ArrayList<Stmt>();
		final ParseResult<Program> expected = new ParseResult(new Program(CDList, StmtList), 13);
		assertEquals(expected, parser.parseProgram(0));
	}
	
	
	// class Dog extends Animal { Dog() 1+2; }
	// println(); 
	@Test
	public void testParseProgramWithOneClassdefAndOneStmts () throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new VariableToken("Dog"),
														new OpenparToken(), new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken(),
														new PrintlnToken(), new OpenparToken(), new CloseparToken(), new SemicolToken()));
		// to make the ClassDefintion
		final Classname classname = new Classname("Dog");
		final Classname extendsClassname = new Classname("Animal");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ClassDefinition theClass = new ClassDefinition(classname, extendsClassname, vardecs, params, stmt, methoddefs);
		
		// to make the Stmt
		final List<Exp> exps = new ArrayList<Exp>();
		final Stmt theStmt = new PrintlnStmt(exps);
		
		// to make the Program
		final List<ClassDefinition> CDList = new ArrayList<ClassDefinition>();
		CDList.add(theClass);
		final List<Stmt> StmtList = new ArrayList<Stmt>();
		StmtList.add(theStmt);
		final ParseResult<Program> expected = new ParseResult(new Program(CDList, StmtList), 17);
		assertEquals(expected, parser.parseProgram(0));
	}
	
	
	// same as above but passes through the parseProgram that takes no params
	// class Dog extends Animal { Dog() 1+2; }
	// println(); 
	@Test
	public void testParseProgramWithOneClassdefAndOneStmtThruNoParamsParseProgram () throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new ClassToken(), new VariableToken("Dog"), new ExtendsToken(), new VariableToken("Animal"), new leftCurlyToken(), new VariableToken("Dog"),
														new OpenparToken(), new CloseparToken(), new IntegerToken(1), new PlusToken(), new IntegerToken(2), new SemicolToken(), new rightCurlyToken(),
														new PrintlnToken(), new OpenparToken(), new CloseparToken(), new SemicolToken()));
		// to make the ClassDefintion
		final Classname classname = new Classname("Dog");
		final Classname extendsClassname = new Classname("Animal");
		final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
		final List<Parameter> params = new ArrayList<Parameter>();
		final Stmt stmt = new ExpStmt(new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(2)));
		final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
		final ClassDefinition theClass = new ClassDefinition(classname, extendsClassname, vardecs, params, stmt, methoddefs);
		
		// to make the Stmt
		final List<Exp> exps = new ArrayList<Exp>();
		final Stmt theStmt = new PrintlnStmt(exps);
		
		// to make the Program
		final List<ClassDefinition> CDList = new ArrayList<ClassDefinition>();
		CDList.add(theClass);
		final List<Stmt> StmtList = new ArrayList<Stmt>();
		StmtList.add(theStmt);
		final ParseResult<Program> expected = new ParseResult(new Program(CDList, StmtList), 17);
		assertEquals(expected.result, parser.parseProgram());
	}
	
	
	// test to make sure that parseProgram throws an Exception when it doesn't read in all the tokens
	@Test(expected = ParseException.class)
	public void testParseProgramUnhappyPath () throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new lessThanToken(), new lessThanToken(), new lessThanToken()));
		parser.parseProgram();
	}
	
	
}

