package WAClexer;

import java.util.List;

public class TokenizerTest {
	public static void testEmptyString() throws TokenizerException {
		//check that tokenizing empty string works
 		Tokenizer tokenizer = new Tokenizer("");
		List<Token> tokens = tokenizer.tokenize();
		assert(tokens.size() == 0);
	}
	
	public static void testOnlyWhitespace() throws TokenizerException {
		Tokenizer tokenizer = new Tokenizer("    ");
		List<Token> tokens = tokenizer.tokenize();
		assert(tokens.size() == 0);
	}
	
	public static void testIntByItself() throws TokenizerException {
		Tokenizer tokenizer = new Tokenizer("Int");
		List<Token> tokens = tokenizer.tokenize();
		assert(tokens.size() == 1);
		Token intToken = tokens.get(0);
		assert(intToken instanceof IntToken);
	}
	
	public static void main(String[] args) throws TokenizerException {
		testOnlyWhitespace();
		testEmptyString();
		testIntByItself();
	}
}