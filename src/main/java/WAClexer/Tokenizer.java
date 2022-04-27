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
	
	
// Method to get rid of white space in front of token
	public void skipWhitespace() {
		while (offset < input.length() && Character.isWhitespace(input.charAt(offset))) {
			offset++;
		}
	}


	// returns null if it wasn't an integer token
	public IntegerToken tryTokenizeInteger() {
		skipWhitespace();

		String number = "";

		while (offset < input.length() && Character.isDigit(input.charAt(offset))) {
			number += input.charAt(offset);
			offset++;
		}
		if (number.length() > 0) {
			// convert string to integer
			return new IntegerToken(Integer.parseInt(number));
		} else {
			return null;
		}
	}

	// return null if it fails to read in any variable or keyword
	public Token tryTokenizeVariableOrKeyword() {
		skipWhitespace();
		String name = "";

		// idea: read one character at a time
		// when we are out of characters, check what the name is
		// if the name is special(e.g., "true"), emit the special token for it (e.g.,
		// TrueToken)
		// if the name isn't special (e.g., "foo"), emit a variable token for it (e.g.,
		// VariableToken("foo")
		//
		// First character of the variable: letter
		// Every subsequent character: letter or a digit


		if (offset < input.length() &&
			Character.isLetter(input.charAt(offset))) {
			name += input.charAt(offset);
			offset++;

			while (offset < input.length() &&
				Character.isLetterOrDigit(input.charAt(offset))) {
				name += input.charAt(offset);
				offset++;
			}

			// by this point, 'name' holds a potential variable
			// 'name' could be 'true'
			// soooo we need to cover all cases from tokenizeSingle that could potentially
			// be 'name' rn
			if (name.equals("Int")) {
				return new IntToken();
			} else if (name.equals("Boolean")) {
				return new BooleanToken();
			} else if (name.equals("String")) {
				return new StringToken();
			} else if (name.equals("super")) {
				return new SuperToken();
			} else if (name.equals("while")) {
				return new WhileToken();
			} else if (name.equals("break")) {
				return new BreakToken();
			} else if (name.equals("if")) {
				return new IfToken();
			} else if (name.equals("else")) {
				return new ElseToken();
			} else if (name.equals("return")) {
				return new ReturnToken();
			} else if (name.equals("this")) {
				return new ThisToken();
			} else if (name.equals("println")) {
				return new PrintlnToken();
			} else if (name.equals("new")) {
				return new NewToken();
			} else if (name.equals("class")) {
				return new ClassToken();
			} else if (name.equals("extends")) {
				return new ExtendsToken();
			} else if (name.equals("false")) {
				return new falseToken();
			} else if (name.equals("true")) {
				return new trueToken();
			} else {
				return new VariableToken(name);
			}
		} 
		else {
			return null;
		}
	}
  
  // Returns a list of tokens and checks if the tokens are exclusively operators , program structure symbols or program key words but token be in more than 1 of the categorizes .
  // The method checks the tokens with operator and program structure symbols first then program key words last 	
	
	// returns null if it couldn't read in a symbol
	public Token tryTokenizeSymbol() {
		skipWhitespace();
		Token retval = null;
		if (input.startsWith("+", offset)) {
			offset += 1;
			retval = new PlusToken();
		} else if (input.startsWith("-", offset)) {
			offset += 1;
			retval = new MinusToken();
		} else if (input.startsWith("*", offset)) {
			offset += 1;
			retval = new MultiplicationToken();
		} else if (input.startsWith("/", offset)) {
			offset += 1;
			retval = new DivisionToken();
		} else if (input.startsWith("==", offset)) {
			offset += 2;
			retval = new equalEqualToken();
		} else if (input.startsWith("{", offset)) {
			offset += 1;
			retval = new leftCurlyToken();
		} else if (input.startsWith("(", offset)) {
			offset += 1;
			retval = new OpenparToken();
		} else if (input.startsWith(")", offset)) {
			offset += 1;
			retval = new CloseparToken();
		} else if (input.startsWith(";", offset)) {
			offset += 1;
			retval = new SemicolToken();
		} else if (input.startsWith("=", offset)) {
			offset += 1;
			retval = new EqualToken();
		} else if (input.startsWith(">", offset)) {
			offset += 1;
			retval = new greaterThanToken();
		} else if (input.startsWith("<", offset)) {
			offset += 1;
			retval = new lessThanToken();
		} else if (input.startsWith("!=", offset)) {
			offset += 2;
			retval = new notEqualToken();
		} else if (input.startsWith("}", offset)) {
			offset += 1;
			retval = new rightCurlyToken();
		} else if (input.startsWith(".", offset)) {
			offset += 1;
			retval = new PeriodToken();
		} else if (input.startsWith(",", offset)) {
			offset += 1;
			retval = new CommaToken();
		}
		return retval;
	}
	
  	public Token tryTokenizeStr() {
		  skipWhitespace();
		  Token retval = null;
		  String value = "";
		
		  if (offset < input.length() && (input.startsWith("\"", offset))) {
			  value += input.charAt(offset);
			  offset++;
			  while (offset < input.length() && Character.isDefined(input.charAt(offset))) {
				  if (input.startsWith("\"", offset)) {
					  value += input.charAt(offset);
					  offset ++;
					  retval = new strToken(value);
					  return retval;
				  } else {
					  value += input.charAt(offset);
					  offset++;
				}
			}
		}
		return retval;
	}
	

	// returns null if there are no more tokens left
	public Token tokenizeSingle() throws TokenizerException {
		Token retval = null;
		skipWhitespace();
		if (offset < input.length()&&
			(retval = tryTokenizeVariableOrKeyword()) == null &&
			(retval = tryTokenizeInteger()) == null &&
			(retval = tryTokenizeSymbol()) == null &&
			(retval = tryTokenizeStr()) == null) {

			throw new TokenizerException();
		}

		/*
		 * if (offset < input.length()) {
		 * retval = tryTokenizeVariableOrKeyword();
		 * if (retval == null) {
		 * retval = tryTokenizeInteger();
		 * if (retval == null) {
		 * retval = tryTokenizeSymbol();
		 * if (retval == null) {
		 * throw new TokenizerException();
		 * }
		 * }
		 * }
		 * }
		 */

		return retval;
	}

	public List<Token> tokenize() throws TokenizerException {
		final List<Token> tokens = new ArrayList<Token>();
		
		Token token = tokenizeSingle();
		while (token != null) {
			tokens.add(token);
			token = tokenizeSingle();
		}
		return tokens;
	}
}