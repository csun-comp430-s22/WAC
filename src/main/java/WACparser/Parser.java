package WACparser;

import WAClexer.*;

import java.util.List;
import java.util.ArrayList;

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

	
	public void assertTokenHereIs(final int position, final Token expected) throws ParseException {
		final Token received = getToken(position);
		if (!expected.equals(received)) {
			throw new ParseException("expected: " + expected + "; received: " + received);
		}
	}
	
	/* // primary_exp ::= x | i | ‘(‘ exp ‘)’
	public ParseResult<Exp> parsePrimaryExp(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof VariableToken) {
			final String name = ((VariableToken)token).name;
			return new ParseResult<Exp>(new VariableExp(name), position + 1);
		} else if (token instanceof IntegerToken) {
			final int value = ((IntegerToken)token).value;
		return new ParseResult<Exp>(new IntegerExp(value), position + 1);
		} else if (token instanceof OpenparToken) {
			final ParseResult<Exp> inParens = parseExp(position + 1);
			assertTokenHereIs(inParens.position, new CloseparToken());
			return new ParseResult<Exp>(inParens.result, inParens.position + 1);
		}
	}
	
	// additive_op ::= + | -
	public ParseResult<Op> parseAdditiveOp(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof PlusToken) {
			return new ParseResult<Op>(new PlusOp(), position + 1);
		} else if (token instanceof MinusToken) {
			return new ParseResult<Op>(new MinusOp(), position + 1);
		} else {
			throw new ParseException("expected + or -; received: " + token);
		}
	}
	
	//additive_exp ::= primary_exp (additive_op primary_exp)*
	public ParseResult<Exp> parseAdditiveExp(final int position) throws ParseException {
		ParseResult<Exp> current = parsePrimaryExp(position);
		boolean shouldRun = true;
		
		while (shouldRun) {
			try {
				final ParseResult<Op> additiveOp = parseAdditiveOp(current.position);
				final ParseResult<Exp> anotherPrimary = parsePrimaryExp(additiveOp.position);
				current = new ParseResult<Exp>(new OpExp(current.result, additiveOp.result, anotherPrimary.result), anotherPrimary.position);
			} catch (final ParseException e) {
				shouldRun = false;
			}
		}
		return current;
	}
	
	// stmt ::= if (exp) stmt else stmt | { stmt }* | println(exp);
	public ParseResult<Stmt> parseStmt(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof IfToken) {
			assertTokenHereIs(position + 1, new OpenparToken());
			final ParseResult<Exp> guard = parseExp(position + 2);
			assertTokenHereIs(guard.position, new CloseparToken());
			final ParseResult<Stmt> trueBranch = parseStmt(guard.position + 1);
			assertTokenHereIs(trueBranch.position, new ElseToken());
			final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
			return new ParseResult<Stmt>(new IfExp(guard.result, trueBranch.result, falseBranch.result), falseBranch.position);
		} else if (token instanceof leftCurlyToken) {
			final List<Stmt> stmts = new ArrayList<Stmt>();
			int curPosition = position + 1;
			boolean shouldRun = true;
			while (shouldRun) {
				try {
					final ParseResult<Stmt> stmt = parseStmt(curPosition);
					stmts.add(stmt.result);
					curPosition = stmt.position;
				} catch (final ParseException e) {
					shouldRun = false;
				}
			}
			return new ParseResult<Stmt>(new BlockStmt(stmts), curPosition);
		} else if (token instanceof PrintlnToken) {
			assertTokenHereIs(position + 1, new OpenparToken());
			final ParseResult<Exp> exp = parseExp(position + 2);
			assertTokenHereIs(exp.position, new CloseparToken());
			assertTokenHereIs(exp.position + 1, new SemicolToken());
			return new ParseResult<Stmt>(new PrintlnStmt(exp.result), exp.position + 2);
		} else {
			throw new ParseException("expected statement; received: " + token);
		}
	}
	 */
}