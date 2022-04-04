package WACparser;

import WAClexer.*;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ParserTest {
	
	public void assertParses(final List<Token> input, final ParseResult[] expected) throws ParseException {
		Parser parser = new Parser(input);
		List<ParseResult> received = parser.parse();
		assertArrayEquals(expected,received.toArray(new ParseResult[received.size()]));
	}
	
	@Test
	public void testEqualsOpExp() {
		// 1 + 1 == 1 = 1
		final OpExp first = new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(1));
		final OpExp second = new OpExp(new IntegerExp(1), new PlusOp(), new IntegerExp(1));
		assertEquals(first, second);
	}
	
	@Test
	public void testAdditiveOp() throws ParseException {
		final List<Token> test = new ArrayList();
		test.add(new PlusToken());
		final ParseResult<Op> shouldBe = new ParseResult<Op>(new PlusOp(), 1);
		final ParseResult[] shouldBeList = new ParseResult[] {shouldBe};
		assertParses(test, shouldBeList);
	}
}