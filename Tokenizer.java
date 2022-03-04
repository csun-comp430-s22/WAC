import java.util.List;
import java.util.ArrayList;

public class Tokenizer {
	private final String input;
	private int offset;
	
	public Tokenizer(final String input) {
		this.input = input;
		offset = 0;
	}
	
	public void skipWhitespace() {
		while (Character.isWhitespace(input.charAt(offset))) {
			offset++;
		}	
	}
	
	//returns null if there are no more tokens left
	public Token tokenizeSingle() throws TokenizerException {
		skipWhitespace();
		if (offset < input.length()) {
			if (input.startsWith("Int", offset)) {
				offset += 3;
				return new IntToken();
			} else if (input.startsWith("Boolean", offset)) {
				offset += 7;
				return new BooleanToken();
			} else if (input.startsWith("String", offset)) {
				offset += 6;
				return new StringToken();
			} else if (input.startsWith("+", offset)) {
				offset += 1;
				return new PlusToken();
			} else if (input.startsWith("-", offset)) {
				offset += 1;
				return new MinusToken();
			} else if (input.startsWith("*", offset)) {
				offset += 1;
				return new MultiplicationToken();
			} else if (input.startsWith("/", offset)) {
				offset += 1;
				return new DivisionToken();
			} else if (input.startsWith("=", offset)) {
				offset += 1;
				return new EqualToken();
			} else if (input.startsWith("super", offset)) {
				offset += 5;
				return new SuperToken();
			} else {
				throw new TokenizerException();
			}
		} else {
			return null;
		}
	}
	
	public List<Token> tokenize() throws TokenizerException {
		final List<Token> tokens = new ArrayList<Token>();
		Token token = tokenizeSingle();
		
		while(token != null) {
			tokens.add(token);
			token = tokenizeSingle();
		}
		return tokens;
	}
}

//this is adapted from Kyle's example following it up to timestamp 54:12 from Feb 21st video.