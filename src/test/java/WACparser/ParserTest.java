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


	// 1 < 2
	@Test
	public void testParseExpForComparison() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(1), new LessThanOp(), new IntegerToken(2)));
	}
	
	// x.get(hi)
	@Test
	public void testVarMethodCall () throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
											new VariableToken("hi"), new CloseparToken()));
		final Exp variable = new VariableExp(new Variable("x"));
		final Exp name = new MethodNameExp(new Methodname("get"));
		final List<Exp> inside = new ArrayList();
		inside.add(new VariableExp(new Variable("hi")));
		assertEquals(new ParseResult<Exp>(new VarMethodCall(variable, name, inside), 7), parser.parseVarMethodCall(0));
	}
}