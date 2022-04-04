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
	
	//helper method to be used in our methods
	public void assertParses(final List<Token> input, final ParseResult[] expected) throws ParseException {
		Parser parser = new Parser(input);
		List<ParseResult> received = parser.parse();
		assertArrayEquals(expected,received.toArray(new ParseResult[received.size()]));
	}
	
	//Kyle's test
	@Test
	public void testEqualsOpExp() {
		// 1 + 1 == 1 = 1
		final OpExp first = new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(1));
		final OpExp second = new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(1));
		assertEquals(first, second);
	}
	
	//Start of Sarah's tests
	
	@Test
	public void testSingleAdditiveOp() throws ParseException {
		final List<Token> test = new ArrayList();
		test.add(new PlusToken());
		final ParseResult<Op> shouldBe = new ParseResult<Op>(new PlusOp(), 1);
		final ParseResult[] shouldBeList = new ParseResult[] {shouldBe};
		assertParses(test, shouldBeList);
	}
	
	//Int x = 5;
	//untested because methods aren't done yet
/* 	public void testSingleVardec() throws ParseException {
		final List<Token> test = new ArrayList();
		Collections.addAll(test, new IntToken(), new VariableToken(), new EqualToken(), new IntegerToken());
		final VariableDeclaration value = new VariableDeclaration(Type IntType, Exp VariableExp, Exp IntegerExp);
		final ParseResult<Vardec> shouldBe = new ParseResult<Vardec>(value, 5);
		final ParseResult [] shouldbeList = new ParseResult[] {shouldBe};
	} */
	
	//end of Sarah's tests
}