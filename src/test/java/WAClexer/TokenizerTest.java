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
	public void testPlusByItself() throws TokenizerException {
		assertTokenizes("+", new Token[] { new PlusToken() });
	}
	
	@Test
	public void testVariable() throws TokenizerException {
		assertTokenizes("foo", new Token[] { new VariableToken("foo") });
	}
	
	@Test
	public void testIntIntIsVariable() throws TokenizerException {
		assertTokenizes("IntInt", new Token[]{ new VariableToken("IntInt") });
	}
	
	@Test
	public void testSingleDigitInteger() throws TokenizerException {
		assertTokenizes("1", new Token[]{ new IntegerToken(1) });
	}
	
	@Test
	public void testMultiDigitInteger() throws TokenizerException {
		assertTokenizes("123", new Token[]{ new IntegerToken(123) });
	}
}