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
	
	public Token tryTokenizeVariable() {
		skipWhitespace();
		String name = "";
		
		// idea: read one character at a time
		// when we are out of characters, check what the name is
		// if the name is special(e.g., "true"), emit the special token for it (e.g., TrueToken)
		// if the name isn't special (e.g., "foo"), emit a variable token for it (e.g., VariableToken("foo")
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
				return new BooleanToken();
			} else if (name.equals("super")) {
				return new SuperToken();
			} else if (name.equals("while")) {
				return new WhileToken();
			} else if (name.equals("break")) {
				return new BreakToken();
			} else if (name.equals("if")) {
				return new IfToken();
			} else if (name.equals("else") {
				return new ElseToken();
			} else if (name.equals("return") {
				return new ReturnToken();
			} else if (name.equals("this") {
				return new ThisToken();
			} else if (name.equals("println") {
				return new PrintlnToken();
			} else if (name.equals("new") {
				return new NewToken();
			} else if (name.equals("class") {
				return new ClassToken();
			} else if (name.equals("extends") {
				return new ExtendsToken();
			} else if (name.equals("false") {
				return new falseToken();
			} else if (name.equals("true") {
				return new trueToken();
			} else {
				return new VariableToken(name);
			}
		} else {
			return null;
		}
	}

	// returns null if there are no more tokens left
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
			} else if (input.startsWith("while", offset)) {
				offset += 5;
				return new WhileToken();
			} else if (input.startsWith("break", offset)) {
				offset += 5;
				return new BreakToken();
			} else if (input.startsWith("if", offset)) {
				offset += 2;
				return new IfToken();
			} else if (input.startsWith("else", offset)) {
				offset += 4;
				return new ElseToken();
			} else if (input.startsWith("return", offset)) {
				offset += 5;
				return new ReturnToken();
			} else if (input.startsWith("{", offset)) {
				offset += 1;
				return new LeftBracketToken();
			} else if (input.startsWith("this", offset)) {
				offset += 4;
				return new ThisToken();
			} else if (input.startsWith("println", offset)) {
				offset += 7;
				return new PrintlnToken();
			} else if (input.startsWith("(", offset)) {
				offset += 1;
				return new OpenparToken();
			} else if (input.startsWith(")", offset)) {
				offset += 1;
				return new CloseparToken();
			} else if (input.startsWith(";", offset)) {
				offset += 1;
				return new SemicolToken();
			} else if (input.startsWith("new", offset)) {
				offset += 3;
				return new NewToken();
			} else if (input.startsWith("class", offset)) {
				offset += 5;
				return new ClassToken();
			} else if (input.startsWith("extends", offset)) {
				offset += 7;
				return new ExtendsToken();
			} else if (input.startsWith("==", offset)) {
				offset += 2;
				return new equalEqualToken();
			} else if (input.startsWith("false", offset)) {
				offset += 5;
				return new falseToken();
			} else if (input.startsWith(">", offset)) {
				offset += 1;
				return new greaterThanToken();
			} else if (input.startsWith("<", offset)) {
				offset += 1;
				return new lessThanToken();
			} else if (input.startsWith("!=", offset)) {
				offset += 2;
				return new notEqualToken();
			} else if (input.startsWith("}", offset)) {
				offset += 1;
				return new rightCurlyToken();
			} else if (input.startsWith("true", offset)) {
				offset += 4;
				return new trueToken();
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

		while (token != null) {
			tokens.add(token);
			token = tokenizeSingle();
		}
		return tokens;
	}
}