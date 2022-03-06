package WAClexer;

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
		while (offset < input.length() && 
				Character.isWhitespace(input.charAt(offset))) {
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
			} 
			else if (input.startsWith("constructor", offset)) {
				offset += 11;
				return new ConstructorToken();
			} else if (input.startsWith("while", offset)) {
				offset += 5;
				return new WhileToken();
			}  else if (input.startsWith("break", offset)) {
				offset += 5;
				return new BreakToken();
			}  else if (input.startsWith("if", offset)) {
				offset += 2;
				return new IfToken();
			}  else if (input.startsWith("else", offset)) {
				offset += 4;
				return new ElseToken();
			}  else if (input.startsWith("return", offset)) {
				offset += 5;
				return new ReturnToken();
			}  else if (input.startsWith("{", offset)) {
				offset += 1;
				return new LeftBracketToken();
			}  
				else {
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