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
	
	//Kyle's test
	@Test
	public void testEqualsOpExp() {
		// 1 + 1 == 1 = 1
		final OpExp first = new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(1));
		final OpExp second = new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(1));
		assertEquals(first, second);
	}
	
	
	//new code will come back to this after parsing methods are finished
	@Test
	public void testPrimaryVariable() throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
		assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1), parser.parsePrimaryExp(0));
	}
	
	// x.get(hi)
	@Test
	public void testVarMethodCall () throws ParseException {
		final Parser parser = new Parser(Arrays.asList(new VariableToken("x"), new PeriodToken(), new VariableToken("get"), new OpenparToken(),
											new VariableToken("hi"), new CloseparToken()));
		final Exp variable = new VariableExp("x");
		final MethodNameExp methodName = new MethodNameExp("get");
		final List<Exp> inside = new ArrayList();
		inside.add(new VariableExp("hi"));
		final Exp expected = new VarMethodCall(variable, methodName, inside);
		assertEquals(new ParseResult<Exp>(expected, 7), parser.parseVarMethodCall(0));
	}
	
}