package WAClexer;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TokenizerTest {
    public void assertTokenizes(final String input,
                                final Token[] expected) throws TokenizerException {
        final Tokenizer tokenizer = new Tokenizer(input);
        final List<Token> received = tokenizer.tokenize();
        assertArrayEquals(expected,
                          received.toArray(new Token[received.size()]));
    }
	
	//annotation
	@Test
	public void testEmptyString() throws TokenizerException {
		//check that tokenizing empty string works
		assertTokenizes("", new Token[0]);

 		//Tokenizer tokenizer = new Tokenizer("");
		//List<Token> tokens = tokenizer.tokenize();
		//assertEquals(0, tokens.size());
	}
	
	@Test
	public void testOnlyWhitespace() throws TokenizerException{
		assertTokenizes("    ", new Token[0]);
		
		//Tokenizer tokenizer = new Tokenizer("    ");
		//List<Token> tokens = tokenizer.tokenize();
		//assertEquals(0, tokens.size());
	}
	
	//start of Sarah's reserved words/symbols testing
	
    @Test
    public void testIntByItself() throws TokenizerException {
        assertTokenizes("Int",
                        new Token[] { new IntToken() });
						
/* 		Tokenizer tokenizer = new Tokenizer("Int");
		List<Token> tokens = tokenizer.tokenize();
		assertEquals(1, tokens.size());
		Token intToken = tokens.get(0);
		assertTrue(intToken instanceof IntToken); */
    }
	
	@Test
	public void testIntSpaceIntAreIntTokens() throws TokenizerException {
		assertTokenizes("Int Int", new Token[]{ new IntToken(), new IntToken() });
	}
	
	@Test
	public void testBooleanByItself() throws TokenizerException {
		assertTokenizes("Boolean", new Token[] { new BooleanToken() });
	}
	
	@Test
	public void testStringByItself() throws TokenizerException {
		assertTokenizes("String", new Token[] { new StringToken() });
	}
	
	@Test
	public void testPlusByItself() throws TokenizerException {
		assertTokenizes("+", new Token[] { new PlusToken() });
	}
	
	@Test
	public void testMinusByItself() throws TokenizerException {
		assertTokenizes("-", new Token[] { new MinusToken() });
	}
	
	@Test
	public void testMultiplicationByItself() throws TokenizerException {
		assertTokenizes("*", new Token[] { new MultiplicationToken() });
	}
	
	@Test
	public void testDivisionByItself() throws TokenizerException {
		assertTokenizes("/", new Token[] { new DivisionToken() });
	}
	
	@Test
	public void testEqualByItself() throws TokenizerException {
		assertTokenizes("=", new Token[] { new EqualToken() });
	}
	
	@Test
	public void testSuperByItself() throws TokenizerException {
		assertTokenizes("super", new Token[] { new SuperToken() });
	}
	
	//start of testing variables
	
	@Test
	public void testVariable() throws TokenizerException {
		assertTokenizes("foo", new Token[] { new VariableToken("foo") });
	}
	
	@Test
	public void testIntIntIsVariable() throws TokenizerException {
		assertTokenizes("IntInt", new Token[]{ new VariableToken("IntInt") });
	}

	// 'this' token test
	@Test
	public void testThisByItself() throws TokenizerException {
		assertTokenizes("this", new Token[] { new ThisToken() });
	}

	// 'println' token test
	@Test
	public void testPrintlnByItself() throws TokenizerException {
		assertTokenizes("println", new Token[] { new PrintlnToken() });
	}

	// '(' token test
	@Test
	public void testOpenparByItself() throws TokenizerException {
		assertTokenizes("(", new Token[] { new OpenparToken() });
	}

	// ')' token test
	@Test
	public void testCloseparByItself() throws TokenizerException {
		assertTokenizes(")", new Token[] { new CloseparToken() });
	}

	// ';' token test
	@Test
	public void testSemicolByItself() throws TokenizerException {
		assertTokenizes(";", new Token[] { new SemicolToken() });
	}

	// 'new' token test
	@Test
	public void testNewByItself() throws TokenizerException {
		assertTokenizes("new", new Token[] { new NewToken() });
	}

	// 'class' token test
	@Test
	public void testClassByItself() throws TokenizerException {
		assertTokenizes("class", new Token[] { new ClassToken() });
	}

	// 'extends' token test
	@Test
	public void testExtendsByItself() throws TokenizerException {
		assertTokenizes("extends", new Token[] { new ExtendsToken() });
	}
	
	//start of testing integers
	
	@Test
	public void testSingleDigitInteger() throws TokenizerException {
		assertTokenizes("1", new Token[]{ new IntegerToken(1) });
	}
	
	@Test
	public void testMultiDigitInteger() throws TokenizerException {
		assertTokenizes("123", new Token[]{ new IntegerToken(123) });
	}
	
	//start of testing invalid input
	
	@Test(expected = TokenizerException.class)
	public void testInvalid() throws TokenizerException {
		assertTokenizes("$", null);
	}
	
	//start of testing strings
	
	@Test
	public void testStrByItself() throws TokenizerException {
		assertTokenizes("\"hi\"", new Token[]{ new strToken("\"hi\"") });
	}
}