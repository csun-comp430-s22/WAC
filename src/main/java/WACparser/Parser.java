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
	} */

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


	//start of Sarah's methods

/* 	public ParseResult<Vardec> parseVardec(final int position) throws ParseException {
		final Token token = getToken(position);
		if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StringToken)) {
			final ParseResult<Type> type = parseType(position);	//dependent on Type interface and parseType method from Ruben
			assertTokenHereIs(type.position, VariableToken());
			final ParseResult<Exp> varName = parsePrimaryExp(type.position); // dependent on parsePrimaryExp method from Ruben
			assertTokenHereIs(varName.position, EqualToken());
			final ParseResult<Exp> value = parseExp(varName.position + 1);
			asserTokenHereIs(value.position, SemicolToken());
			return new ParseResult<Vardec>(new VariableDeclaration(type.result, varName.result, value.result), value.position + 1);
		} else {
			throw new ParseException("Expected a variable declaration but received: " +  token);
		}
	}

	public ParseResult<Classdef> parseClassdef(final int position) throws ParseException {
		final Token token = getToken(position);
		if  (token instanceof ClassToken) {
			final ParseResult<Exp> classname = parsePrimaryExp(position + 1);	//parse in classname
			token = getToken(classname.position);
			position = position + 2;	//sets position to thing after classname
			if (token instanceof ExtendsToken) {	//if the class extends another class
				assertTokenHereIs(classname.position + 1, VariableToken());
				final ParseResult<Exp> extendsClassname = parsePrimaryExp(classname.position + 1);	//parse in name of class it's extending
				token = getToken(extendsClassname.position);	//update the next token to be read
				position = position + 2;	//position is now set the thing after the extends classname
			}

			//here need to parse in the 0 or more vardec = exp;
			if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StringToken)) {	//if we read in a type
				assertTokenHereIs(position + 1, VariableToken());	//assert the next thing is a variable
				assertTokenHereIs(position + 2, EqualToken());	//assert the next thing is assignment operator (ensures that this is a variable instantiation) we know we have at least one variable instantiation
				boolean shouldRun = true;
				final List<ParseResult> vardecs = new ArrayList<ParseResult>();
				while (shouldRun) {
					try {
						final ParseResult<Vardec> vardec = parseVardec(position);	//calls parseVardec method to parse the "type var" section
						vardecs.add(vardec.result);
						position = vardec.position;
					} catch (final ParseException e) {
						shouldRun = false;
					}
				}
			}

			//here is where a constructor might appear
			if (token instanceof VariableToken) {
				assertTokenHereIs(position, OpenparToken());
				//left of here
			}
		}
		else {
			throw ParseException("");
		}
	} */

	//end of Sarah's methods

	public ParseResult<Type> parseType(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof IntToken) {
			return new ParseResult<Type>(new IntType(), position + 1);
		} else if (token instanceof BooleanToken) {
			return new ParseResult<Type>(new BooleanType(), position + 1);
		} else if (token instanceof StringToken) {
			return new ParseResult<Type>(new StringType(), position + 1);
		} else {
			throw new ParseException("");
		}
	}

	// primary_exp ::= var | str | int | true | false
	public ParseResult<Exp> parsePrimaryExp(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof VariableToken) {
			final String name = ((VariableToken) token).name;
			return new ParseResult<Exp>(new VariableExp(name), position + 1);
		} else if (token instanceof strToken) {
			final String value = ((strToken) token).value;
			return new ParseResult<Exp>(new StrExp(value), position + 1);
		} else if (token instanceof IntegerToken) {
			final int value = ((IntegerToken) token).value;
			return new ParseResult<Exp>(new IntegerExp(value), position + 1);
		} else if (token instanceof trueToken) {
			return new ParseResult<Exp>(new TrueExp(), position + 1);
		} else if (token instanceof falseToken) {
			return new ParseResult<Exp>(new FalseExp(), position + 1);
		} else {
			throw new ParseException("");
		}
	}

	public ParseResult<Op> parseMultiplicativeOp(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof MultiplicationToken) {
			return new ParseResult<Op>(new MultiplicationOp(), position + 1);
		} else if (token instanceof DivisionToken) {
			return new ParseResult<Op>(new DivisionOp(), position + 1);
		} else {
			throw new ParseException("expected * or /; received: " + token);
		}
	}

	public ParseResult<Exp> parseMultiplicativeExp(final int position) throws ParseException {


	/*
	//additive_exp ::= primary_exp (additive_op primary_exp)*
	public ParseResult<Exp> parseAdditiveExp(final int position) throws ParseException {
		ParseResult<Exp> current = parsePrimaryExp(position);
		boolean shouldRun = true;

		while (shouldRun) {
			try {
				final ParseResult<Op> multiplicativeOp = parseMultiplicativeOp(current.position);
				final ParseResult<Exp> anotherPrimary = parsePrimaryExp(multiplicativeOp.position);
				current = new ParseResult<Exp>(new OpExp(current.result,
						multiplicativeOp.result,
						anotherPrimary.result),
						anotherPrimary.position);
			} catch (final ParseException e) {
				shouldRun = false;
				throw new ParseException("");
			}
		}

		return current;
	}

	/*
	 * // additive_op ::= + | -
	 * public ParseResult<Op> parseAdditiveOp(final int position) throws
	 * ParseException {
	 * final Token token = getToken(position);
	 * if (token instanceof PlusToken) {
	 * return new ParseResult<Op>(new PlusOp(), position + 1);
	 * } else if (token instanceof MinusToken) {
	 * return new ParseResult<Op>(new MinusOp(), position + 1);
	 * } else {
	 * throw new ParseException("expected + or -; received: " + token);
	 * }
	 * }
	 *
	 * //additive_exp ::= primary_exp (additive_op primary_exp)*
	 * public ParseResult<Exp> parseAdditiveExp(final int position) throws
	 * ParseException {
	 * ParseResult<Exp> current = parsePrimaryExp(position);
	 * boolean shouldRun = true;
	 *
	 * while (shouldRun) {
	 * try {
	 * final ParseResult<Op> additiveOp = parseAdditiveOp(current.position);
	 * final ParseResult<Exp> anotherPrimary = parsePrimaryExp(additiveOp.position);
	 * current = new ParseResult<Exp>(new OpExp(current.result, additiveOp.result,
	 * anotherPrimary.result), anotherPrimary.position);
	 * } catch (final ParseException e) {
	 * shouldRun = false;
	 * }
	 * }
	 * return current;
	 * }
	 *
	 * // stmt ::= if (exp) stmt else stmt | { stmt }* | println(exp);
	 * public ParseResult<Stmt> parseStmt(final int position) throws ParseException
	 * {
	 * final Token token = getToken(position);
	 * if (token instanceof IfToken) {
	 * assertTokenHereIs(position + 1, new OpenparToken());
	 * final ParseResult<Exp> guard = parseExp(position + 2);
	 * assertTokenHereIs(guard.position, new CloseparToken());
	 * final ParseResult<Stmt> trueBranch = parseStmt(guard.position + 1);
	 * assertTokenHereIs(trueBranch.position, new ElseToken());
	 * final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
	 * return new ParseResult<Stmt>(new IfExp(guard.result, trueBranch.result,
	 * falseBranch.result), falseBranch.position);
	 * } else if (token instanceof leftCurlyToken) {
	 * final List<Stmt> stmts = new ArrayList<Stmt>();
	 * int curPosition = position + 1;
	 * boolean shouldRun = true;
	 * while (shouldRun) {
	 * try {
	 * final ParseResult<Stmt> stmt = parseStmt(curPosition);
	 * stmts.add(stmt.result);
	 * curPosition = stmt.position;
	 * } catch (final ParseException e) {
	 * shouldRun = false;
	 * }
	 * }
	 * return new ParseResult<Stmt>(new BlockStmt(stmts), curPosition);
	 * } else if (token instanceof PrintlnToken) {
	 * assertTokenHereIs(position + 1, new OpenparToken());
	 * final ParseResult<Exp> exp = parseExp(position + 2);
	 * assertTokenHereIs(exp.position, new CloseparToken());
	 * assertTokenHereIs(exp.position + 1, new SemicolToken());
	 * return new ParseResult<Stmt>(new PrintlnStmt(exp.result), exp.position + 2);
	 * } else {
	 * throw new ParseException("expected statement; received: " + token);
	 * }
	 * }
	 */

		public ParseResult parseSingle () throws ParseException {
			ParseResult retval;
			retval = parseAdditiveOp(0);
			return retval;
		}

		public List<ParseResult> parse () throws ParseException {
			final List<ParseResult> results = new ArrayList<ParseResult>();
			ParseResult<Op> result1 = parseSingle();
			results.add(result1);
			return results;
		}
	}

//	public ParseResult<Exp> parseAdditiveExp(final int position) throws ParseException {
//
//		public ParseResult parseSingle () throws ParseException {
//			ParseResult retval;
//			retval = parseAdditiveOp(0);
//			return retval;
//		}
//
//		public List<ParseResult> parse () throws ParseException {
//			final List<ParseResult> results = new ArrayList<ParseResult>();
//			ParseResult<Op> result1 = parseSingle();
//			results.add(result1);
//			return results;
//		}
//	}

	public ParseResult<Exp> parseAdditiveExp(final int position) throws ParseException {

		ParseResult<Exp> current = parsePrimaryExp(position);
		boolean shouldRun = true;

		while (shouldRun) {
			try {
				final ParseResult<Op> additiveOp = parseAdditiveOp(current.position);
				final ParseResult<Exp> anotherPrimary = parsePrimaryExp(additiveOp.position);
				current = new ParseResult<Exp>(new OpExp(current.result,
						additiveOp.result,
						anotherPrimary.result),
						anotherPrimary.position);
			} catch (final ParseException e) {
				shouldRun = false;
			}
		}

		return current;
	}