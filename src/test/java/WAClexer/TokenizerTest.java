package WAClexer;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TokenizerTest {
	// This method starts the procces of checking the input for tokens
    public void assertTokenizes(final String input, final Token[] expected) throws TokenizerException {
         Tokenizer tokenizer = new Tokenizer(input);
         List<Token> received = tokenizer.tokenize();
        assertArrayEquals(expected,received.toArray(new Token[received.size()]));     
        }
    public void assertSingleTokenizes(final String input, final Token[] expected) throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer(input);
        List<Token> received = tokenizer.tokenize();
        assertEquals(0,input.compareTo(received.get(0).toString()));
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
        assertTokenizes("Int", new Token[] { new IntToken() });
						
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
	public void testWhileByItself() throws TokenizerException {

		assertSingleTokenizes("while", new Token[]{ new WhileToken() });
	}
	@Test
	public void testBreakByItself() throws TokenizerException {
		assertSingleTokenizes("break", new Token[]{ new BreakToken() });
	}
	@Test
	public void testIfByItself() throws TokenizerException {
		assertSingleTokenizes("if", new Token[]{ new IfToken() });
	}
	@Test
	public void testElseByItself() throws TokenizerException {
		assertSingleTokenizes("else", new Token[]{ new ElseToken() });
	}
	@Test
	public void testReturnByItself() throws TokenizerException {
		assertSingleTokenizes("return", new Token[]{ new ReturnToken() });
	}
	@Test
	public void testLeftCurlyByItself() throws TokenizerException {
		assertSingleTokenizes("{", new Token[]{ new leftCurlyToken() });
	}
	
}