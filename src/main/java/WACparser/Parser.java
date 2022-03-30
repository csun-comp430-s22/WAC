package WACparser;

import java.util.List;

public class Parser {
	private final List<Token> tokens;
	
	public Parser(final List<Token> tokens) {
		this.tokens = tokens;
	}
	
	public Token getToken(final int position) throws ParseException {
		if (position >= 0 && position < tokens.size()) {
			return tokens.get(position);
		} else {
			throw new ParseException("Invalid token position: " + position);
		}
	}
	
	//parser for op
	// op ::= +|-|<|==
	public ParseResult<Op> parseOp(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof PlusToken) {
			return new ParseResult<Op>(new PlusOp(), position + 1);
		} else if (token instanceof MinusToken) {
			return new ParseResult<Op>(new MinusOp(), position + 1);
		} else if (token instanceof LessThanOp(), position + 1) {
			return new ParseResult<Op>(new LessThanOp(), position + 1);
		} else if (token instanceof EqualEqualsOp) {
			return new ParseResult<Op>(new EqualEqualsOp(), position + 1);
		} else {
			throw new ParseException("expected operator; received " + token);
		}
	}
}