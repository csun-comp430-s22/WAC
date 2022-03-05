package WAClexer;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenizerTest {
	//annotation
	@Test
	public void testEmptyString() throws TokenizerException {
		//check that tokenizing empty string works
 		Tokenizer tokenizer = new Tokenizer("");
		List<Token> tokens = tokenizer.tokenize();
		assertEquals(0, tokens.size());
		//assert(tokens.size() == 0);
	}
	
	@Test
	public void testOnlyWhitespace() throws TokenizerException {
		Tokenizer tokenizer = new Tokenizer("    ");
		List<Token> tokens = tokenizer.tokenize();
		assertEquals(0, tokens.size());
		//assert(tokens.size() == 0);
	}
	
	@Test
	public void testIntByItself() throws TokenizerException {
		Tokenizer tokenizer = new Tokenizer("Int");
		List<Token> tokens = tokenizer.tokenize();
		assertEquals(1, tokens.size());
		//assert(tokens.size() == 1);
		Token intToken = tokens.get(0);
		assertTrue(intToken instanceof IntToken);
		//assert(intToken instanceof IntToken);
	}
	
/* 	public static void main(String[] args) throws TokenizerException {
		testOnlyWhitespace();
		testEmptyString();
		testIntByItself();
	} */
}